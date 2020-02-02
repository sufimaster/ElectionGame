package com.election.game.dialog;

import java.util.Map;

public class DialogContainer {
	
	
	private Map<String, DialogTree> dialogTrees;
	
	private Map<String, DialogModel> dialogs;

	public DialogContainer(Map<String, DialogTree> dialogTree, Map<String, DialogModel> dialogModel) {
		this.dialogTrees = dialogTree;
		this.dialogs = dialogModel;
	}

	public DialogContainer() {
		// TODO Auto-generated constructor stub
	}

	public Map<String, DialogTree> getDialogTrees() {
		return dialogTrees;
	}

	public void setDialogTrees(Map<String, DialogTree> dialogTrees) {
		this.dialogTrees = dialogTrees;
	}

	public Map<String, DialogModel> getDialogs() {
		return dialogs;
	}

	public void setDialogs( Map<String,DialogModel> dialogs) {
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
