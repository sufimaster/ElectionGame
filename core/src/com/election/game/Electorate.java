package com.election.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Electorate {
	
	private static final int INFLUENCE_MAX = 10;

	/*
	 * TODO: ad some variables or a matrix for attributes relating to
	 * each person's voting reasons
	 */
	public int influenceLevel;
	
	//List of terrain types the electorate can occupy
	public ArrayList<String> occupiableTiles;
	
	
	
	public Sprite sprite;

	public Electorate(Texture texture) {
		sprite = new Sprite(texture);
		this.influenceLevel = ElectionGame.randGen.nextInt(INFLUENCE_MAX);
	}
	
	public Electorate(Texture texture, int influence) {
		sprite = new Sprite(texture);
		this.influenceLevel = influence;
	}

	
	public void draw(){
		
		sprite.draw(ElectionGame.GAME_OBJ.batch);
	}
	
	
	
	public void update(float delta){
	
	
	}
	
	
}
