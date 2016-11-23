package com.election.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MapSprite {

		private boolean moveUp= false;
		private boolean moveDown= false;
		private boolean moveLeft= false;
		private boolean moveRight= false;
		
		private Vector2 prevPosition;
		
		private Direction direction;
		//public Sprite sprite;

		public TextureMapObject mapObject;
		public TextureRegion textureRegion;
		public Rectangle bounds;
		
		
		private enum Direction{
			NONE,
			UP,
			DOWN,
			LEFT,
			RIGHT;
		}

		
		
		public MapSprite(Texture texture) {
			
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			textureRegion = new TextureRegion(texture);
			mapObject = new TextureMapObject(textureRegion);
			mapObject.setX(0);
			mapObject.setY(0);
			
			
			bounds = new Rectangle(mapObject.getX(), mapObject.getY(), texture.getWidth(), texture.getHeight());
			
			prevPosition = new Vector2(0,0);
		}

		
		public void draw(){
					
			//sprite.draw(ElectionGame.GAME_OBJ.batch);
		}
		

		
		public void update(float delta){
			
			prevPosition.set(mapObject.getX(), mapObject.getY());
			
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
			
			mapObject.setX(prevPosition.x);
			mapObject.setY(prevPosition.y);
			//sprite.setPosition(prevPosition.x, prevPosition.y);
		}
		

		public void moveY(float delta){
			
			mapObject.setY( mapObject.getY() + (int) Constants.CHAR_YSPEED * delta);
			bounds.setY(mapObject.getY());
			
			//sprite.translate(0, (int)Constants.CHAR_YSPEED * delta);
		}
		
		public void moveX(float delta){
			
			mapObject.setX( mapObject.getX() + (int) Constants.CHAR_XSPEED * delta);
			bounds.setX(mapObject.getX());
			
			//sprite.translate((int)Constants.CHAR_XSPEED * delta, 0);
		}
		
		public void moveSprite(float delta){
			moveX(delta);
			moveY(delta);
			//sprite.translate((int)Constants.CHAR_XSPEED  * delta, (int)Constants.CHAR_YSPEED  * delta);
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
		
		
		public void setPosition( float x, float y){
			
			mapObject.setX(x);
			mapObject.setY(y);
			bounds.setPosition(mapObject.getX(), mapObject.getY());

			
		}
		
		public float getX(){
			return mapObject.getX();
		}
		
		public float getY(){
			return mapObject.getY();
		}


		public boolean overlaps(Rectangle boundingRectangle) {
			
			if( bounds.overlaps(boundingRectangle))
				return true;
			
			return false;
		}
		
		public Rectangle getBoundingRectangle(){
			return bounds;
		}
	}
