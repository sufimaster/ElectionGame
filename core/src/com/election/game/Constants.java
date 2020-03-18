package com.election.game;

import java.util.TreeMap;

import com.badlogic.gdx.Input.Keys;
import com.election.game.maps.TownMap;

public class Constants {

	
	public static final float VIRTUAL_HEIGHT = 10f; //40 meters height
	
	
	
	
	public static final int WINDOWS_GAME_WIDTH = 1024;
	public static final int WINDOWS_GAME_HEIGHT = 800;

	public static final int ELECTORATE_COUNT_MAX = 5;
	
	public static final String GAME_TITLE = "ELection Game";

	public static final float CHAR_YSPEED = 2;
	public static final float CHAR_XSPEED = 2;
	public static final float CAM_YSPEED = 100;
	public static final float CAM_XSPEED = 100;
	
	public static final float CAM_MOVE_SCREEN_PERCENTAGE = .04f;
	
	public static final int TILE_NUMBER = 32;
	public static final int TILE_SIZE = WINDOWS_GAME_WIDTH/TILE_NUMBER;
	public static final int CANDIDATE_INTERACT_KEY = Keys.E;
	
	//NPC CONSTANTS
	public static final String NPC_DEF_PATH = "data/npcdata/npcdata.json";
	
	
	//Quest Constants
	public static final String QUEST_DEF_PATH = "data/quests/quests.json";
	
	//skin constants
	public static final String UI_SKIN_PATH = "data/skins/uiskin.json";
	public static final String UI_FONT_PATH = "data/font/arial.ttf";
	
	//DIALOG CONSTANTS
	public static final int CAND_NAME =0;
	public static final String DIALOG_TREES_PATH="data/dialog/dialog_trees.json";
	public static final String DIALOG_LINES_PATH="data/dialog/dialog_lines.json";
	public static final int UNSELECTED = -128929;
	public static final int NO_MORE_DIALOG = -12123;
	
	
	//MAP CONSTANTS
	public static final String MAPS_DEF_PATH = "data/config/maps.json";
	public static final String MAP_OBJ_PHYSICS_LAYER = "physics";
	public static final String MAP_OBJ_NPC_LOCATION_LAYER = "npc_locations";
	public static final String MAP_OBJ_OBJECT_TYPE = "type";
	public static final String MAP_OBJ_OBJECT_TYPE_COLLIDE = "collide";
	public static final String MAP_OBJ_DOOR = "door";
	public static final String MAP_OBJ_DOOR_ID = "id";	
	public static final String MAP_OBJ_DOOR_NONE = "-1";
	public static final String MAP_OBJ_OBJECT_LAYER = "objects";


	public static final Object QUEST_DEFN_QUESTS_KEY = "quests";

	public static final int HOMELESS = -129;

	public static final String MAP_OUTSIDE_WORLD_PREFIX = "MO";	



	public static final float MAP_TRANSITION_TIME = 2f; //2 second transition between maps
	
	
	
	
}
