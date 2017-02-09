package com.election.game.maps;

import java.util.Map;

public class MapHandler {

	public String currentMap;
	
	public Map<String, TownMap> gameMaps;
	
	public void init(){
		
		//set outside map to first map
		
		
		//initialize map size variables
		/*
		 * can't do this with JSON parser for some reason
		 * TODO: can't figure out how to call a method post JSON parsing
		 * 
		 */
		
		for (String key : gameMaps.keySet() ) {
			
			TownMap map = gameMaps.get(key);
			map.init();
			
		}
		
	}
	
	public TownMap getMap(String mapId){
		return gameMaps.get(mapId);
	}
}
