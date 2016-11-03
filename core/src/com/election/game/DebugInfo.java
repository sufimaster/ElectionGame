package com.election.game;

import com.badlogic.gdx.Gdx;
import com.election.game.camera.OrthographicCameraMovementWrapper;
import com.election.game.sprites.Candidate;

public class DebugInfo {

	
	public void draw(Candidate candidate, OrthographicCameraMovementWrapper camera){
		
		
		
		if(!ElectionGame.GAME_OBJ.isdebug){
			return;
		}
		
		
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "FPS:" + Gdx.graphics.getFramesPerSecond(), 10, Constants.WINDOWS_GAME_HEIGHT - 60 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "STATE:" + ElectionGame.GAME_OBJ.state, 10, Constants.WINDOWS_GAME_HEIGHT - 76 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Candidate Pos: [" + candidate.sprite.getBoundingRectangle().getX() + ", " + candidate.sprite.getBoundingRectangle().getY() + "]", 10, Constants.WINDOWS_GAME_HEIGHT - 92 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Camera Position: [" + camera.source.position.x + ", " + camera.source.position.y + "]", 10, Constants.WINDOWS_GAME_HEIGHT - 108 );
		
		
	}
	
}
