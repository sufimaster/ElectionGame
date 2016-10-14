package com.election.game.dialog;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.election.game.Constants;
import com.election.game.ElectionGame;
import com.election.game.Electorate;
import com.election.game.States.*;


public class DialogHandler implements InputProcessor {
	
	private static final float BUFFER_WIDTH = 20;
	public Sprite dialogBox;
	public Sprite choiceBox;
	public Vector2 position;
	
	public Rectangle rect;
	//public ShapeRenderer shapeRenderer;

	Electorate elector;

	DialogContainer dialogObj;
	DialogTree candidateDialogTree;
	Map<String, String> currentDialogOptions;
	//Dialog currentElectorDialog;
	DialogModel displayingDialog;
	
	BitmapFont font;
	BitmapFont selectedFont;

	
	private boolean dialogEnabled = false;
	
	
	int candidateDialogIdx = 0;
	int highlightedDialogOption=0;
	int selectedDialogOption= Constants.UNSELECTED;
	//private int dialogOptionCount;
	

	
	float accum = 0f;
	//private boolean endDialog = false;;
	

	
	public DialogState dialogState=DialogState.DISPLAYING;
	
	public Animation prompt;
	
	public GlyphLayout layout1;
	public GlyphLayout layout2;
	
	public DialogHandler(DialogContainer dialogContainer, BitmapFont font, BitmapFont selectedFont){
		init();
		
		Texture texture = new Texture(Gdx.files.internal("dialog_box2.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		dialogBox = new Sprite(texture);		
		dialogBox.setSize(dialogBox.getWidth()*0.2f, dialogBox.getHeight()* 0.2f);
		dialogBox.setOriginCenter();
		
		
		layout1 = new GlyphLayout(); 
		layout1.width = dialogBox.getWidth() - BUFFER_WIDTH;
		
		
		choiceBox = new Sprite(texture);
		choiceBox.setSize(choiceBox.getWidth()*0.2f, choiceBox.getHeight()* 0.1f);
		choiceBox.setOriginCenter();
		
		layout2= new GlyphLayout(); 
		layout2.width = choiceBox.getWidth() - BUFFER_WIDTH;
		
		
		
		//shapeRenderer = new ShapeRenderer();
		rect = new Rectangle(Gdx.graphics.getWidth()/6, Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 );
	
		this.font = font;
		this.selectedFont = selectedFont;
		
		dialogObj = dialogContainer;
		
		//prompt = new Animation(10, );
		
	}


	public void init(){
		
		candidateDialogIdx = 0;
		highlightedDialogOption=0;
		selectedDialogOption= Constants.UNSELECTED;
		
		accum = 0f;
		
		elector = null;
		displayingDialog = null;
		currentDialogOptions = null;
		candidateDialogTree = null;
	}
	
	
	public void draw(SpriteBatch batch, float delta){
		
		//Gdx.app.log(this.getClass().getName(), "Dialog State:" + dialogState);
		
		if(!dialogEnabled){
			return;
		}
		
		
		if( dialogState == DialogState.DISPLAYING){
			
			if( displayingDialog != null){
				drawDialog(batch);
			}
			
		}else if( dialogState == DialogState.WAITING_TO_DISMISS){
			
			drawDialog(batch);
			drawPrompt( batch);
			
		}else if( dialogState == DialogState.MAKING_SELECTION){
		
			drawDialog(batch);
			drawCandidateOptions(batch);
						
		}else if( dialogState == DialogState.ENDING){
			
			endDialog();
		}
		
		
		
	}




	private void drawPrompt(SpriteBatch batch) {

		
		//Gdx.app.log(this.getClass().getName(), "Press Enter to proceed");
		selectedFont.draw(batch, "Press enter to continue", dialogBox.getX()+ dialogBox.getWidth()/2 - BUFFER_WIDTH, dialogBox.getY()/2 - BUFFER_WIDTH);
		
	}


	private void drawDialog(SpriteBatch batch) {
		
		if( displayingDialog !=null){
			
			layout1.setText(font, displayingDialog.id + ": " + displayingDialog.value, Color.RED, dialogBox.getWidth() , Align.topLeft, true );
			//float width = layout.width;
			//float height = layout.height;
			
			
			dialogBox.draw(batch);
			font.draw(batch, layout1, dialogBox.getX()+ BUFFER_WIDTH, dialogBox.getY() + dialogBox.getHeight()/1.6f - BUFFER_WIDTH);
		}
		
		if( candidateDialogIdx == Constants.NO_MORE_DIALOG){
			dialogState = DialogState.WAITING_TO_DISMISS;
		}	else{
			dialogState = DialogState.MAKING_SELECTION;
		}
		
		
		
		
	}

	private void drawCandidateOptions(SpriteBatch batch) {
		
		//get the list of dialogs available for candidate to say
		List<Map<String, String>> list  = candidateDialogTree.getInput();				

		//get the dialogs the candidate says to elector 
		//if the first time, candidateDialogIdx shold be 0
		//if its later in the conversation, candidateIdx should be the index for the particular
		//tree that is in response to the elector's response
		currentDialogOptions = list.get(candidateDialogIdx);		
		
		choiceBox.draw(batch);
		
		
		//iterate through each of the dialog line options for the candidate and print them
		int yOffset = 0;
		int count=0;		
		Iterator<String> itr  = currentDialogOptions.keySet().iterator();
		while(itr.hasNext()){
			
			String key = itr.next();				
			DialogModel text = dialogObj.getDialogs().get(key);
			
			if( count==highlightedDialogOption){
				layout2.setText(selectedFont,  text.id + ": " + text.value, Color.RED, choiceBox.getWidth(), Align.topLeft, true );
				
				selectedFont.draw(batch, layout2, 10, choiceBox.getY() + choiceBox.getHeight()/1.7f - yOffset);
			}else{			
				
				layout2.setText(font,  text.id + ": " + text.value, Color.RED, choiceBox.getWidth(), Align.topLeft, true );
				font.draw(batch, layout2, 10, choiceBox.getY() + choiceBox.getHeight()/1.7f- yOffset);
			}
			
			yOffset+=layout2.height + 3;
			count++;
		}		
	}



	private void resetDialog() {
		init();
	}

	
	

	public void startDialog(Electorate interactedElector) {
		
		Gdx.app.log(this.getClass().getName(), "Starting Dialog with "+ interactedElector.id);

		resetDialog();
		
		
		dialogEnabled = true;
		elector = interactedElector;		
		//dialogTree = dialogObj.getDialogTrees().get("" + interactedElector.id);
		
		
		candidateDialogTree = dialogObj.getDialogTrees().get("" + interactedElector.id);
		
		if( candidateDialogTree == null){
			endDialog();
			return;
		}
		
		if( candidateDialogTree.input.size() > 0){
			dialogState = DialogState.MAKING_SELECTION;
		}
		
		
		
	}
	

	public void endDialog(){
		Gdx.app.log(this.getClass().getName(), "Ending Dialog");
		dialogEnabled = false;
		resetDialog();

		
		ElectionGame.GAME_OBJ.state = GameState.RUNNING;
		Gdx.input.setInputProcessor((InputProcessor) ElectionGame.GAME_OBJ.getScreen());
		
	}




	

	
	private void continueDialog() {
		// TODO Auto-generated method stub


		if( candidateDialogIdx == Constants.NO_MORE_DIALOG ){
			dialogState= DialogState.ENDING;
		}else{
			
			String idx = candidateDialogTree.getOutput().get(displayingDialog.id);

			if( idx != null && !idx.isEmpty()){
				candidateDialogIdx = Integer.parseInt(idx);
				dialogState = DialogState.MAKING_SELECTION;
			}else{
				candidateDialogIdx = Constants.NO_MORE_DIALOG;
				dialogState = DialogState.WAITING_TO_DISMISS;
			}
	
			
		}
	}


	/*
	 * Once a player selects the dialog they want their candidate to say, 
	 * it will call this method, lock in that dialog, and choose the elector's response
	 * 
	 */
	private void selectLine() {
		
		
		if( dialogState != DialogState.MAKING_SELECTION){
			return;
		}
		

		//lock in the highlighted dialog line
		selectedDialogOption = highlightedDialogOption;
		
		
		/*
		 * selectedDialogOption will be from 0 to # of dialog options
		but the ids for the dialog options could start at 0, or 1 or 2 etc - e.g., "C3", "C4", "C5" could be options
		so we need to find which id matches up with the selectedDialogOptions
		*
		*/		
		int count =0;
		String selectedCandidateKey = null;		
		Set<String> keys = currentDialogOptions.keySet();
		
		Iterator<String> itr = keys.iterator();		
		while (itr.hasNext()) {		
			selectedCandidateKey= (String) itr.next();	

			if( count == selectedDialogOption ){
				break;
			}else{			
				count++;
			}
		}
		
		//get the elector response KEY (like R1, R2 etc) mapped to the selected Candidate dialog line C1, C2, etc...
		//if this is null, that means you display the final response, and end the dialog
		String electorDialogKey = currentDialogOptions.get(selectedCandidateKey);
		Gdx.app.log(this.getClass().getName(), "Candidate key: " + selectedCandidateKey + ", NPC key:" + electorDialogKey);
		
		
		//if the elector has nothing else to say, then end the dialog
		//else, get the candidate's array of responses available 
		if( electorDialogKey != null && !electorDialogKey.isEmpty()){
		
			//get the index for the candidate dialog tree associated with the elector response
			String temp = candidateDialogTree.output.get( electorDialogKey );
			
			if( temp != null && !temp.isEmpty()){
				candidateDialogIdx = Integer.parseInt( temp );	
			}else{
				candidateDialogIdx = Constants.NO_MORE_DIALOG;
			}
			
			//get the actual elector response object mapped to the key
			//currentElectorDialog = dialogObj.getDialogs().get(electorDialogKey);
			displayingDialog = dialogObj.getDialogs().get(electorDialogKey);
			dialogState = DialogState.DISPLAYING;	
			
			//reset which dialog line is highlighted
			selectedDialogOption = 0;
		}
		
		
		dialogState = DialogState.DISPLAYING;

		
	}




	/*
	 * highlight option when user presses up/down key
	 */
	private void highlightNextOption(int incrementSelection) {

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
			highlightNextOption(-1);		
			break;
		case Keys.DOWN:
		case Keys.S:
			highlightNextOption(1);
			break;			
		case Keys.ENTER:
			if( dialogState == DialogState.DISPLAYING){
				break;
			}
			
			if( dialogState == DialogState.MAKING_SELECTION){				
				selectLine();
				break;
			}
			
			if( dialogState == DialogState.WAITING_TO_DISMISS){
				continueDialog();
				break;
			}
			
			if( dialogState == DialogState.ENDING){
				endDialog();
				break;
			}
			
			break;
		case Keys.E:
			selectLine();
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
