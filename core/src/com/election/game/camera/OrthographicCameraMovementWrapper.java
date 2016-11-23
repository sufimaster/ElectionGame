package com.election.game.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.election.game.Constants;
import com.election.game.ElectionGame;
import com.election.game.MapSprite;
import com.election.game.sprites.Candidate;

public class OrthographicCameraMovementWrapper {
	
	public OrthographicCamera source;
	
	public Rectangle cameraRect;
	public Rectangle boundsRect;
	
	public boolean moveUp= false;
	public boolean moveDown= false;
	public boolean moveLeft= false;
	public boolean moveRight= false;

	private Vector2 prevPosition;
	
	//length of time it takes camera to snap to player position in seconds
	private int timeToSnap = 1;
	
	public OrthographicCameraMovementWrapper(){
		this.source = new OrthographicCamera();
	}
		
	public OrthographicCameraMovementWrapper(boolean yDown, float viewportWidth, float viewportHeight){
		
		this.source = new OrthographicCamera();
		setToOrtho(yDown,viewportWidth, viewportHeight);
		
		
		
		
		//w and h are the center location
		//w*2 and h*2
		
		cameraRect = new Rectangle(0, viewportHeight*2, viewportWidth, viewportHeight);
		boundsRect = new Rectangle(0, viewportHeight*2, viewportWidth*Constants.CAM_MOVE_SCREEN_PERCENTAGE, viewportHeight*Constants.CAM_MOVE_SCREEN_PERCENTAGE);
		boundsRect.setCenter(viewportWidth/2,viewportHeight/2);
		
		prevPosition = new Vector2(viewportWidth/2, viewportHeight/2);
		
	}
	
	
	public void resetCamera(){
		cameraRect.setCenter(prevPosition);
	}
	
	public void update(float delta, MapSprite candidate){
		
		prevPosition.set(source.position.x, source.position.y);
		
		Vector2 camSpeed = getSpeed(delta, candidate);
		
		//TODO: if this line is used instead of the uncommented line, the camera follows candidate closer, but introduces jitter in the screen/characters
		//source.translate((int)camSpeed.x*delta, (int)camSpeed.y*delta);
		
		//source.translate((camSpeed.x*delta),(camSpeed.y*delta));
		syncRects();
	}
	
	public void update(float delta, Candidate candidate){
		
		prevPosition.set(source.position.x, source.position.y);
		
		Vector2 camSpeed = getSpeed(delta, candidate);
		
		//TODO: if this line is used instead of the uncommented line, the camera follows candidate closer, but introduces jitter in the screen/characters
		source.translate((int)camSpeed.x*delta, (int)camSpeed.y*delta);
		
		//source.translate((camSpeed.x*delta),(camSpeed.y*delta));
		syncRects();
	}
	
	
	public void update(float delta){
		
		if( moveUp ){
			moveY(delta );
		}else if( moveDown){
			
			moveY(-delta );
		}else if( moveLeft){
			
			moveX(-delta );
		}else if( moveRight){
			
			moveX(delta );
		}
		
		
	}
	
	public void moveY(float delta){
		
		source.translate(0, Constants.CAM_YSPEED *  delta);
		syncRects();
	}
	
	public void moveX(float delta){
		
		/*if( moveLeft){
			source.translate(Constants.CAM_XSPEED *1.8f *  delta, 0);
		}else if(moveRight){
			source.translate(-Constants.CAM_XSPEED *1.8f *  delta, 0);
		}else{		*/	
			source.translate(Constants.CAM_XSPEED * delta, 0);
			syncRects();
		//}
	}
	
	public void syncRects(){
		
		cameraRect.setCenter(source.position.x, source.position.y);	
		boundsRect.setCenter(source.position.x, source.position.y);
		
	}

	
	private Vector2 getSpeed(float delta, MapSprite candidate){
		Vector2 camCenter = new Vector2(source.position.x, source.position.y);
				
		float xSpeed = ((Constants.CHAR_XSPEED * delta) + candidate.getBoundingRectangle().getX() - camCenter.x)/timeToSnap ;		
		float ySpeed = ((Constants.CHAR_YSPEED * delta) + candidate.getBoundingRectangle().getY() - camCenter.y)/timeToSnap;
						
		return new Vector2(xSpeed, ySpeed);
		
		
	}
	
	private Vector2 getSpeed(float delta, Candidate candidate){
		Vector2 camCenter = new Vector2(source.position.x, source.position.y);
				
		
		Rectangle boundingRectangle = candidate.getBoundingRectangle();
		
		/*float xSpeed = ((Constants.CHAR_XSPEED * delta) + boundingRectangle.getX() - camCenter.x)/timeToSnap ;		
		float ySpeed = ((Constants.CHAR_YSPEED * delta) + boundingRectangle.getY() - camCenter.y)/timeToSnap;*/
						
		float xSpeed = (  boundingRectangle.getX() - camCenter.x)/timeToSnap ;		
		float ySpeed = ( boundingRectangle.getY() - camCenter.y)/timeToSnap;
						
		
		return new Vector2(xSpeed, ySpeed);
		
		
	}
	
	
	public void setToOrtho(boolean yDown, float viewportWidth, float viewportHeight){
		source.setToOrtho(yDown, viewportWidth, viewportHeight);
		
		cameraRect = new Rectangle(0, viewportHeight*2, viewportWidth, viewportHeight);
		boundsRect = new Rectangle(0, viewportHeight*2, viewportWidth*Constants.CAM_MOVE_SCREEN_PERCENTAGE, viewportHeight*Constants.CAM_MOVE_SCREEN_PERCENTAGE);
		boundsRect.setCenter(viewportWidth/2,viewportHeight/2);
		
		prevPosition = new Vector2(viewportWidth/2, viewportHeight/2);
		
	}
	

}
