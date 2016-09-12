package com.election.game;

import com.badlogic.gdx.Input.Keys;

public class Constants {

	public static final int WINDOWS_GAME_WIDTH = 1024;
	public static final int WINDOWS_GAME_HEIGHT = 800;
	
	
	public static final String NPC_LOCATION_LAYER = "npc_locations";
	public static final int ELECTORATE_COUNT_MAX = 50;
	
	public static final String GAME_TITLE = "ELection Game";

	public static final float CHAR_YSPEED = 100;
	public static final float CHAR_XSPEED = 100;
	public static final float CAM_YSPEED = 100;
	public static final float CAM_XSPEED = 100;
	
	public static final float CAM_MOVE_SCREEN_PERCENTAGE = .1f;
	
	public static final int TILE_NUMBER = 32;
	public static final int TILE_SIZE = WINDOWS_GAME_WIDTH/TILE_NUMBER;
	public static final int CANDIDATE_INTERACT_KEY = Keys.E;
	
	
	//DIALOG CONSTANTS
	public static final int CAND_NAME =0;
	public static final String DIALOG_TREES_PATH="dialog/dialog_trees.json";
	public static final String DIALOG_LINES_PATH="dialog/dialog_lines.json";
	
	
}
