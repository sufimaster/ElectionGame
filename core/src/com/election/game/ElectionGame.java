package com.election.game;

import java.util.Map;
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
import com.election.game.dialog.DialogHandler;
import com.election.game.json.JsonParser;
import com.election.game.maps.MapHandler;
import com.election.game.maps.TownMap;
import com.election.game.quests.QuestHandler;

public class ElectionGame extends Game {
	
	public static Random randGen = new Random(System.currentTimeMillis());
	public static  ElectionGame  GAME_OBJ= null;
	
	public BitmapFont font;
	public SpriteBatch batch;
	public SpriteBatch hudBatch;
	public ShapeRenderer shapeRenderer;
	public DialogHandler dialogHandler;
	
	public boolean isdebug = true;
	public static boolean isFullScreen = false;
	
	
	
	
	public GameState state = GameState.READY;
	
	public BitmapFont dialogFont;
	public BitmapFont selectedDialogFont;
	public BitmapFont debugFont;	
	public BitmapFont  menuFont;
	public Skin dialogSkin;
	
	public QuestHandler questHandler;
	public MapHandler mapHandler;
	

	public void create () {
		GAME_OBJ = this;
		
		Gdx.graphics.setWindowedMode(Constants.WINDOWS_GAME_WIDTH, Constants.WINDOWS_GAME_HEIGHT);
		
		createBatches();
		createSkins();
		createFonts();
		createQuests();
		createDialogObjects();
		createRenderers();
		loadMaps();
		//createNPCs();
		
		
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
		dialogSkin = new Skin(Gdx.files.internal(Constants.UI_SKIN_PATH));
		
	}


	public void createFonts(){
		
		font = new BitmapFont();

		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Constants.UI_FONT_PATH));
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

	private void loadMaps(){
		
		JsonParser jsonParser = new JsonParser();
		mapHandler = new MapHandler();
		mapHandler.gameMaps = jsonParser.parseMapsFile( Constants.MAPS_DEF_PATH);
		mapHandler.init();
	}
	
	private void createQuests(){
		
		questHandler = new QuestHandler();
		JsonParser jsonParser = new JsonParser();				

		questHandler.setQuests(  jsonParser.parseQuest( Constants.QUEST_DEF_PATH) );
	}
	
	private void createDialogObjects() {

		
		
		JsonParser jsonParser = new JsonParser();				
		dialogHandler = new DialogHandler (jsonParser.parseDialog(Constants.DIALOG_TREES_PATH, Constants.DIALOG_LINES_PATH), dialogFont, selectedDialogFont);
				
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
