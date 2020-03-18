package com.election.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Electorate {

	private static int ID_NUM = 0;
	private static final int INFLUENCE_MAX = 10;

	public int houseId = Constants.HOMELESS;
	public String mapId;
	/*
	 * TODO: ad some variables or a matrix for attributes relating to each
	 * person's voting reasons
	 */
	public int influence;

	// List of terrain types the electorate can occupy
	public ArrayList<String> occupiableTiles;

	
	//public MapSprite sprite;
	public Sprite sprite;

	public boolean hit = false;

	public int id;

	public Texture texture;
	
	
	public Electorate(){
	
	}
	
	
	public Electorate(Texture texture) {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//sprite = new MapSprite(texture);
		
		sprite = new Sprite(texture);
		this.influence = ElectionGame.randGen.nextInt(INFLUENCE_MAX);
		//this.id = 0;//for testing purposes all NPCs are 0 because I only have that dialog tree 
		//this.id = Electorate.getNewId();
	}

	public Electorate(Texture texture, int influence) {
		//sprite = new MapSprite(texture);
		sprite = new Sprite(texture);
		this.influence = influence;
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
