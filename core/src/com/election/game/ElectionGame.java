package com.election.game;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.election.game.dialog.DialogContainer;
import com.election.game.dialog.DialogHandler;
import com.election.game.dialog.DialogParser;

public class ElectionGame extends Game {
	
	public static  ElectionGame  GAME_OBJ= null;
	public BitmapFont font;
	public SpriteBatch batch;
	public SpriteBatch hudBatch;
	public boolean isdebug = true;
	public DialogHandler dialogHandler;
	
	public static boolean isFullScreen = false;
	
	public static Random randGen = new Random(System.currentTimeMillis());
	
	public enum GameState { READY, RUNNING,  PAUSED, GAMEOVER } 
	public GameState state = GameState.READY;
	private boolean isPaused = false;
	
	
	public void create () {
		
		
		
		Gdx.graphics.setWindowedMode(Constants.WINDOWS_GAME_WIDTH, Constants.WINDOWS_GAME_HEIGHT);
		
		hudBatch = new SpriteBatch();
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		DialogParser dialogParser = new DialogParser();		
		
		dialogHandler = new DialogHandler (dialogParser.parseDialog(Constants.DIALOG_TREES_PATH, Constants.DIALOG_LINES_PATH) );
		
		
		this.setScreen(new MenuScreen(this));
		
		GAME_OBJ = this;
	}

	
	public void render () {
		
		
		super.render();


	}
	
	

	public void dispose(){
		batch.dispose();
		font.dispose();
	}


	public static void toggleFullScreen() {

		isFullScreen = !isFullScreen;
		DisplayMode currentMode = Gdx.graphics.getDisplayMode();
		
		if( isFullScreen)
			Gdx.graphics.setFullscreenMode(currentMode);			
		else 	
			Gdx.graphics.setWindowedMode(Constants.WINDOWS_GAME_WIDTH, Constants.WINDOWS_GAME_HEIGHT);
		
	}


	public void togglePause() {

		isPaused  = !isPaused;
		
		if( isPaused){
			ElectionGame.GAME_OBJ.state = GameState.PAUSED;
		}else{
			ElectionGame.GAME_OBJ.state = GameState.RUNNING;
		}
		
		
		
	}
	
}
