package com.election.game.json;

import java.lang.reflect.Type;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TmxMapLoader.Parameters;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class BooleanDeserializer implements JsonDeserializer<TiledMap>{


	@Override
	public TiledMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		JsonArray tiledMapInfo = json.getAsJsonArray();
		String tileMapPath = tiledMapInfo.get(0).getAsString();
		boolean scaleToTile = tiledMapInfo.get(1).getAsBoolean();
		
		
		Parameters params = new Parameters();
		params.convertObjectToTileSpace = scaleToTile;
		
		TiledMap tiledMap = new TmxMapLoader().load(tileMapPath, params);
		
		
		
		
		return tiledMap;
	}
	
	
	

}
