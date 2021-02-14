package com.election.game.sprites;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.election.game.BattleAttributes;
import com.election.game.Constants;

import com.election.game.States.WalkingDirection;


public class Candidate {
	
	//positive points from quest
	public float questPositiveReward=0;
	
	//negative points from quest
	public float questNegativeReward=0;
	
	public int argPower = Constants.DEFAULT_INITIAL_AP;
	
	private boolean moveUp= false;
	private boolean moveDown= false;
	private boolean moveLeft= false;
	private boolean moveRight= false;
	
	private Vector2 prevPosition;
	
	private WalkingDirection directionState = WalkingDirection.NONE;
	private WalkingDirection prevDirectionState = WalkingDirection.DOWN;
	
	public Sprite sprite;

	Animation <TextureRegion> idleAnimationLeft;
	Animation <TextureRegion> idleAnimationRight;
	Animation <TextureRegion> idleAnimationDown;
	Animation <TextureRegion> idleAnimationUp;
	
	Animation <TextureRegion> leftAnimations;
	Animation <TextureRegion> rightAnimations;
	Animation<TextureRegion> downAnimations;
	Animation<TextureRegion> upAnimations;

	
	float elapsedTime=0;

	private float width;

	private float height;


	public BattleAttributes attributes;	


	public Candidate(Texture texture) {
		
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);


		sprite = new Sprite(texture);
		prevPosition = new Vector2();
		attributes= new BattleAttributes();

		
		initDirectionAnimations(texture);
		
		
		
	}
	

	private void initDirectionAnimations(Texture texture) {
		
		//split the texture up into each individual frame
		//a row is one directional animation
		//each element is one image in an animation
		TextureRegion[][] tmpFrames = TextureRegion.split(texture, 16, 32);
		int index=0;
		
		//iterate through each row
		for(int i=0; i<tmpFrames.length; i++){
			
			//if its the first row, its the idle animation
			if(i==0) {
				//4 frames currently for each walking animation
				TextureRegion [] idleFrames = new TextureRegion[4];
				
				//iterate through each column (each image)
				//add them to an array
				for(int j=0; j<tmpFrames[i].length; j++){
					TextureRegion region =tmpFrames[i][j]; 
					
					idleFrames[index] = region;
					index++;
				}
				
				//create the idle animation. TODO: for now, just set Idle upAnimation to same as Idle downAnimation
				idleAnimationDown = new Animation<TextureRegion>(1f, idleFrames);			
				idleAnimationUp = idleAnimationDown;
			}
			
			//second row - down animation
			if(i==1) {
				index=0;

				TextureRegion [] downFrames = new TextureRegion[4];

				for(int j=0; j<tmpFrames[i].length; j++){
					TextureRegion region =tmpFrames[i][j]; 
					
					downFrames[index] = region;
					index++;
				}
				
				//create the animation for when the sprite walks downward. 
				//TODO: For now, set down animation to same as up Animation
				downAnimations = new Animation<TextureRegion>(1f, downFrames);
				upAnimations = downAnimations;
			}
			
			//row 3 - right animation/left animations
			if(i==2) {
				index=0;

				TextureRegion [] rightFrames = new TextureRegion[4];
				TextureRegion [] leftFrames = new TextureRegion[4];

				for(int j=0; j<tmpFrames[i].length; j++){
					TextureRegion region = tmpFrames[i][j]; 
					
					/*
					//if you create the right walking animation using the original texture, and then u flip the object, 
					//and create the left animation object using it, the right animation references the flipped object
					//so, to avoid this, I create a copy of the texture region, then flip it. And only use that flipped 
					//texture for the left animations 
					*/
					TextureRegion leftRegion = new TextureRegion(region);
					rightFrames[index] = region;
					
					leftRegion.flip(true, false);
					leftFrames[index] = leftRegion;
					index++;

				}
				
				rightAnimations = new Animation<TextureRegion>(1f, rightFrames);		
				leftAnimations = new Animation<TextureRegion>(1f, leftFrames);			
				
				idleAnimationRight = rightAnimations;
				idleAnimationLeft = leftAnimations;
				
				/*
				 * TODO: add rows for idle left/idle right/idle up animations
				 */

			
			}
			
			
		}
				
		
			
	}

	
	
	public Candidate(Texture texture, float width, float height) {

		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		sprite = new Sprite(texture);
		sprite.setSize(width, height);					
		prevPosition = new Vector2();

		this.width = width;
		this.height = height;
		
		initDirectionAnimations(texture);
		
		
	}

	
	public void draw(Batch batch){
			
		if( directionState == WalkingDirection.NONE){
		
			
			if( prevDirectionState == WalkingDirection.RIGHT){
				batch.draw( idleAnimationRight.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY(), width, height);
			}else if( prevDirectionState == WalkingDirection.LEFT){
				batch.draw( idleAnimationLeft.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY(), width, height);	
			}else if( prevDirectionState == WalkingDirection.DOWN){
				batch.draw( idleAnimationDown.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY(), width, height);
			}else if(prevDirectionState == WalkingDirection.UP){
				batch.draw( idleAnimationUp.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY(), width, height);
			}else {
				batch.draw( idleAnimationUp.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY(), width, height);
			}
		
		}else if(directionState == WalkingDirection.RIGHT){
			batch.draw( rightAnimations.getKeyFrame(elapsedTime,true), sprite.getX(), sprite.getY(), width, height);
		}else if(directionState == WalkingDirection.LEFT){
			batch.draw( leftAnimations.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY(), width, height);
		}else if(directionState == WalkingDirection.DOWN){
			batch.draw( downAnimations.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY(), width, height);
		}else if(directionState == WalkingDirection.UP){
			batch.draw( upAnimations.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY(), width, height);
		}
	}
	 

	
	public void update(float delta){
		
		prevPosition.set(sprite.getX(), sprite.getY());
		
		/* 
		 * if( directionState == WalkingDirection.UP && !(directionState ==
		 * WalkingDirection.LEFT || directionState == WalkingDirection.RIGHT)){
		 * moveY(delta); }
		 * 
		 * 
		 * if( directionState == WalkingDirection.DOWN && !(directionState ==
		 * WalkingDirection.LEFT || directionState == WalkingDirection.RIGHT)){
		 * moveY(-delta); }
		 * 
		 * if( directionState == WalkingDirection.RIGHT && !(directionState ==
		 * WalkingDirection.UP || directionState == WalkingDirection.DOWN)){
		 * moveX(delta); }
		 * 
		 * if( directionState == WalkingDirection.LEFT && !(directionState ==
		 * WalkingDirection.UP || directionState == WalkingDirection.DOWN)){
		 * moveX(-delta); }
		 */
		
		if( directionState == WalkingDirection.UP ){
			moveY(delta);
		}
		
		
		if( directionState == WalkingDirection.DOWN ){
			moveY(-delta);
		}
		
		if( directionState == WalkingDirection.RIGHT ){
			moveX(delta);
		}
		
		if( directionState == WalkingDirection.LEFT ){
			moveX(-delta);
		}

		elapsedTime += delta;
		//Gdx.app.log(this.getClass().getName(), "cand pos changed to: (" +this.getX() +" ,"+ this.getY() +")");
	
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


	public void setMoveUp(boolean movingUp) {
		
		prevDirectionState = directionState;
		
		if(movingUp){
			directionState = WalkingDirection.UP;	
		}else{
			/*TODO: these lines all cause problems.
			specifically, if you are pressing up, and then before letting go of UP, 
			and press another direction then the sprite stops moving. Solution? Thinking need to hold
			multiple direction movements at the same time, instead of just a state from one to the next.
			*/
			
			directionState = WalkingDirection.NONE;
		}
		
		//dont understand why I added this.	
		//if( moveDown && movingUp) moveDown = false;
		//moveUp = movingUp;
		
	}

	public void setMoveDown(boolean movingDown) {
		
		prevDirectionState = directionState;
		
		if(movingDown){
			directionState = WalkingDirection.DOWN;

		}else{
			directionState = WalkingDirection.NONE;
		}
		
		//dont understand why I added this.
		//if( moveUp && movingDown) moveUp = false;
		//moveDown = movingDown;
		
		
	}

	public void setMoveRight(boolean movingRight) {
		
		prevDirectionState = directionState;
		
		if(movingRight){
			directionState = WalkingDirection.RIGHT;
		}else{
			directionState = WalkingDirection.NONE;
		}
		
		//dont understand why I added this.
		//if( moveLeft && movingRight) moveLeft = false;
		//moveRight = movingRight;
		
		
	}

	public void setMoveLeft(boolean movingLeft) {
		
		prevDirectionState = directionState;
		
		if(movingLeft){
			directionState = WalkingDirection.LEFT;
		}else{
			directionState = WalkingDirection.NONE;
		}
		
		//dont understand why I added this.
		//if( moveRight && movingLeft) moveRight = false;
		//moveLeft = movingLeft;
				
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

	
	public void setSize(float width, float height){	
		
		this.width = width;
		this.height = height;
		
		sprite.setSize(width, height);
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
