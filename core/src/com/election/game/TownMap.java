package com.election.game;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

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

	public TownMap( String mapFile ){
		
		
		
		tiledMap = new TmxMapLoader().load(mapFile);
		
		MapProperties prop = tiledMap.getProperties();

		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tilePixelWidth = prop.get("tilewidth", Integer.class);
		tilePixelHeight = prop.get("tileheight", Integer.class);
	

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;
		
		mapObjs = tiledMap.getLayers().get(Constants.MAP_OBJ_PHYSICS_LAYER).getObjects();
		
	}
	
	
	
}
