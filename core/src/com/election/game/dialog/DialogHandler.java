package com.election.game.dialog;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.election.game.Constants;
import com.election.game.ElectionGame;
import com.election.game.Electorate;

public class DialogHandler implements InputProcessor {
	
	private static final float WIDTH = 10;
	public Sprite sprite;
	public Vector2 position;
	
	public Rectangle rect;
	public ShapeRenderer shapeRenderer;
	
	DialogContainer dialogObj;

	Electorate elector;
	private DialogTree dialogTree;
	
	int candidateDialogIdx = 0;
	BitmapFont font;
	
	public DialogHandler(DialogContainer dialogContainer){
		
		Texture texture = new Texture(Gdx.files.internal("dialog.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		sprite = new Sprite(texture);
		
		sprite.setOrigin(0, 0);
		sprite.setScale(0.3f);
		
		sprite.translateY(-20);
		sprite.translateX(-20);
		
		shapeRenderer = new ShapeRenderer();
		rect = new Rectangle(Gdx.graphics.getWidth()/6, Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 );
	
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		
		dialogObj = dialogContainer;
		
	}


	
	
	
	public void draw(SpriteBatch batch, float delta){
		
		shapeRenderer.begin(ShapeType.Line);
		
		shapeRenderer.setColor(1, 1, 0, 1);
		shapeRenderer.rect(rect.x-WIDTH, 0, rect.width + WIDTH, rect.height + WIDTH);
		shapeRenderer.end();
		
		
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(.3f, .5f, .6f, 1);
		shapeRenderer.rect(rect.x, WIDTH, rect.width , rect.height );
		shapeRenderer.end();

		
		
		List<Map<String, String>> list  = dialogTree.getInput();
		
		Map<String, String> dialogOptions = list.get(candidateDialogIdx);
		
		int yOffset = 0;
		
		Iterator<String> itr  = dialogOptions.keySet().iterator();
		while(itr.hasNext()){

			String key = itr.next();
				
			Dialog text = dialogObj.getDialogs().get(key);
			
			font.draw(batch, text.id + ": " + text.value, rect.x + WIDTH, rect.height - yOffset);	

			yOffset+=10;
			
		}
		
		
		
		
		//sprite.draw(batch);
	}





	public void startDialog(Electorate interactedElector) {
		
		
		elector = interactedElector;
		
		//dialogTree = dialogObj.getDialogTrees().get("" + interactedElector.id);
		dialogTree = dialogObj.getDialogTrees().get("" + 0);
		
		
		
	}

	public void endDialog(){
		
		
		dialogTree = null;
		Gdx.input.setInputProcessor((InputProcessor) ElectionGame.GAME_OBJ.getScreen());
		
	}




	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}





	@Override
	public boolean keyUp(int keycode) {

		switch (keycode) {
		
		case Keys.ESCAPE:
			this.endDialog();
			break;
		case Keys.ENTER:
			//TODO select dialog entry
				
			break;
	
		}
		
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
