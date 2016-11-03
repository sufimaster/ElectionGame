package com.election.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import com.badlogic.gdx.Input.Keys;

public class Constants {

	public static final int WINDOWS_GAME_WIDTH = 1024;
	public static final int WINDOWS_GAME_HEIGHT = 800;

	public static final int ELECTORATE_COUNT_MAX = 5;
	
	public static final String GAME_TITLE = "ELection Game";

	public static final float CHAR_YSPEED = 100;
	public static final float CHAR_XSPEED = 100;
	public static final float CAM_YSPEED = 100;
	public static final float CAM_XSPEED = 100;
	
	public static final float CAM_MOVE_SCREEN_PERCENTAGE = .04f;
	
	public static final int TILE_NUMBER = 32;
	public static final int TILE_SIZE = WINDOWS_GAME_WIDTH/TILE_NUMBER;
	public static final int CANDIDATE_INTERACT_KEY = Keys.E;
	
	
	//DIALOG CONSTANTS
	public static final int CAND_NAME =0;
	public static final String DIALOG_TREES_PATH="dialog/dialog_trees.json";
	public static final String DIALOG_LINES_PATH="dialog/dialog_lines.json";
	public static final int UNSELECTED = -128929;
	public static final int NO_MORE_DIALOG = -12123;
	
	public static final String MAP_OBJ_PHYSICS_LAYER = "physics";
	public static final String MAP_OBJ_NPC_LOCATION_LAYER = "npc_locations";
	public static final String MAP_OBJ_OBJECT_TYPE = "type";
	public static final String MAP_OBJ_DOOR = "door";
	public static final String MAP_OBJ_DOOR_ID = "id";

	
	public static final TreeMap<Integer, TownMap> tiledMaps = new TreeMap<Integer, TownMap>();
	public static final int MAP_OBJ_DOOR_NONE = -1;

	static
	    {
	        tiledMaps.put(new Integer(1), new TownMap("maps/town.tmx"));
	        tiledMaps.put(new Integer(2), new TownMap("maps/house1.tmx"));
	        tiledMaps.put(new Integer(3), new TownMap("maps/house1.tmx"));
	        tiledMaps.put(new Integer(4), new TownMap("maps/house1.tmx"));
	        tiledMaps.put(new Integer(5), new TownMap("maps/house1.tmx"));
	        tiledMaps.put(new Integer(6), new TownMap("maps/house1.tmx"));
	        tiledMaps.put(new Integer(7), new TownMap("maps/house1.tmx"));
	    }
	
	
	
	
}
