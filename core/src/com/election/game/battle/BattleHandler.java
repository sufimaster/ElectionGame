package com.election.game.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.election.game.Constants;
import com.election.game.ElectionGame;
import com.election.game.Electorate;
import com.election.game.States.GameState;
import com.election.game.sprites.Candidate;

public class BattleHandler implements InputProcessor {

	//public HudComponent hud;
	
	public Vector2 windowPosition;

	//private Image background;

	private Skin skin;

	//private Window battleWindow;

	private Stage stage;

	//private Window battleDisplay;
	
	private Table battleTable;
	

	private Image player_battle_img;
	private Image opponent_battle_img;

	private Candidate fighter;
	private Electorate opponent;

	private int givenDamage;

	private int receivedDamage;

	private boolean attackSuccess;

	private Table opponentTable;
	
	
	/*
	 * Battle System: THe Candidate/Player collects Argument points (ARG) by engaging in conversation
	 * or buying them or learning new intel/facts about the town, people, likes, dislikes etc. 
	 * The Candidate can choose to engage in battle with the people in town. If the player has more arg points
	 * than elector sway points, he wins the battle.
	 * 
	 */
	
	public BattleHandler(TextureRegion background){
		//hud = new HudComponent("Quests", ElectionGame.GAME_OBJ.dialogFont, ElectionGame.GAME_OBJ.dialogSkin, 
		//		Gdx.graphics.getWidth()*.01f, Gdx.graphics.getWidth()*.01f, 1920,1080, false, false);

	//	this.background = new Image(background);
		this.player_battle_img = new Image(new Texture(Constants.PC_BATTLE_IMG_SRC));
		this.player_battle_img.setScale(15);
		this.skin = ElectionGame.GAME_OBJ.dialogSkin;

		stage = new Stage();
		stage.setDebugAll(true);
		populateBattleStage();
	}
	
	private void populateBattleStage() {
		

		
		
		
		/*
		 * battleDisplay = new Window("Battle", skin);
		 * battleDisplay.setSize(Gdx.graphics.getWidth()/2f,
		 * Gdx.graphics.getHeight()/2.5f);
		 * 
		 * 
		 * battleDisplay.setFillParent(false); battleDisplay.addActor(player);
		 */
		
		battleTable = new Table();
		battleTable.debug();
		battleTable.setFillParent(true);
		
		opponentTable = new Table();
		opponentTable.debug();
		
		battleTable.add(opponentTable);
		battleTable.add(player_battle_img); //addActor(player);
		
		
		TextButton fightButton = new TextButton("FIGHT!", skin);
		fightButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				endBattle();
			}
			
		});
		
		TextButton endBattleButton = new TextButton("End Battle", skin);
		endBattleButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				endBattle();
			}
			
		});
		battleTable.row();
		battleTable.add(fightButton);
		battleTable.row();
		battleTable.add(endBattleButton);		
		
		stage.addActor(battleTable);
		//battleDisplay.setVisible(false);
		
	}

	public void draw(float delta) {
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
	}

	public void startBattle(Candidate candidate, Electorate interactedElector) {
		battleTable.setVisible(true);
		this.fighter = candidate;
		this.opponent = interactedElector;
		
		opponentTable.clear();
		opponentTable.add(new Image(this.opponent.sprite.getTexture()));
		
		Gdx.app.log(this.getClass().getName(), "Starting Battle with "+ interactedElector.id);
	}
	

	public void fight() {

		//Rules
		/*if opponent influence is lower than persuasion, the attack hits for persuasion-influence hp
		//if opponent influence is higher than candidates persuasion, then candidate intellect+persuasion 
		//must be greater than opponent persuasion; attack hits = (int +per -opp_influence)/2 hp
		//if int+per is less than opp_inf, then attack backfires random hp % from 5-7%
		*/
		
		//if opponent influence is lower than persuasion, the attack hits for persuasion-influence hp
		int diff = this.fighter.attributes.influence - this.opponent.attributes.persuasion;
		if(diff>0) {
			givenDamage = diff;
			attackSuccess = true;
		}else if( diff<0) {
			
			//if opponent influence is higher than candidates persuasion, then candidate intellect+persuasion 
			//must be greater than opponent persuasion; attack hits = (int +per -opp_influence)/2 hp
			//if int+per is less than opp_inf, then attack backfires random hp % from 5-7%
			diff = this.opponent.attributes.influence - (this.fighter.attributes.intellect + this.fighter.attributes.persuasion);

			if( diff > 0 ) { //opponent has more influence than candidate int+per
				receivedDamage = diff;
				attackSuccess = false;
			}else if( diff < 0) { //opponent has less influence than candidate int+per
				givenDamage = diff/2;
				attackSuccess = true;
			}else{ //opp_inf = ftr_int+ftr_pers;
				//not sure here.
			}
		}
	}
	
	private void endBattle() {
		battleTable.setVisible(false);		
		Gdx.app.log(this.getClass().getName(), "Ending Battle");

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
			this.endBattle();
			break;
		case Keys.UP:
		case Keys.W:
			//highlightNextOption(-1);		
			break;
		case Keys.DOWN:
		case Keys.S:
			//highlightNextOption(1);
			break;			
		case Keys.ENTER:			
			break;
		case Keys.E:
			//selectAction();
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
	
	
	public void resize(int width, int height) {
        // use true here to center the camera
        // that's what you probably want in case of a UI
        stage.getViewport().update(width, height, true);
    }
	
}
