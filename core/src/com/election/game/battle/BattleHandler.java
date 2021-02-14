package com.election.game.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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

	private Image opponentImage;

	private Table playerTable;

	private boolean player_shout;

	private float speed =.5f;

	private float player_moved = 0f;

	private float moveAmt;

	private Label playerPos;
	
	public enum ATTACK_TYPE {
		
		SHOUT, REASON, QUESTION
		
	}
	
	//what do these moves look like?
	//SHOUT - character opens his mouth and lines come out to show that hes yelling
	//REASON - taking out a paper/book an pointing to it
	//QUESTION - maybe get rid of this, add a different move.
	
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
		playerPos = new Label("PLAYER POSITION", skin);

		
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
		playerPos.setColor(Color.WHITE);
		playerTable = new Table(ElectionGame.GAME_OBJ.dialogSkin);
		playerTable.debug();
		playerTable.add(player_battle_img);
		playerTable.add(playerPos);
		battleTable.add(opponentTable).width(Gdx.graphics.getWidth()/5).pad(1).align(Align.bottomLeft);
		battleTable.add(playerTable).width(Gdx.graphics.getWidth()/2).pad(1).align(Align.bottomRight); 
		
		//don't do this, because it will add it anywhere on the screen! 
		//addActor(player);
		
		
		TextButton fightButton = new TextButton("Shout", skin);
		fightButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log(this.getClass().getName(), "Shout!");
				fight(ATTACK_TYPE.SHOUT);
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
	
	public void setPreBattleConditions() {
		player_shout = false;

		speed =.5f;

		player_moved = 0f;

		moveAmt = 0f;
		
	}

	public void draw(float delta) {
		
		stage.act(Gdx.graphics.getDeltaTime());
		
		if(player_shout) {
			
			//enable shout Animation
			
			
			//player_battle_img.moveBy(-4, 0);
		
			
			/*
			if( Math.abs(player_moved) >= 2f) {
				speed = -speed;
				moveAmt = -speed*delta;
				player_battle_img.moveBy(moveAmt, 0);
				player_moved+= moveAmt;
			}if( Math.abs(player_moved)  >= 4f){
				player_shout = false;
				player_moved=0f;
				speed = -speed;
			}else {
				moveAmt = -speed*delta;
				player_battle_img.moveBy(moveAmt, 0);
				player_moved+= moveAmt;
			}*/
			playerPos.setText("Player moved:" + player_moved + ", (" + player_battle_img.getX() + ", " + player_battle_img.getY() +")");
			player_shout=false;
		}
		
		stage.draw();
		
	}

	public void startBattle(Candidate candidate, Electorate interactedElector) {
		battleTable.setVisible(true);
		this.fighter = candidate;
		this.opponent = interactedElector;
		
		//reinit battle opponent
		if( opponentImage != null) {
			opponentImage.setDrawable(new TextureRegionDrawable(new TextureRegion(this.opponent.sprite.getTexture())));
		}else {
			opponentImage = new Image(this.opponent.sprite.getTexture());
		}
		//scale him/her
		opponentImage.setScale(15);
		
		//add to holder table
		opponentTable.clear();
		opponentTable.add(opponentImage);
		
		Gdx.input.setInputProcessor(this.stage);
		Gdx.app.log(this.getClass().getName(), "Starting Battle with "+ interactedElector.id);
	}
	

	public void fight(ATTACK_TYPE type) {
		
		
		if( type == ATTACK_TYPE.SHOUT) {
			
			player_shout = true;
		}

		//FOR NOW THIS IS TOO COMPLEX
		//Rules
		/*if opponent influence is lower than persuasion, the attack hits for persuasion-influence hp
		//if opponent influence is higher than candidates persuasion, then candidate intellect+persuasion 
		//must be greater than opponent persuasion; attack hits = (int +per -opp_influence)/2 hp
		//if int+per is less than opp_inf, then attack backfires random hp % from 5-7%
		
		
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
		
		*/
	}
	
	private void endBattle() {
		battleTable.setVisible(false);		
		Gdx.app.log(this.getClass().getName(), "Ending Battle");

		ElectionGame.GAME_OBJ.state = GameState.RUNNING;
		Gdx.input.setInputProcessor((InputProcessor) ElectionGame.GAME_OBJ.getScreen());
		
		this.setPreBattleConditions();
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
