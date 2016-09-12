package com.election.game.dialog;

import java.util.Map;

public class DialogContainer {
	
	
	private Map<String, DialogTree> dialogTrees;
	
	private Map<String, Dialog> dialogs;

	public Map<String, DialogTree> getDialogTrees() {
		return dialogTrees;
	}

	public void setDialogTrees(Map<String, DialogTree> dialogTrees) {
		this.dialogTrees = dialogTrees;
	}

	public Map<String, Dialog> getDialogs() {
		return dialogs;
	}

	public void setDialogs( Map<String,Dialog> dialogs) {
		this.dialogs = dialogs;
	}
	
	
	public DialogTree getElectorDialog(int id){
		
		DialogTree tree = dialogTrees.get(id +"");
		
		if( tree == null){			
			return new DialogTree();		
		}
		
		return tree;
				
	}
	
	
}
