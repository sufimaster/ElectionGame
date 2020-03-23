package com.election.game.json;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.election.game.Constants;
import com.election.game.Electorate;
import com.election.game.Utilities;
import com.election.game.dialog.DialogContainer;
import com.election.game.dialog.DialogModel;
import com.election.game.dialog.DialogTree;
import com.election.game.maps.TownMap;
import com.election.game.quests.Quest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonParser {

	
	Gson gson;
	
	public JsonParser(){
	
		gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().
				registerTypeAdapter(Sprite.class, new SpriteDeserializer()).
				registerTypeAdapter(TiledMap.class, new MapDeserializer()).
				create();
		
	}

	public Map<String, TownMap> parseMapsFile(String electorDefPath){
		
		FileHandle electorDefFile = Gdx.files.internal(electorDefPath);
		Map <String, TownMap> maps = gson.fromJson(electorDefFile.readString(), new TypeToken<LinkedHashMap<String, TownMap>>(){}.getType());
		
		return maps;
	}
	
	
	
	public Map<String, Electorate> parseElectorFile(String electorDefPath){
		
		FileHandle electorDefFile = Gdx.files.internal(electorDefPath);
		Map <String, Electorate> electorate = gson.fromJson(electorDefFile.readString(), new TypeToken<LinkedHashMap<String, Electorate>>(){}.getType());
		
		return electorate;
	}
	
	public Map<String, Quest> parseQuest(String questDefPath){
		
		//ArrayList<Quest> quests = new ArrayList<Quest>();
		
		FileHandle questDefFile = Gdx.files.internal(questDefPath);
		Map<String, Map<String,Quest>> quests = gson.fromJson(questDefFile.readString(), new TypeToken<LinkedHashMap<String, LinkedHashMap<String, Quest>>>(){}.getType()); 
		
		return quests.get(Constants.QUEST_DEFN_QUESTS_KEY);
		
		
	}
	
	
	public DialogContainer parseDialog(String dialogTreesPath, String dialogLinesPath){
		
		DialogContainer holder = new DialogContainer();
		
		
		FileHandle file1 = Gdx.files.internal(dialogTreesPath);		
		String dialogTreesText = file1.readString();
		
		FileHandle file2 = Gdx.files.internal(dialogLinesPath);		
		String dialogLinesText = file2.readString();
		
		
		Map <String, DialogTree> dialogTrees = parseDialogTrees(dialogTreesText);
		Map<String,DialogModel> dialogLines = parseDialogs(dialogLinesText);
		
		
		holder.setDialogTrees(dialogTrees);
		holder.setDialogs(dialogLines);
		
		return holder;
		
	}
	


	public Map <String, DialogTree> parseDialogTrees(String dialogTrees) {
		
		
		Map <String, DialogTree> dialogTree = gson.fromJson( dialogTrees, new TypeToken<LinkedHashMap<String, DialogTree>>(){}.getType() );
		
		return dialogTree;
	}


	public Map<String, DialogModel> parseDialogs(String dialogLines) {

		
		Map<String, DialogModel>  lines = gson.fromJson( dialogLines, new TypeToken<LinkedHashMap<String, DialogModel>>(){}.getType() );
		
		return lines;
	}

	
	public void serializeDialogLines(Map<String, DialogModel> dialogLines, Writer writer) {
		gson.toJson(dialogLines, writer);
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void serializeDialogTrees(Map <String, DialogTree> dialogTree , Writer writer){
		
		
		gson.toJson(dialogTree, writer);
		
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	//test
	public static void main(String []args){
		
		
		JsonParser parser = new JsonParser();
		
		

		String dialogTreesContent = Utilities.fileToString (	Paths.get("E:/Projects/libgdx/electionDay/android/assets/data/dialog", "dialog_trees.json")  );
		
		String dialogLinesContent  = Utilities.fileToString (	Paths.get("E:/Projects/libgdx/electionDay/android/assets/data/dialog", "dialog_lines.json")  );
		
			
		Map <String, DialogTree> dialogTree = parser.parseDialogTrees(dialogTreesContent);
		Map<String, DialogModel>  dialogLines = parser.parseDialogs(dialogLinesContent);

		System.out.println(dialogTree);
		
		System.out.println(dialogLines);
	}
	
	

	
}
