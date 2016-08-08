package com.election.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.election.game.ElectionGame;

public class House {

	public Sprite sprite;
	private Vector2 position;

	public House(Texture texture, Vector2 position) {
		
		this.position = position;
		sprite = new Sprite(texture);
		sprite.setPosition(this.position.x, this.position.y);
	}
	
	
	public void draw(){
		
		sprite.draw(ElectionGame.GAME_OBJ.batch);
	}
	
	
	public void update(float delta){
		
	
	}

}
