package com.election.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.election.game.Constants;
import com.election.game.ElectionGame;

public class Candidate {
	private boolean moveUp= false;
	private boolean moveDown= false;
	private boolean moveLeft= false;
	private boolean moveRight= false;
	
	
	private enum Direction{
		NONE,
		UP,
		DOWN,
		LEFT,
		RIGHT;
	}
	
	private Direction direction;
	
	
	
	public Sprite sprite;

	
	
	public Candidate(Texture texture) {
		sprite = new Sprite(texture);
	}

	
	public void draw(){
				
		sprite.draw(ElectionGame.GAME_OBJ.batch);
	}
	

	
	public void update(float delta){
		
		
		if( direction == Direction.UP && !(direction == Direction.LEFT || direction == Direction.RIGHT)){
			moveY(delta);
		}
		
		
		if( direction == Direction.DOWN && !(direction == Direction.LEFT || direction == Direction.RIGHT)){
			moveY(-delta);
		}
		
		if( direction == Direction.RIGHT && !(direction == Direction.UP || direction == Direction.DOWN)){
			moveX(delta);
		}
		
		if( direction == Direction.LEFT && !(direction == Direction.UP || direction == Direction.DOWN)){
			moveX(-delta);
		}
			
		
		/*
		if( moveUp && !(moveLeft || moveRight)){
			moveY(delta);
		}
		

		if( moveDown && !(moveLeft || moveRight)){
			moveY(-delta);
		}

		if( moveRight  && !(moveUp || moveDown)){
			
			moveX(delta);
		}
		
		
	
		if( moveLeft  && !(moveUp || moveDown)){
			
			moveX(-delta);
		}
		
		*/
	}

	public void moveY(float delta){
		sprite.translate(0, Constants.CHAR_YSPEED * delta);
	}
	
	public void moveX(float delta){
		sprite.translate(Constants.CHAR_XSPEED * delta, 0);
	}
	
	public void moveSprite(float delta){
		sprite.translate(Constants.CHAR_XSPEED  * delta, Constants.CHAR_YSPEED  * delta);
	}


	public void setMoveUp(boolean b) {
		if(b){
			direction = Direction.UP;	
		}else{
			direction = Direction.NONE;
		}
		
		
		if( moveDown && b) moveDown = false;
		
		moveUp = b;
		
	}

	public void setMoveDown(boolean b) {
		
		if(b){
			direction = Direction.DOWN;
		}else{
			direction = Direction.NONE;
		}
		
		

		
		
		if( moveUp && b) moveUp = false;
		moveDown = b;
		
		
	}

	public void setMoveRight(boolean b) {
		
		if(b){
			direction = Direction.RIGHT;
		}else{
			direction = Direction.NONE;
		}
		
		
		if( moveLeft && b) moveLeft = false;
		moveRight = b;
		
		
	}

	public void setMoveLeft(boolean b) {
		if(b){
			direction = Direction.LEFT;
		}else{
			direction = Direction.NONE;
		}
		
		if( moveRight && b) moveRight = false;
		moveLeft = b;
				
	}
	
	
}
