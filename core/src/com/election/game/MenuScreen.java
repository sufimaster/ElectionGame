package com.election.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MenuScreen implements Screen, InputProcessor {

	
	final ElectionGame gameObj;
	OrthographicCamera camera;
	

	public MenuScreen( final ElectionGame gameObj){
		Gdx.app.log(getClass().getName(), "Creating Menu Screen");
		this.gameObj = gameObj;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1024, 800);		
	} 
	
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {

			
		update();
		//processInput();
		
	}
	
	
	/*
	 * Add in intro screen stating "Welcome to the city of Azad Pashmir".  This is a free city found south of AkhirJahan and west of Sumeria.
	 * The mayor of the town, Lazlo Hariki, recently fled to AkhirJahan after being exposed as stealing from the local pension fund.  THe city needs
	 * a new mayor, and you've decided to throw in your hat. Speak with the people of Azad Pashmir and convince them you are the right man for the job!  
	 * 
	 * Also, display a map showing location of Azad Pashmir in relation to other places
	 */
	
	
	public void update(){
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		
		gameObj.batch.setProjectionMatrix(camera.combined);
		gameObj.batch.begin();
			
			gameObj.font.setColor(Color.CORAL);
			gameObj.font.draw(gameObj.batch, "Welcome to " + Constants.GAME_TITLE, 200, 300);
			gameObj.font.draw(gameObj.batch, "Press any key to get started", 200, 250);
			
			
		gameObj.batch.end();
		
		
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}


	@Override
	public boolean keyDown(int keycode) {


		
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		
		gameObj.setScreen(new OutsideScreen(gameObj));
		dispose();
	
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
