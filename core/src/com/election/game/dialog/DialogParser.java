package com.election.game.dialog;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DialogParser {

	
	Gson gson;
	
	public DialogParser(){
	
		gson = new Gson();

		
	}

	
	public DialogContainer parseDialog(String dialogTreesPath, String dialogLinesPath){
		
		DialogContainer holder = new DialogContainer();
		
		
		FileHandle file1 = Gdx.files.internal(dialogTreesPath);		
		String dialogTreesText = file1.readString();
		
		FileHandle file2 = Gdx.files.internal(dialogLinesPath);		
		String dialogLinesText = file2.readString();
		
		
		Map <String, DialogTree> dialogTrees = parseDialogTrees(dialogTreesText);
		Map<String,Dialog> dialogLines = parseDialogs(dialogLinesText);
		
		
		holder.setDialogTrees(dialogTrees);
		holder.setDialogs(dialogLines);
		
		return holder;
		
	}
	
	

	private Map <String, DialogTree> parseDialogTrees(String dialogTrees) {
		
		
		Map <String, DialogTree> dialogTree = gson.fromJson( dialogTrees, new TypeToken<Map<String, DialogTree>>(){}.getType() );
		
		return dialogTree;
	}


	private Map<String, Dialog> parseDialogs(String dialogLines) {

		
		Map<String, Dialog>  lines = gson.fromJson( dialogLines, new TypeToken<Map<String, Dialog>>(){}.getType() );
		
		return lines;
	}


	//test
	public static void main(String []args){
		
		
		DialogParser parser = new DialogParser();
		
		

		String dialogTreesContent = fileToString (	Paths.get("E:/Projects/libgdx/electionDay/android/assets/dialog", "dialog_trees.json")  );
		
		String dialogLinesContent  = fileToString (	Paths.get("E:/Projects/libgdx/electionDay/android/assets/dialog", "dialog_lines.json")  );
		
			
		Map <String, DialogTree> dialogTree = parser.parseDialogTrees(dialogTreesContent);
		Map<String, Dialog>  dialogLines = parser.parseDialogs(dialogLinesContent);

		System.out.println(dialogTree);
		
		System.out.println(dialogLines);
	}
	
	
	public static String fileToString(Path path){
		
		//Path file = Paths.get("E:/Projects/libgdx/electionDay/android/assets", "dialogTrees.json");
	    Charset charset = Charset.forName("ISO-8859-1");
		final String EoL = System.getProperty("line.separator");

	    
	    
	    List<String> lines = new ArrayList<String>();
	    StringBuilder builder = new StringBuilder();
		try {
		      lines = Files.readAllLines(path, charset);

		      for (String line : lines) {
		    	  builder.append(line).append(EoL);
		      }
		}catch (IOException e) {
			
		      System.out.println(e);
		}
		
		return builder.toString();
		
	}
	
}
