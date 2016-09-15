package com.election.game.dialog;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.election.game.Constants;
import com.election.game.ElectionGame;
import com.election.game.ElectionGame.GameState;
import com.election.game.Electorate;

public class DialogHandler implements InputProcessor {
	
	private static final float WIDTH = 10;
	public Sprite sprite;
	public Vector2 position;
	
	public Rectangle rect;
	//public ShapeRenderer shapeRenderer;

	Electorate elector;

	DialogContainer dialogObj;
	DialogTree dialogTree;
	Map<String, String> currentDialogOptions;
	Dialog currentElectorDialog;
	
	BitmapFont font;
	BitmapFont selectedFont;

	
	private boolean dialogEnabled = false;
	
	
	int candidateDialogIdx = 0;
	int highlightedDialogOption=0;
	int selectedDialogOption= Constants.UNSELECTED;
	//private int dialogOptionCount;
	
	private boolean selectDialogTransition = false;
	private float transitionTime = 10f;
	
	float accum = 0f;
	private boolean endDialog = false;;
	
	public DialogHandler(DialogContainer dialogContainer, BitmapFont font, BitmapFont selectedFont){
		init();
		
		Texture texture = new Texture(Gdx.files.internal("dialog_box.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		sprite = new Sprite(texture);
		
		sprite.setOrigin(0, 0);
		sprite.setScale(0.3f);
		
		sprite.translateY(-20);
		sprite.translateX(-20);
		
		//shapeRenderer = new ShapeRenderer();
		rect = new Rectangle(Gdx.graphics.getWidth()/6, Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 );
	
		this.font = font;
		this.selectedFont = selectedFont;
		
		dialogObj = dialogContainer;
		
	}


	public void init(){
		
		candidateDialogIdx = 0;
		highlightedDialogOption=0;
		selectedDialogOption= Constants.UNSELECTED;
		
		selectDialogTransition = false;
		accum = 0f;
		endDialog = false;
		
		currentDialogOptions = null;
		currentElectorDialog = null;
		dialogTree = null;
	}
	
	
	public void draw(SpriteBatch batch, float delta){
		
		accum +=delta;
		
		if(!dialogEnabled){
			return;
		}
		
		
		/*TODO: 
		 * can't use shaperenderer inside of spritebatch begin/end, 
		 * because they both alter GL state. Figure out another way!
		 * 
		 * 
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		
		shapeRenderer.begin(ShapeType.Line);		
		shapeRenderer.setColor(1, 1, 0, 1);
		shapeRenderer.rect(rect.x-WIDTH, 0, rect.width + WIDTH, rect.height + WIDTH);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(.3f, .5f, .6f, 1);
		shapeRenderer.rect(rect.x, WIDTH, rect.width , rect.height );
		shapeRenderer.end();
		 */
		
		drawCandidateDialog(batch);
		
		if( currentElectorDialog !=null ){
		
			drawElectorateDialog(batch);
			
		}
		
		//but this means that the last elector response will be drawn, but not seen since the dialog is ended immediate. 
		//need to introduce some lag here.
		if( endDialog && accum >=transitionTime){
			endDialog();
			accum = 0;
		}
		
		
		
	}




	private void drawCandidateDialog(SpriteBatch batch) {
		
		//get the list of dialogs available for candidate to say
		List<Map<String, String>> list  = dialogTree.getInput();				

		//get the dialogs the candidate says to elector (depending on where the conversation is)
		currentDialogOptions = list.get(candidateDialogIdx);		
		
		sprite.draw(batch);
		
		
		//iterate through each of the dialog line options for the candidate and print them
		int yOffset = 0;
		int count=0;		
		Iterator<String> itr  = currentDialogOptions.keySet().iterator();
		while(itr.hasNext()){
			
			String key = itr.next();				
			Dialog text = dialogObj.getDialogs().get(key);
			
			if( count==highlightedDialogOption){
				selectedFont.draw(batch, text.id + ": " + text.value, rect.x + WIDTH, rect.height - 10 - yOffset);
			}else{			
				font.draw(batch, text.id + ": " + text.value, rect.x + WIDTH, rect.height - 10 - yOffset);
			}
			
			yOffset+=16;
			count++;
		}		
	}





	private void drawElectorateDialog(SpriteBatch batch) {

		Dialog text = dialogObj.getDialogs().get(currentElectorDialog.id);
		
		
		font.draw(batch, text.id + ": " + text.value, rect.width, rect.height/2);
		
		
		//selectedDialogOption = Constants.UNSELECTED;
	}



	
	
	

	private void resetDialog() {
		init();
	}

	
	

	public void startDialog(Electorate interactedElector) {
		
		Gdx.app.log(this.getClass().getName(), "Starting Dialog");

		resetDialog();
		
		
		dialogEnabled = true;
		elector = interactedElector;		
		//dialogTree = dialogObj.getDialogTrees().get("" + interactedElector.id);
		dialogTree = dialogObj.getDialogTrees().get("" + 0);
		
		
	}
	

	public void endDialog(){
		Gdx.app.log(this.getClass().getName(), "Ending Dialog");

		dialogEnabled = false;
		ElectionGame.GAME_OBJ.state = GameState.RUNNING;
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
		case Keys.UP:
		case Keys.W:
			selectNextOption(-1);		
			break;
		case Keys.DOWN:
		case Keys.S:
			selectNextOption(1);
			break;			
		case Keys.ENTER:
		case Keys.E:
			selectLine();
			break;
	
		}
		
		return false;
		
	}





	private void selectLine() {
		
		
		selectDialogTransition = true;

		//lock in the highlighted dialog line
		selectedDialogOption = highlightedDialogOption;
		
		//get the elector response KEY (like R1, R2 etc) mapped to the selected Candidate dialog line
		//if this is null, that means you display the final response, and end the dialog
		String electorDialogKey = currentDialogOptions.get("C" + selectedDialogOption);
		Gdx.app.log(this.getClass().getName(), "Elector Respose Dialog ID:" + electorDialogKey);
		
		
		//if the elector has nothing else to say, then end the dialog
		//else, get the candidate's array of responses available, and 
		if( electorDialogKey == null){
			endDialog = true;
		}else{
		
			//get the index for the candidate dialog tree associated with the elector response
			candidateDialogIdx = Integer.parseInt(dialogTree.output.get( electorDialogKey ) );
			
			//get the actual elector response object mapped to the key
			currentElectorDialog = dialogObj.getDialogs().get(electorDialogKey);
			
			//reset which dialog line is highlighted
			selectedDialogOption = 0;
		}
		
		
	}





	private void selectNextOption(int incrementSelection) {

		int dialogOptionCount = currentDialogOptions.size();

		if( highlightedDialogOption + incrementSelection>= dialogOptionCount ){
			//if you press down at last dialog option, loop back to beginning			
			highlightedDialogOption = 0;
			
		}else if( highlightedDialogOption + incrementSelection < 0){
			//if you press up at first dialog option, loop back to end
			highlightedDialogOption = dialogOptionCount-1;
			
		}else{
			
			//otherwise, just move down or up.
			highlightedDialogOption+= incrementSelection;
			
		}
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
