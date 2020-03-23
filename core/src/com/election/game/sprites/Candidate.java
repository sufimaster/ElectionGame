package com.election.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.election.game.Constants;

import com.election.game.States.WalkingDirection;


public class Candidate {
	
	//positive points from quest
	public float questPositiveReward=0;
	
	//negative points from quest
	public float questNegativeReward=0;
	
	private boolean moveUp= false;
	private boolean moveDown= false;
	private boolean moveLeft= false;
	private boolean moveRight= false;
	
	private Vector2 prevPosition;
	
	private WalkingDirection directionState = WalkingDirection.NONE;
	private WalkingDirection prevDirectionState = WalkingDirection.RIGHT;
	
	public Sprite sprite;

	//public TextureRegion[] animationFramesLeft;
	Animation <TextureRegion> idleAnimationLeft;
	
	//public TextureRegion[] animationFramesRight;
	Animation <TextureRegion> idleAnimationRight;
	
	
	float elapsedTime=0;

	private float width;

	private float height;

	//private AnimationState state = AnimationState.IDLE;
	


	public Candidate(Texture texture) {
		
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);


		sprite = new Sprite(texture);
		prevPosition = new Vector2();
		
		initAnimations(texture);
		
		
		
	}
	
	private void initAnimations(Texture texture) {
		//idle animation
		
		initLeftAnimations(texture);
		
		initRightAnimations(texture);
		
		initUpAnimations(texture);	
		
		initDownAnimations(texture);	

	}



	private void initRightAnimations(Texture texture) {
		TextureRegion[][] tmpFrames = TextureRegion.split(texture, 128, 256);
		TextureRegion [] animationFramesRight = new TextureRegion[8];
		int index=0;
		
		for(int i=0; i<tmpFrames.length; i++){
			for(int j=0; j<tmpFrames[i].length; j++){
				TextureRegion region =tmpFrames[i][j]; 
				region.flip(true, false);
				
				animationFramesRight[index] = region;
				animationFramesRight[animationFramesRight.length-1-index] = tmpFrames[i][j];
				index++;
			}
		}
				
		
		idleAnimationRight = new Animation<TextureRegion>(1f/4f, animationFramesRight);			
	}
	
	private void initLeftAnimations(Texture texture) {

		TextureRegion[][] tmpFrames = TextureRegion.split(texture, 128, 256);
		TextureRegion [] animationFramesLeft = new TextureRegion[8];
		int index=0;
		
		for(int i=0; i<tmpFrames.length; i++){
			for(int j=0; j<tmpFrames[i].length; j++){
				
				
				animationFramesLeft[index] = tmpFrames[i][j];
				animationFramesLeft[animationFramesLeft.length-1-index] = tmpFrames[i][j];
				index++;
			}
		}
				
		
		idleAnimationLeft = new Animation<TextureRegion>(1f/4f, animationFramesLeft);		
		

	}
	

	private void initDownAnimations(Texture texture) {

	}

	private void initUpAnimations(Texture texture) {
		// TODO Auto-generated method stub
		
	}	
	
	
	public Candidate(Texture texture, float width, float height) {

		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		sprite = new Sprite(texture);
		sprite.setSize(width, height);					
		prevPosition = new Vector2();

		this.width = width;
		this.height = height;
		
		initAnimations(texture);
		
		
	}

	
	public void draw(Batch batch){
			
		if( directionState == WalkingDirection.NONE){
		
			
			if( prevDirectionState == WalkingDirection.RIGHT){
				batch.draw( idleAnimationRight.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY(), width, height);
			}else if( prevDirectionState == WalkingDirection.LEFT){
				batch.draw( idleAnimationLeft.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY(), width, height);	
			}else if( prevDirectionState == WalkingDirection.DOWN){
				batch.draw( idleAnimationRight.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY(), width, height);
			}else if(prevDirectionState == WalkingDirection.UP){
				batch.draw( idleAnimationLeft.getKeyFrame(elapsedTime, true), sprite.getX(), sprite.getY(), width, height);
			}
		
		}else if(directionState == WalkingDirection.RIGHT){
			batch.draw( idleAnimationRight.getKeyFrame(0f), sprite.getX(), sprite.getY(), width, height);
		}else if(directionState == WalkingDirection.LEFT){
			batch.draw( idleAnimationLeft.getKeyFrame(0f), sprite.getX(), sprite.getY(), width, height);
		}else if(directionState == WalkingDirection.DOWN){
			batch.draw( idleAnimationRight.getKeyFrame(0f), sprite.getX(), sprite.getY(), width, height);
		}else if(directionState == WalkingDirection.UP){
			batch.draw( idleAnimationLeft.getKeyFrame(0f), sprite.getX(), sprite.getY(), width, height);
		}
	}
	 

	
	public void update(float delta){
		
		prevPosition.set(sprite.getX(), sprite.getY());
		
		if( directionState == WalkingDirection.UP && !(directionState == WalkingDirection.LEFT || directionState == WalkingDirection.RIGHT)){
			moveY(delta);
		}
		
		
		if( directionState == WalkingDirection.DOWN && !(directionState == WalkingDirection.LEFT || directionState == WalkingDirection.RIGHT)){
			moveY(-delta);
		}
		
		if( directionState == WalkingDirection.RIGHT && !(directionState == WalkingDirection.UP || directionState == WalkingDirection.DOWN)){
			moveX(delta);
		}
		
		if( directionState == WalkingDirection.LEFT && !(directionState == WalkingDirection.UP || directionState == WalkingDirection.DOWN)){
			moveX(-delta);
		}

		elapsedTime += delta;
			
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
		
		prevDirectionState = directionState;
		
		if(b){
			directionState = WalkingDirection.UP;	
		}else{
			directionState = WalkingDirection.NONE;
		}
		
		
		if( moveDown && b) moveDown = false;
		
		moveUp = b;
		
	}

	public void setMoveDown(boolean b) {
		
		prevDirectionState = directionState;
		
		if(b){
			directionState = WalkingDirection.DOWN;

		}else{
			directionState = WalkingDirection.NONE;
		}
		
		

		
		
		if( moveUp && b) moveUp = false;
		moveDown = b;
		
		
	}

	public void setMoveRight(boolean b) {
		
		prevDirectionState = directionState;
		
		if(b){
			directionState = WalkingDirection.RIGHT;
		}else{
			directionState = WalkingDirection.NONE;
		}
		
		
		if( moveLeft && b) moveLeft = false;
		moveRight = b;
		
		
	}

	public void setMoveLeft(boolean b) {
		
		prevDirectionState = directionState;
		
		if(b){
			directionState = WalkingDirection.LEFT;
		}else{
			directionState = WalkingDirection.NONE;
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
