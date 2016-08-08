package com.election.game;

import java.util.List;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.election.game.sprites.Candidate;

public class TownMap {
	
	public TiledMap map;
	
	int mapWidth;
	int mapHeight;
	int tilePixelWidth;
	int tilePixelHeight;

	int mapPixelWidth;

	int mapPixelHeight;
	
	
	//private List<MapObject> mapObjects;
	MapObjects mapObjs;

	public TownMap( String mapFile ){
		
		
		
		map = new TmxMapLoader().load(mapFile);
		
		MapProperties prop = map.getProperties();

		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tilePixelWidth = prop.get("tilewidth", Integer.class);
		tilePixelHeight = prop.get("tileheight", Integer.class);
	

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;
		
		mapObjs = map.getLayers().get("physics").getObjects();
		
	}
	
	
	
}
