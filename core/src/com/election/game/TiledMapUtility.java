package com.election.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class TiledMapUtility {

	
	public static ArrayList<String> getTileTypes(TiledMap map, float x, float y){
		
		int tilePixelWidth = map.getProperties().get("tilewidth", Integer.class);
		int tilePixelHeight = map.getProperties().get("tileheight", Integer.class);
		
		ArrayList<String> tileList = new ArrayList<String>();
		
		
		
		for(MapLayer layer: map.getLayers()){
			if(layer.isVisible()){
				if( layer instanceof TiledMapTileLayer){
					
					
					
					//(X,Y) float coordinate is Pixels 
					//to get tile #, divide by tile width
					int xpos = (int) x/ tilePixelWidth;
					int ypos = (int) y/tilePixelHeight;
					
					//get cell
					Cell tileCell = ((TiledMapTileLayer) layer).getCell(xpos, ypos);
					
					if(tileCell != null){
						TiledMapTile tile =  tileCell.getTile();
						
						if(tile != null) {
							String type = (String) tile.getProperties().get("type");
							tileList.add(type);
						}
					}
					
				}
			}
		}
		
		
		return tileList;
		
	}

	public static boolean isElectorateSpace(TownMap townMap, int x, int y) {


		MapLayer layer  = townMap.tiledMap.getLayers().get(Constants.MAP_OBJ_NPC_LOCATION_LAYER);
		
		if( layer == null )		
			return false;
		
		MapObjects mapObjects = layer.getObjects();
		
		
		int randomIdx = ElectionGame.randGen.nextInt(mapObjects.getCount());		
		MapObject mapObject = mapObjects.get(randomIdx);
		//Gdx.app.log("DEBUG", "Electorate Space: " + randomIdx);
		
		//for (MapObject mapObject : mapObjects) {
		
		if( mapObject instanceof EllipseMapObject){
			
			EllipseMapObject ellipseObj = (EllipseMapObject) mapObject;
			return ellipseObj.getEllipse().contains(x, y);
			
		}
		
		//}
		
		
		return false;
		
		
		
	}
	
	
}
