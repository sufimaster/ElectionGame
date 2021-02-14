package com.election.game;


import com.badlogic.gdx.Input.Keys;

public class Constants {

	
	public static final float VIRTUAL_HEIGHT = 10f; //40 meters height
	
	public static final int RES_1024_800 [] = {1024,800};
	public static final int RES_1920_1080 [] = {1920,1080};
	public static final int RES_1440_900 [] = {1440,900};
	public static final int RES_3840_2160 [] = {3840,2160};
	
	
	public static final int WINDOWS_GAME_WIDTH = RES_1920_1080[0];//1024;
	public static final int WINDOWS_GAME_HEIGHT = RES_1920_1080[1];//800;

	public static final float UNIT_SCALE = 1f/16f;
	
	public static final int ELECTORATE_COUNT_MAX = 5;
	
	public static final String GAME_TITLE = "ELection Game";

	public static final float CHAR_YSPEED = 2;
	public static final float CHAR_XSPEED = 2;
	public static final float CAM_YSPEED = 100;
	public static final float CAM_XSPEED = 100;
	
	public static final float CAM_MOVE_SCREEN_PERCENTAGE = .04f;
	//public static final float CAM_MOVE_SCREEN_PERCENTAGE = .4f;
	
	public static final int TILE_NUMBER = 32;
	public static final int TILE_SIZE = 128;//WINDOWS_GAME_WIDTH/TILE_NUMBER;
	public static final int CANDIDATE_INTERACT_KEY = Keys.E;
	
	//input constants
	public static final int CANDIDATE_MOVE_UP_KEY = Keys.W;
	public static final int CANDIDATE_MOVE_DOWN_KEY = Keys.S;
	public static final int CANDIDATE_MOVE_LEFT_KEY = Keys.A;
	public static final int CANDIDATE_MOVE_RIGHT_KEY = Keys.D;
	
	//NPC CONSTANTS
	public static final String NPC_DEF_PATH = "data/npcdata/npcdata.json";
	//public static final String PC_IMG_SRC = "MC.png";\
	public static final String PC_BATTLE_IMG_SRC = "battle/char_btl_sheet.png";
	public static final String PC_IMG_SRC = "character_64x96.png";
	public static final String BATTLE_BG = "battle/battle_bg.png";

	
	//Quest Constants
	public static final String QUEST_DEF_PATH = "data/quests/quests.json";
	
	//skin constants
	public static final String UI_SKIN_PATH = "data/skins/uiskin.json";
	public static final String UI_FONT_PATH = "data/font/arial.ttf";
	
	//DIALOG CONSTANTS
	public static final int CAND_NAME =0;
	public static final String DIALOG_TREES_PATH="data/dialog/test_dialogtrees.json"; //"data/dialog/dialog_trees.json";
	public static final String DIALOG_LINES_PATH="data/dialog/test_dialoglines.json"; //"data/dialog/dialog_lines.json";
	public static final int UNSELECTED = -128929;
	public static final int NO_MORE_DIALOG = -12123;
	
	
	//MAP CONSTANTS
	public static final String MAPS_DEF_PATH = "data/config/maps.json";
	public static final String MAP_OBJ_PHYSICS_LAYER = "physics";
	public static final String MAP_OBJ_NPC_LOCATION_LAYER = "npc_locations";
	public static final String MAP_OBJ_OBJECT_TYPE = "type";
	public static final String MAP_OBJ_OBJECT_TYPE_COLLIDE = "collide";
	public static final String MAP_OBJ_DOOR = "door";
	public static final String MAP_OBJ_EXIT_AREA = "exit_area";
	public static final String MAP_OBJ_DOOR_ID = "id";	
	public static final String MAP_OBJ_DOOR_NONE = "-1";
	public static final String MAP_OBJ_OBJECT_LAYER = "objects";


	public static final Object QUEST_DEFN_QUESTS_KEY = "quests";

	public static final int HOMELESS = -129;

	public static final String MAP_OUTSIDE_WORLD_PREFIX = "MO";	



	public static final float MAP_TRANSITION_TIME = 2f; //2 second transition between maps

	public static final String MAP_MOMS_HOUSE = "5";

	public static final int KEY_DEBUG = Keys.NUM_0;

	public static final Object YES = "y";
	public static final Object NO = "n";

	public static final int DEFAULT_INITIAL_AP = 10;

	public static final Object DIALOG_SEP = "--------------------\n";

}
