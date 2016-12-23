package com.election.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.election.game.Constants;

public class Candidate {
	private boolean moveUp= false;
	private boolean moveDown= false;
	private boolean moveLeft= false;
	private boolean moveRight= false;
	
	private Vector2 prevPosition;
	
	private Direction direction;
	public Sprite sprite;

	
	private enum Direction{
		NONE,
		UP,
		DOWN,
		LEFT,
		RIGHT;
	}

	public Candidate(Texture texture) {

		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);


		sprite = new Sprite(texture);
		prevPosition = new Vector2();
	}
	
	public Candidate(Texture texture, int width, int height) {

		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);


		sprite = new Sprite(texture, width, height);
		prevPosition = new Vector2();
	}

	
	public void draw(Batch batch){
				
		sprite.draw(batch);
	}
	

	
	public void update(float delta){
		
		prevPosition.set(sprite.getX(), sprite.getY());
		
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

			
	}
	
	public void resetPosition(){
		
		sprite.setPosition(prevPosition.x, prevPosition.y);
	}
	

	public void moveY(float delta){
		sprite.translate(0, (int)Constants.CHAR_YSPEED * delta);
	}
	
	public void moveX(float delta){
		sprite.translate((int)Constants.CHAR_XSPEED * delta, 0);
	}
	
	public void moveSprite(float delta){
		sprite.translate((int)Constants.CHAR_XSPEED  * delta, (int)Constants.CHAR_YSPEED  * delta);
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
	
	public float getX(){
		return sprite.getX();
	}
	public float getY(){
		return sprite.getY();
	}


	public void setPosition(float f, float g) {

		sprite.setPosition(f, g);
/*		getBoundingRectangle().
*/
		
		sprite.setOrigin(f,g);

		
	}


	public boolean overlaps(Rectangle boundingRectangle) {
		// TODO Auto-generated method stub
		return boundingRectangle.overlaps(sprite.getBoundingRectangle());
	}


	public Rectangle getBoundingRectangle() {
		// TODO Auto-generated method stub
		return sprite.getBoundingRectangle();
	}


	
}
