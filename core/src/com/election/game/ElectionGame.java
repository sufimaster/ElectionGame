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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.election.game.States.GameState;
import com.election.game.dialog.DialogHandler2;
import com.election.game.dialog.DialogParser;

public class ElectionGame extends Game {
	
	public static Random randGen = new Random(System.currentTimeMillis());
	public static  ElectionGame  GAME_OBJ= null;
	
	public BitmapFont font;
	public SpriteBatch batch;
	public SpriteBatch hudBatch;
	public ShapeRenderer shapeRenderer;
	public DialogHandler2 dialogHandler;
	
	public boolean isdebug = true;
	public static boolean isFullScreen = false;
	
	
	
	
	public GameState state = GameState.READY;
	
	public BitmapFont dialogFont;
	public BitmapFont selectedDialogFont;
	public BitmapFont debugFont;	
	public BitmapFont  menuFont;
	public Skin dialogSkin;
	
	
	

	public void create () {
		GAME_OBJ = this;
		
		Gdx.graphics.setWindowedMode(Constants.WINDOWS_GAME_WIDTH, Constants.WINDOWS_GAME_HEIGHT);
		
		createBatches();
		createSkins();
		createFonts();
		createDialogObjects();
		createRenderers();
		
		this.setScreen(new MenuScreen(this));
		
	}
	
	

	
	

	private void createRenderers() {
		shapeRenderer = new ShapeRenderer();		
	}






	private void createBatches() {
		hudBatch = new SpriteBatch();
		batch = new SpriteBatch();		
	}


	private void createSkins() {
		dialogSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
	}


	public void createFonts(){
		
		font = new BitmapFont();

		
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

	private void createDialogObjects() {

		DialogParser dialogParser = new DialogParser();				
		dialogHandler = new DialogHandler2 (dialogParser.parseDialog(Constants.DIALOG_TREES_PATH, Constants.DIALOG_LINES_PATH), dialogFont, selectedDialogFont);
				
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

		if( state == GameState.PAUSED){
			
			state = GameState.RUNNING;
			
		}else {
			
			state = GameState.PAUSED;
			
		}
		
	}
	
}
