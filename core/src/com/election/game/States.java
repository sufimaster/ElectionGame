package com.election.game;

public class States {

	public enum GameState { 
		READY, 
		INTRO,
		RUNNING, 
		DIALOG, 
		PAUSED, 
		MAP_TRANSITION,
		GAMEOVER 
	} 
	
	public enum DialogState{	
		DISPLAYING,
		WAITING_TO_DISMISS,
		MAKING_SELECTION,
		ENDING;
		
	}
	
	public enum InteractionState{
		NONE,
		ELECTOR,
		HOUSE,
		ITEM,
		SHOPKEEPER,
		POLL;
			
	}
	
	public enum WalkingDirection{
		NONE,
		UP,
		DOWN,
		LEFT,
		RIGHT;
	}
	
	public enum AnimationState{
		IDLE, WALKING
	}
	
	
}
