package com.election.game.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.election.game.Constants;
import com.election.game.ElectionGame;
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
	private int timeToSnap = 2;
	
	
		
	public OrthographicCameraMovementWrapper(boolean b, float w, float h){
		
		this.source = new OrthographicCamera();
		this.source.setToOrtho(b, w, h);
		
		
		//w and h are the center location
		//w*2 and h*2
		
		cameraRect = new Rectangle(0, h*2, w, h);
		boundsRect = new Rectangle(0, h*2, w*Constants.CAM_MOVE_SCREEN_PERCENTAGE, h*Constants.CAM_MOVE_SCREEN_PERCENTAGE);
		boundsRect.setCenter(w/2,h/2);
		
		prevPosition = new Vector2(w/2, h/2);
		
	}
	
	
	public void resetCamera(){
		cameraRect.setCenter(prevPosition);
	}
	
	public void update(float delta, Candidate candidate){
		
		prevPosition.set(source.position.x, source.position.y);
		
		Vector2 camSpeed = getSpeed(delta, candidate);
		
		source.translate((int)camSpeed.x*delta, (int)camSpeed.y*delta);
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

	
	private Vector2 getSpeed(float delta, Candidate candidate){
		Vector2 camCenter = new Vector2(source.position.x, source.position.y);
				
		float xSpeed = ((Constants.CHAR_XSPEED * delta) + candidate.sprite.getBoundingRectangle().getX() - camCenter.x)/timeToSnap ;		
		float ySpeed = ((Constants.CHAR_YSPEED * delta) + candidate.sprite.getBoundingRectangle().getY() - camCenter.y)/timeToSnap;
						
		return new Vector2(xSpeed, ySpeed);
		
		
	}

}
