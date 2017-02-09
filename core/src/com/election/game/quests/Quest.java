package com.election.game.quests;

import java.util.List;
import java.util.Map;

public class Quest {
	
	String id;
	String name;
	String description;
	
	//reward points gained when completed this quest
	float reward; 
	boolean required=false;
	
	//npcs belonging to this quest
	int [] npcs;
		
	//subquests
	List<Quest> subquests;
	
	//NPC dialogs belonging to this quest
	Map<String, String> dialogtree;
	
	//quests that this one depends on
	List<String> dependencies;

	//activated quest?
	boolean active = false;

	//successfully completed quest?
	boolean success = false;
	
	
	public Quest(){
		
	}
	
	
	public Quest(String id) {
		this.id = id;
		
		init();
		
	}

	private void init() {

		
	}

	public void activate() {

		this.active = true;
		
		if (subquests==null){
			return;
		}
		
		for( Quest subquest: subquests){
			subquest.active = true;
		}
	}

}
