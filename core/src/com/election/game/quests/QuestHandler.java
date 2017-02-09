package com.election.game.quests;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.election.game.ElectionGame;
import com.election.game.hud.HudComponent;
import com.election.game.sprites.Candidate;

public class QuestHandler {

	public Map<String, Quest> quests;

	public HudComponent hud;
	
	public Vector2 windowPosition;
	
	
	public QuestHandler(){
		windowPosition = new Vector2(Gdx.graphics.getWidth() *4/5, Gdx.graphics.getHeight() *4/5 );
		quests = new HashMap<String, Quest>();
		hud = new HudComponent("Quests", ElectionGame.GAME_OBJ.dialogFont, ElectionGame.GAME_OBJ.dialogSkin, windowPosition, new Vector2(200,200), false);
	}
	

	public void setQuests(Map<String, Quest> map) {

		this.quests = map;
	}

	public void activateQuest(String questID) {

		Quest quest = quests.get(questID);
		
		if( !quest.active){
			quest.activate();
			Label questLabel = new Label(quest.name, hud.skin);
			hud.contentTable.add(questLabel).growX().top().left().padLeft(10);
			hud.contentTable.row().pad(5);
			
			if( quest.subquests == null){
				return;
			}
			
			for (Quest subquest : quest.subquests) {
				
				Label subquestLabel = new Label(subquest.name, hud.skin);
				hud.contentTable.add(subquestLabel).growX().top().left().padLeft(10);
				hud.contentTable.row().pad(3);

			}
			
		}
			
	}
	
	public void completeQuest(String questID, Candidate candidate){
		Quest quest = quests.get(questID);
		quest.success = true;
		candidate.questPositiveReward += quest.reward;
			
	}
	
	public void failQuest(String questID){
		Quest quest = quests.get(questID);
		quest.success = false;	
	}


	public void draw(float delta) {
		
		hud.draw(delta);
	}
	
	
	
	
}
