package com.election.game.json;

import java.lang.reflect.Type;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class TextureDeserializer  implements JsonDeserializer<Texture>{


	@Override
	public Texture deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		String texturePath = json.getAsString();

		Texture texture = new Texture(Gdx.files.internal(texturePath));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		
		return texture;
	}
	
	
	

}
