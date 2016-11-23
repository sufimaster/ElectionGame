package com.election.game;

import java.util.ArrayList;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TmxMapLoader.Parameters;

public class TownMap {
	
	public TiledMap tiledMap;
	
	int mapWidth;
	int mapHeight;
	int tilePixelWidth;
	int tilePixelHeight;

	int mapPixelWidth;

	int mapPixelHeight;
	
	
	//private List<MapObject> mapObjects;
	MapObjects mapObjs;

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
	
	public void addSprite(MapSprite sprite){
		
		tiledMap.getLayers().get(Constants.MAP_OBJ_OBJECT_LAYER).getObjects().add(sprite.mapObject);
		
		
	}
	
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
	
	public MapObjects getAllMapCollisionObjects(){
		
		MapObjects allLayerObjects = new MapObjects();
		MapLayers layers = tiledMap.getLayers();
		for (MapLayer mapLayer : layers) {
			
			MapObjects layerobjects= mapLayer.getObjects();
			for (MapObject mapObject : layerobjects) {
				
				String type = (String) mapObject.getProperties().get(Constants.MAP_OBJ_OBJECT_TYPE);
				
				if( type != null && type.toLowerCase().equals(Constants.MAP_OBJ_OBJECT_TYPE_COLLIDE.toLowerCase())){
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
	
	
	public void addSprites(ArrayList<Electorate> electorates){
		
		
		for (Electorate electorate : electorates) {
			addSprite( electorate.sprite);
		}
		
		
	}
	
	
}
