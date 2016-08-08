package com.election.game.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.election.game.Constants;

public class OrthographicCameraMovementWrapper {
	
	public OrthographicCamera source;
	
	public Rectangle cameraRect;
	public Rectangle boundsRect;
	
	public boolean moveUp= false;
	public boolean moveDown= false;
	public boolean moveLeft= false;
	public boolean moveRight= false;
	
	
		
	public OrthographicCameraMovementWrapper(boolean b, float w, float h){
		
		this.source = new OrthographicCamera();
		this.source.setToOrtho(b, w, h);
		
		
		//w and h are the center location
		//w*2 and h*2
		
		cameraRect = new Rectangle(0, h*2, w, h);
		boundsRect = new Rectangle(0, h*2, w*Constants.CAM_MOVE_SCREEN_PERCENTAGE, h*Constants.CAM_MOVE_SCREEN_PERCENTAGE);
		boundsRect.setCenter(w/2,h/2);
		
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

	public void setMoveUp(boolean b) {
		if( moveDown && b) moveDown = false;
		
		moveUp = b;
		
	}

	public void setMoveDown(boolean b) {
		if( moveUp && b) moveUp = false;
		moveDown = b;
		
		
	}

	public void setMoveRight(boolean b) {
		if( moveLeft && b) moveLeft = false;
		moveRight = b;
		
		
	}

	public void setMoveLeft(boolean b) {
		if( moveRight && b) moveRight = false;
		moveLeft = b;
				
	}


	//the camera is movable if one or more of the move flags is set
	//otherwise, the camera has hit a boundary, or char isnt moving
	public boolean isMovable() {

		return moveUp || moveDown || moveLeft || moveRight;
	}
}
