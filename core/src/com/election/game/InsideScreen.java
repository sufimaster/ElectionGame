package com.election.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.election.game.camera.OrthographicCameraMovementWrapper;
import com.election.game.sprites.Candidate;

public class InsideScreen implements Screen, InputProcessor {

	private OrthographicCameraMovementWrapper camera;
	private final ElectionGame gameObj;
	private Candidate candidate;

	//private TiledMap map;
	
	
	public InsideScreen(Candidate candidate) {
		// TODO Auto-generated constructor stub
		this.gameObj = ElectionGame.GAME_OBJ;
		this.candidate = candidate;
		candidate.sprite.setPosition( Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		
		
		
		camera = new OrthographicCameraMovementWrapper(false, 800, 480);
	}

	

	
	@Override
	public void show() {
		Gdx.app.log(getClass().getName(), "Setting input processor to: {" + this.getClass().getName() + "}");
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 

		camera.source.update();
				
		gameObj.batch.setProjectionMatrix(camera.source.combined);
			
		gameObj.batch.begin();
					
			candidate.draw(gameObj.batch);
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
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
