package com.election.game.dialog.editor;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.election.game.Constants;
import com.election.game.Utilities;
import com.election.game.camera.OrthographicCameraMovementWrapper;
import com.election.game.dialog.DialogContainer;
import com.election.game.dialog.DialogModel;
import com.election.game.dialog.DialogTree;
import com.election.game.json.JsonParser;
import com.google.gson.Gson;

public class DialogGUIEditor extends Game implements Screen, InputProcessor {

	Gson gson;
	DialogContainer container;
	private Stage stage;
	private Window dialogDisplay;
	private Skin skin;
	public OrthographicCameraMovementWrapper worldCam;
	private Vector3 mousePos;
	private Table leftTable;
	private Table rightTable;	

	
	public DialogGUIEditor(){
		gson = new Gson();
		
		JsonParser parser = new JsonParser();
		
		

		String dialogTreesContent = Utilities.fileToString (	Paths.get("C:/Users/ayan_/Documents/Programming/libGDX/ElectionGame/android/assets/data/dialog", "dialog_trees.json")  );
		
		String dialogLinesContent  = Utilities.fileToString (	Paths.get("C:/Users/ayan_/Documents/Programming/libGDX/ElectionGame/android/assets/data/dialog", "dialog_lines.json")  );
		
			
		Map <String, DialogTree> dialogTree = parser.parseDialogTrees(dialogTreesContent);
		Map<String, DialogModel>  dialogModel = parser.parseDialogs(dialogLinesContent);
		container = new DialogContainer(dialogTree, dialogModel);

		

	}
	
	

	@Override
	public void create() {
		worldCam = new OrthographicCameraMovementWrapper();
		stage = new Stage();

		Gdx.graphics.setWindowedMode(Constants.WINDOWS_GAME_WIDTH, Constants.WINDOWS_GAME_HEIGHT);

		this.setScreen(this);
		createDialogDisplay();
		Gdx.input.setInputProcessor(stage);

	}
	
	
	private void createDialogDisplay() {
		skin = new Skin(Gdx.files.internal(Constants.UI_SKIN_PATH));
		
		//dialogDisplay = new Window("Dialog", skin);
		//dialogDisplay.setSize(Gdx.graphics.getWidth()/1.3f, Gdx.graphics.getHeight()/1.3f);
		//dialogDisplay.setPosition(0,0 );

		//stage.setDebugAll(true);
		
		//create a table to arrange display of dialogs
		//a first table on the left will show the IDs of the sprite
		//second table on the right will show the dialog trees that the main character
		//can say to this sprite
		Table parentTable = new Table();
		parentTable.setFillParent(true);
		//added to debug why buttons in a nested table are not receiving events
		leftTable = new Table();
		leftTable.debug();
		leftTable.center().left();		
		
		rightTable = new Table(); 
		rightTable.debug();
		rightTable.center().right();
		/*
		 * final ScrollPane scroll = new ScrollPane(leftTable, skin);
		 * scroll.setFadeScrollBars(false); scroll.setDebug(true);
		 * 
		 * leftTable.padTop(10);
		 */
		
		
		//fill left table with all the sprite ids that have dialog
		for (final String  candId: container.getDialogTrees().keySet()) {
			TextButton candDialogBtn = new TextButton("Sprite " + candId, skin);
			candDialogBtn.addListener(new ClickListener() {
			    @Override
			    public void clicked(InputEvent event, float x, float y) {
			    	System.out.println("GUI: clicked button for sprite " + candId);
			    	DialogTree dialogTrees = container.getDialogTrees().get(candId);
					List<Map<String, String>> inputList = dialogTrees.getInput();
					for (Map<String, String> map : inputList) {
						for (String key : map.keySet()) {
							Label lbl = new Label(key + ", " + map.get(key), skin);
							rightTable.add(lbl);
						}
					}
			    
			    };

			});
			leftTable.row();
			leftTable.add(candDialogBtn);
			
			
		}
		
		parentTable.add(leftTable);
		parentTable.add(rightTable);
		
		TextButton btn = new TextButton("TestButton",skin);
		btn.addListener(new ClickListener() {
			 @Override
			 public void clicked(InputEvent event, float x, float y) {
				 System.out.println("Test");
			 }
		});
		stage.addActor(btn);
		stage.addActor(parentTable);

		//dialogDisplay.add(parentTable);

	}
	

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		mousePos.x = screenX;
		mousePos.y = screenY;
		
		worldCam.source.unproject(mousePos);
		
		return false;
	}


	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
			
	}
	@Override
	public void resize(int width, int height) {
		worldCam.setToOrtho(false, Constants.VIRTUAL_HEIGHT *width/(float)height, Constants.VIRTUAL_HEIGHT);
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
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	
	
}
