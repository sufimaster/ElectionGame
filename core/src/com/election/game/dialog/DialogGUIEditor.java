package com.election.game.dialog;

import java.nio.file.Paths;
import java.util.Map;

import com.election.game.Utilities;
import com.election.game.json.JsonParser;
import com.google.gson.Gson;

public class DialogGUIEditor {

	Gson gson;
	DialogContainer container;
	
	public static void main(String []args){		
		
		DialogGUIEditor editor = new DialogGUIEditor();

	}
	
	public DialogGUIEditor(){
		gson = new Gson();
		
		JsonParser parser = new JsonParser();
		
		

		String dialogTreesContent = Utilities.fileToString (	Paths.get("E:/Projects/libgdx/electionDay/android/assets/dialog", "dialog_trees.json")  );
		
		String dialogLinesContent  = Utilities.fileToString (	Paths.get("E:/Projects/libgdx/electionDay/android/assets/dialog", "dialog_lines.json")  );
		
			
		Map <String, DialogTree> dialogTree = parser.parseDialogTrees(dialogTreesContent);
		Map<String, DialogModel>  dialogLines = parser.parseDialogs(dialogLinesContent);
		
		container.setDialogTrees(dialogTree);
		container.setDialogs(dialogLines);
	}
	
	
	
}
