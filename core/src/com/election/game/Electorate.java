package com.election.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Electorate {

	private static int ID_NUM = 0;

	public int houseId = Constants.HOMELESS;
	public String mapId;

	public BattleAttributes attributes;

	// List of terrain types the electorate can occupy
	public ArrayList<String> occupiableTiles;
 
	
	//public MapSprite sprite;
	public Sprite sprite;
	
	public Sprite debate_img;

	public boolean isDebater =false;
	public boolean isDebater() {
		return isDebater;
	}


	public void setDebater(boolean isDebater) {
		this.isDebater = isDebater;
	}


	public boolean hit = false;

	public int id;

	public Texture texture;
	
	
	public Electorate(){
	
	}
	
	
	public Electorate(Texture texture) {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//sprite = new MapSprite(texture);
		
		sprite = new Sprite(texture);
		attributes= new BattleAttributes();
		
		//this.id = 0;//for testing purposes all NPCs are 0 because I only have that dialog tree 
		//this.id = Electorate.getNewId();
	}



	private static int getNewId() {
		return ID_NUM++;
	}

	
	public void draw(Batch batch){
		
		sprite.draw(batch);
	}
	
	
	/*public void draw() {

		sprite.draw();
	}

*/	public void update(float delta) {

	}

}
