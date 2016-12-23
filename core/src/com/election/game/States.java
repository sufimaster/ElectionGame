package com.election.game;

public class States {

	public enum GameState { 
		READY, 
		INTRO,
		RUNNING, 
		DIALOG, 
		PAUSED, 
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
	
}
