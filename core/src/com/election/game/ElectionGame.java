package com.election.game;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ElectionGame extends Game {
	
	public static  ElectionGame  GAME_OBJ= null;
	//private OrthographicCamera camera;
	public BitmapFont font;
	public SpriteBatch batch;
	public boolean isdebug = true;
	
	public static boolean isFullScreen = false;
	
	public static Random randGen = new Random(System.currentTimeMillis());


	
	public void create () {
		
		
		
		Gdx.graphics.setWindowedMode(Constants.WINDOWS_GAME_WIDTH, Constants.WINDOWS_GAME_HEIGHT);
		
		batch = new SpriteBatch();
		font = new BitmapFont();
		
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
	
}
