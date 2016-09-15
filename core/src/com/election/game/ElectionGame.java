package com.election.game;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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
	
	public enum GameState { READY, RUNNING, DIALOG, PAUSED, GAMEOVER } 
	public GameState state = GameState.READY;
	private boolean isPaused = false;
	
	public BitmapFont dialogFont;
	public BitmapFont selectedDialogFont;
	public BitmapFont debugFont;
	
	public BitmapFont  menuFont;
	
	
	

	public void create () {
		
		
		
		Gdx.graphics.setWindowedMode(Constants.WINDOWS_GAME_WIDTH, Constants.WINDOWS_GAME_HEIGHT);
		
		hudBatch = new SpriteBatch();
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		createFonts();
		
		DialogParser dialogParser = new DialogParser();				
		dialogHandler = new DialogHandler (dialogParser.parseDialog(Constants.DIALOG_TREES_PATH, Constants.DIALOG_LINES_PATH), dialogFont, selectedDialogFont);
		
		
		
		
		this.setScreen(new MenuScreen(this));
		
		GAME_OBJ = this;
	}
	
	
	public void createFonts(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/arial.ttf"));
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		
		param.size=16;
		param.color= Color.BLACK;
		param.shadowColor = Color.GRAY;
		param.shadowOffsetX = 1;		
		dialogFont = generator.generateFont(param);
		
		param.borderWidth = 1f;
		param.borderColor = Color.RED;
		selectedDialogFont = generator.generateFont(param);
		
		
		param = new FreeTypeFontParameter();
		param.size=10;
		param.color= Color.RED;		
		debugFont = generator.generateFont(param);
		
		
		param = new FreeTypeFontParameter();
		param.size=30;
		param.color= Color.ORANGE;
		param.shadowColor = Color.GRAY;
		param.shadowOffsetX = 2;				
		param.shadowOffsetY = 2;
		menuFont = generator.generateFont(param);
		
		generator.dispose();
		
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
