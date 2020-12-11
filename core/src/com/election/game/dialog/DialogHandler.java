package com.election.game.dialog;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.election.game.Constants;
import com.election.game.ElectionGame;
import com.election.game.Electorate;
import com.election.game.States.DialogState;
import com.election.game.States.GameState;


public class DialogHandler implements InputProcessor {
	
	BitmapFont font;
	BitmapFont selectedFont;

	
	Electorate elector;
	DialogContainer dialogObj;
	DialogTree candidateDialogTree;
	Map<String, String> currentDialogOptions;
	DialogModel displayingDialog;

	
	int candidateDialogIdx = 0;
	int highlightedDialogOption=0;
	int selectedDialogOption= Constants.UNSELECTED;
	float accum = 0f;
	private boolean dialogEnabled = true;
	
	public DialogState dialogState=DialogState.DISPLAYING;
	
	
	//new stuff scene2d
	private Stage stage;
	private Window dialogDisplay;
	
	private Window dialogSelection;
	private Label dialogLabel;
	
	float letterDrawRate = 1f/.2f;  //1 letter every .2 seconds
	private float percentage;
	private Table selectionTable;
	private Skin skin;
	
	
	public DialogHandler(DialogContainer dialogContainer, BitmapFont font, BitmapFont selectedFont){
		init();
		
		
		
		this.font = font;
		this.selectedFont = selectedFont;
		
		dialogObj = dialogContainer;
		skin = ElectionGame.GAME_OBJ.dialogSkin;
		stage = new Stage();

		
		createDialogDisplay();
		createDialogSelection();
		
	}

	private void createDialogDisplay() {
		dialogDisplay = new Window("Dialog", skin);
		dialogDisplay.setSize(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2.5f);
		
		
		//dialogDisplay.setDebug(true);
		
		float width = (float)Gdx.graphics.getWidth();

		dialogDisplay.setPosition( width*.01f,width*.01f );
		
		stage.addActor(dialogDisplay);
				
		Table table = new Table();
		table.debug();
		table.right();	
		
		final ScrollPane scroll = new ScrollPane(table, skin);
		scroll.setFadeScrollBars(false);
		//scroll.setDebug(true);
		
		
		dialogLabel = new Label("", skin);	
		dialogLabel.setWrap(true);
		dialogLabel.setFillParent(true);
		
		table.row();
		table.add( dialogLabel);
		
		TextButton closeButton = new TextButton("X", skin);
		closeButton.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				closeDialog();
				
			}

			
		});
		
		dialogDisplay.getTitleTable().add(closeButton);
		
		
		dialogDisplay.add(scroll).expand().fill().colspan(4);
		dialogDisplay.row().space(10).padBottom(10);
		TextButton continueButton = new TextButton("Ok", skin);
		continueButton.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// change state to DIALOG_MAKING_SELECTION
				dialogState = DialogState.MAKING_SELECTION;
			}
		});
		
		dialogDisplay.add(continueButton);
		
		dialogDisplay.setVisible(false);
	}
	
	
	private void closeDialog() {
		dialogDisplay.remove();
		dialogSelection.remove();
		endDialog();
		
	}
	
	
	public void createDialogSelection(){
		
		dialogSelection = new Window("Choices", skin);
		dialogSelection.setSize(Gdx.graphics.getWidth()/3, dialogDisplay.getHeight());
		dialogSelection.setDebug(false);
		//dialogSelection.setPosition( 20 + dialogDisplay.getX() + dialogDisplay.getWidth(), 0);
		float width = (float)Gdx.graphics.getWidth();
		dialogSelection.setPosition( Gdx.graphics.getWidth() - dialogSelection.getWidth() - (0.01f)*width, (0.01f)*width);
		
		
		stage.addActor(dialogSelection);
				
		selectionTable = new Table();
		selectionTable.debug();
	

		final ScrollPane scroll = new ScrollPane(selectionTable, skin);		
		scroll.setFadeScrollBars(false);
		scroll.setDebug(true);
				
		selectionTable.padTop(10);
		
		dialogSelection.add(scroll).expand().fill().colspan(4);
		
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
	
	
	public void draw(float delta){
		
		//Gdx.app.log(this.getClass().getName(), "Dialog State:" + dialogState);
		
		stage.setDebugAll(ElectionGame.GAME_OBJ.isdebug);
			
		
		if(!dialogEnabled){
			return;
		}
		
		if( dialogState == DialogState.DISPLAYING){
			populateDialog();
			
		}else if( dialogState == DialogState.WAITING_TO_DISMISS){
			populateDialog();
			
		}else if( dialogState == DialogState.MAKING_SELECTION){
			
			
			if( !dialogLabel.getText().toString().trim().equals("")){
				populateDialog();
			}
			//populateChoices();
						
		}else if( dialogState == DialogState.ENDING){
			
			endDialog();
		}
		
		
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		
		
	}





	private void populateDialog() {
		
			
		//Gdx.app.log(this.getClass().getName(), "length to draw: " + lengthToDraw);
		//Gdx.app.log(this.getClass().getName(), "Dialog length: " + dialogLabel.getText().length);
		
		
				
		if( displayingDialog !=null){
			
			dialogDisplay.setVisible(true);
			dialogLabel.setText(displayingDialog.toString());
							
			//layout1.setText(font, , Color.RED, dialogBox.getWidth() , Align.topLeft, true );
			//float width = layout.width;
			//float height = layout.height;
			
			
			//dialogBox.draw(batch);
			//font.draw(batch, layout1, dialogBox.getX()+ BUFFER_WIDTH, dialogBox.getY() + dialogBox.getHeight()/1.6f - BUFFER_WIDTH);
		}
		
		
		if( candidateDialogIdx == Constants.NO_MORE_DIALOG){
			dialogState = DialogState.WAITING_TO_DISMISS;
		}else{
			dialogState = DialogState.MAKING_SELECTION;
		}
		
		
	}

	private void populateChoices() {
		
		if( candidateDialogIdx == Constants.NO_MORE_DIALOG){
			dialogSelection.setVisible(false);
			dialogState = DialogState.WAITING_TO_DISMISS;
			return;
		}
		
		dialogSelection.setVisible(true);
		selectionTable.clear();
		
		//get the list of dialogs available for candidate to say
		List<Map<String, String>> list  = candidateDialogTree.getInput();				

		//get the dialogs the candidate says to elector 
		//if the first time, candidateDialogIdx shold be 0
		//if its later in the conversation, candidateIdx should be the index for the particular
		//tree that is in response to the elector's response
		currentDialogOptions = list.get(candidateDialogIdx);		
		
		//choiceBox.draw(batch);
		
		
		//iterate through each of the dialog line options for the candidate and add them to dialog box		
		int count=0;		
		Iterator<String> itr  = currentDialogOptions.keySet().iterator();
		while(itr.hasNext()){
			
			String key = itr.next();				
			DialogModel dialogModelObj = dialogObj.getDialogs().get(key);
			
			
			
			
			
			if( count==highlightedDialogOption){
				
				Label choice1 = new Label(dialogModelObj.toString(), skin);			
				choice1.setWrap(true);
				choice1.setAlignment(Align.left);
				
				//add to table
				Label highlightLabel = new Label(">>", skin);
				highlightLabel.setWidth(5);
				highlightLabel.setAlignment(Align.top | Align.left);

				selectionTable.add(highlightLabel);
				selectionTable.add( choice1).growX().top().left().padLeft(10);
				selectionTable.row().pad(15);
				
				//layout2.setText(selectedFont,  text.id + ": " + text.value, Color.RED, choiceBox.getWidth(), Align.topLeft, true );				
				//selectedFont.draw(batch, layout2, 10, choiceBox.getY() + choiceBox.getHeight()/1.7f - yOffset);
			}else{			
				
				//TODO: Distinguish by using different skin?				
				Label choice1 = new Label(dialogModelObj.toString(), skin);			
				choice1.setWrap(true);
				choice1.setAlignment(Align.left);
								
				//add to table
				Label notHighlightLabel = new Label("---", skin);
				notHighlightLabel.setWidth(5);
				notHighlightLabel.setAlignment(Align.top | Align.left);
				
				selectionTable.add(notHighlightLabel);
				selectionTable.add( choice1).growX().top().left().padLeft(10);
				selectionTable.row().pad(15);
				//layout2.setText(font,  text.id + ": " + text.value, Color.RED, choiceBox.getWidth(), Align.topLeft, true );
				//font.draw(batch, layout2, 10, choiceBox.getY() + choiceBox.getHeight()/1.7f- yOffset);
			}
			count ++;
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
			populateChoices();
			dialogState = DialogState.MAKING_SELECTION;
		}
		
		
		
	}
	

	public void endDialog(){
		dialogDisplay.setVisible(false);
		dialogSelection.setVisible(false);
		
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
	
			selectedDialogOption = 0;
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
		
		
		
		//check if the candidate's selected dialog line adds a quest to his Task List
		if( candidateDialogTree.quest != null) {
			String questID = candidateDialogTree.quest.get(selectedCandidateKey);
			if( questID != null && !questID.trim().isEmpty()){
				//add quest to task list
				ElectionGame.GAME_OBJ.questHandler.activateQuest(questID);
			}			
		}
		
		
		//get the elector response KEY (like R1, R2 etc) mapped to the selected Candidate dialog line C1, C2, etc...
		//if this is null, that means you display the final response, and end the dialog
		String electorDialogKey = currentDialogOptions.get(selectedCandidateKey);
		Gdx.app.log(this.getClass().getName(), "Candidate key: " + selectedCandidateKey + ", NPC key:" + electorDialogKey);
		
		
		//if the elector has something else get the candidate's array of responses available 
		//otherwise, end dialog
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
		
		populateChoices();
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
		
		populateChoices();
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
