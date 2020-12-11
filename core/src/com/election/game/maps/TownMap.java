package com.election.game.maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TmxMapLoader.Parameters;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.election.game.Constants;
import com.election.game.ElectionGame;
import com.election.game.Electorate;
import com.election.game.Region;
import com.election.game.Utilities;


public class TownMap {
	
	
public String id;
	
	public String name;
	public String description;

	//outside/inside map? does it matter?
	public int type;
	
	public Map<String,Electorate>	npcs;
	
	public TiledMap tiledMap;
	
	public int mapWidth;
	public int mapHeight;
	public int tilePixelWidth;
	public int tilePixelHeight;

	public int mapPixelWidth;

	public int mapPixelHeight;
	
	
	//private List<MapObject> mapObjects;
	public MapObjects mapObjs;

	public Region[][] regions;

	public ArrayList<Electorate> electorate;

	public boolean activated = false;

	public MapObjects mapCollisionObjs;

	public TownMap( String mapFile, boolean scaleToTile ){
		
		Parameters params = new Parameters();
		params.convertObjectToTileSpace = scaleToTile;
		
		tiledMap = new TmxMapLoader().load(mapFile, params);
		
		MapProperties prop = tiledMap.getProperties();

		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tilePixelWidth = prop.get("tilewidth", Integer.class);
		tilePixelHeight = prop.get("tileheight", Integer.class);
	

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;
		
		mapObjs = tiledMap.getLayers().get(Constants.MAP_OBJ_PHYSICS_LAYER).getObjects();
		
	}
	
	public void init(){
		MapProperties prop = tiledMap.getProperties();

		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tilePixelWidth = prop.get("tilewidth", Integer.class);
		tilePixelHeight = prop.get("tileheight", Integer.class);
	

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;
		
		mapObjs = tiledMap.getLayers().get(Constants.MAP_OBJ_PHYSICS_LAYER).getObjects();
		
		mapCollisionObjs = getAllMapCollisionObjects();
	}
	
	/*
	 * TODO: This should only be done once, unless the maps are dynamic
	 * which they aren't at the moment.
	 */
	public MapObjects getAllMapObjects(){
		
		MapObjects allLayerObjects = new MapObjects();
		MapLayers layers = tiledMap.getLayers();
		for (MapLayer mapLayer : layers) {
			
			MapObjects layerobjects= mapLayer.getObjects();
			for (MapObject mapObject : layerobjects) {
				
							
				allLayerObjects.add(mapObject);
				
			}
			
		}
		
		return allLayerObjects;
	}
	
	/*
	 * TODO: This should only be done once, unless the maps are dynamic
	 * which they aren't at the moment.
	 */
	public MapObjects getAllRectMapObjects(){
		
		MapObjects allLayerObjects = new MapObjects();
		MapLayers layers = tiledMap.getLayers();
		for (MapLayer mapLayer : layers) {
			
			MapObjects layerobjects= mapLayer.getObjects();
			for (MapObject mapObject : layerobjects) {
				
				if( mapObject instanceof RectangleMapObject){
					allLayerObjects.add(mapObject);
				}
			}
			
		}
		
		return allLayerObjects;
	}
	
	/*
	 * TODO: This should only be done once, unless the maps are dynamic
	 * which they aren't at the moment.
	 */
	private MapObjects getAllMapCollisionObjects(){
		
		MapObjects allLayerObjects = new MapObjects();
		MapLayers layers = tiledMap.getLayers();
		for (MapLayer mapLayer : layers) {
			
			MapObjects layerobjects= mapLayer.getObjects();
			for (MapObject mapObject : layerobjects) {
				
				String type = (String) mapObject.getProperties().get(Constants.MAP_OBJ_OBJECT_TYPE);
				
				if( type != null && 
						(type.toLowerCase().equals(Constants.MAP_OBJ_OBJECT_TYPE_COLLIDE.toLowerCase())
						|| 		type.toLowerCase().equals(Constants.MAP_OBJ_DOOR.toLowerCase()
					))){
					allLayerObjects.add(mapObject);
				}
			}
			
		}
		
		return allLayerObjects;
	}
	
	public MapObjects getMapObjects(String mapLayer){
		
		MapLayer layer = tiledMap.getLayers().get(mapLayer);
		
		if( layer != null){
			MapObjects objs = layer.getObjects();
			return objs;	
		}
		
		return null;
	}

	public void activate() {
		
		if( activated ){
			return;
		}
		
		Gdx.app.log(this.getClass().getName(), this.name + "," + this.description);
		
		electorate = new ArrayList<Electorate>();		

		//initialize all npcs in the map
		initRegions();
		initNPCs();
		
		activated = true;
		
	}
	
	private void initRegions() {
		
		int numTilesX = mapWidth;//Constants.TILE_SIZE;
		int numTilesY = mapHeight;///Constants.TILE_SIZE;
		
		
		regions = new Region[numTilesX][numTilesY];
		
		for (int i = 0; i < numTilesX; i++) {

			for (int j = 0; j < numTilesY; j++) {
				
				regions[i][j] = new Region(i, j);

				/*
				 * Region region = new Region(i, j);
				 * 
				 * //regions are drawn on 1/128 scale, so need to be scaled up to do proper
				 * collisino checking Rectangle scaledRect =
				 * Utilities.scaleRectangle(region.rect, Constants.TILE_SIZE);
				 * 
				 * for(int k=0; k < mapCollisionObjs.getCount(); k++) {
				 * 
				 * MapObject mapObj = mapCollisionObjs.get(k);
				 * 
				 * if (mapObj instanceof RectangleMapObject) { Rectangle rect =
				 * ((RectangleMapObject)mapObj).getRectangle(); //TODO: need to write method
				 * that checks if a bouding polygon or bouding rect //hits a particular region
				 * 
				 * if( scaledRect.overlaps(rect) ) { region.addCollisionObject(mapObj); } }else
				 * if(mapObj instanceof PolygonMapObject) { Polygon poly =
				 * ((PolygonMapObject)mapObj).getPolygon(); //TODO: need to write method that
				 * checks if a bouding polygon or bouding rect //hits a particular region
				 * 
				 * if( Utilities.isCollision( poly, region.rect)) {
				 * region.addCollisionObject(mapObj); }
				 * 
				 * }
				 * 
				 * }
				 * 
				 * regions[i][j] = region;
				 */
				
				
			}
			
		}
		
				
	}

	
	private void initNPCs() {
		
		/*
		 * For homeless NPCs, add them in the predefined locations on the map
		 */
		MapLayer layer  = this.tiledMap.getLayers().get(Constants.MAP_OBJ_NPC_LOCATION_LAYER);
		
		if( layer == null )		
			return;
		
		MapObjects mapObjects = layer.getObjects();
		
		
		/*
		 * Add them to the electorate list
		 */
		Iterator<String> itr = npcs.keySet().iterator();
		while(itr.hasNext()) {
			
			Electorate elector = npcs.get(itr.next()); 	
			elector.sprite.setSize(1, 1);
			
			electorate.add(elector);
			
			float xLoc = 0;
			float yLoc = 0;
			
				
			//NPC position should be random location if homeless, 
			//otherwise they should be around their house
			if( elector.houseId == Constants.HOMELESS || type==1){
				//get a random map object - a bounding box near a random house
				int randomIdx = ElectionGame.randGen.nextInt(mapObjects.getCount());		
				MapObject mapObject = mapObjects.get(randomIdx);
				
				//if its a 			
				if( mapObject.getName().equalsIgnoreCase(Constants.MAP_OBJ_NPC_LOCATION_LAYER)){
				
					RectangleMapObject obj = (RectangleMapObject) mapObject;
					Rectangle rect = obj.getRectangle();	
					
					xLoc = rect.x + (ElectionGame.randGen.nextFloat() *  rect.width);
					yLoc = rect.y + (ElectionGame.randGen.nextFloat() * rect.height);
					
					elector.sprite.setPosition(xLoc, yLoc);
				}

			}else{
				//put NPC near house
				MapObject mapObject = layer.getObjects().get(elector.houseId + "" );

				if( mapObject instanceof PolygonMapObject){
					
					PolygonMapObject polyobj = (PolygonMapObject) mapObject;
					Rectangle rect = polyobj.getPolygon().getBoundingRectangle();
					
					
					while( !polyobj.getPolygon().contains(xLoc, yLoc)) {
						xLoc = rect.x + (ElectionGame.randGen.nextFloat() *  rect.width);
						yLoc = rect.y + (ElectionGame.randGen.nextFloat() * rect.height);
					}
					
					
					elector.sprite.setPosition( xLoc, yLoc);
				}else if(mapObject instanceof RectangleMapObject) {
					RectangleMapObject rectObj = (RectangleMapObject) mapObject;
					Rectangle rect = rectObj.getRectangle();
					
					xLoc = rect.x + (ElectionGame.randGen.nextFloat() *  rect.width);
					yLoc = rect.y + (ElectionGame.randGen.nextFloat() * rect.height);
					
					elector.sprite.setPosition( xLoc, yLoc);
				}
			}
				
			
			
			//add elector to region where each corner is in				
			//bottom left corner region
			Region region1 = regions[(int)xLoc ][(int)yLoc];
			region1.addElectors(elector);
			
			try {
				//top right corner region
				Region region2 = regions[(int) (xLoc + elector.sprite.getWidth()) ][ (int) (yLoc + elector.sprite.getHeight() )];
				region2.addElectors(elector);
			} catch (Exception e) {}
			
			
			try{
			
				//bottom right corner region
				Region region4 = regions[(int) (xLoc + elector.sprite.getWidth()) ][ (int)yLoc ];
				region4.addElectors(elector);
			}catch(IndexOutOfBoundsException e){}
			
			try{
				//top left corner region
				Region region3 = regions[(int) (xLoc ) ][ (int) (yLoc + elector.sprite.getHeight()) ];
				region3.addElectors(elector);
			}catch(IndexOutOfBoundsException e){}
			
	
			
		}
		
		
	}

	
}
