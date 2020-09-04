package com.election.game.desktop;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.election.game.Utilities;
import com.election.game.dialog.DialogContainer;
import com.election.game.dialog.DialogModel;
import com.election.game.dialog.DialogTree;
import com.election.game.dialog.editor.DialogTableModelNew;
import com.election.game.json.JsonParser;

public class DialogEditorSwingGui {
	
	
	private DialogContainer container;
	private JFrame frame;
	private JPanel content;
	private JsonParser parser;
	private List<DefaultTreeModel> masterModelList;

	public DialogEditorSwingGui(){
	
				
		parser = new JsonParser();
		masterModelList = new ArrayList<DefaultTreeModel>();

		String dialogTreesContent = Utilities.fileToString (	Paths.get("C:/Users/ayan_/Documents/Programming/libGDX/ElectionGame/android/assets/data/dialog", "test_dialogtrees.json")  );
		
		String dialogLinesContent  = Utilities.fileToString (	Paths.get("C:/Users/ayan_/Documents/Programming/libGDX/ElectionGame/android/assets/data/dialog", "test_dialoglines.json")  );
		
		//this maps a sprite id to a particular dialog tree	(identified by dialog id)
		Map <String, DialogTree> dialogTree = parser.parseDialogTrees(dialogTreesContent);
		//this is a map of a dialog id to the actual string content of the dialog line
		Map<String, DialogModel>  dialogModel = parser.parseDialogs(dialogLinesContent);
		
		container = new DialogContainer(dialogTree, dialogModel);


	}
	
	

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	public void createAndShowGUI() {
	    //Create and set up the window.
	    frame = new JFrame("Dialog Editor" );
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	    content = new JPanel();
	    content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
	    frame.getContentPane().add(content);
	 
	    createDialogPane();
	    
	    //Display the window.
	    frame.pack();
	    frame.setVisible(true);
	}

	
	public void createDialogPane() {
		createMenu();
	}
	
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Sprite Dialogs");
		menuBar.add(menu);

		//add menu for raw dialog lines
		JMenuItem dialogLinesMenuItem = new JMenuItem("Dialog Lines");
		dialogLinesMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				generateDialogLinesTable();
			}
		});
		
		menu.add(dialogLinesMenuItem);
		
		//add sub-menu for sprite dialog trees
		JMenuItem dialogTreesMenu = new JMenuItem("Sprite Dialog Trees");
		
		dialogTreesMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				generateSpriteDialogTrees();
			}
		});

		menu.add(dialogTreesMenu);

		frame.setJMenuBar(menuBar);
	}

	protected void generateDialogLinesTable() {
		
		content.removeAll();

		JLabel dialogLabel =  new JLabel("Dialog Lines for Game");
		  
		content.add(dialogLabel); 
		
		final DialogTableModelNew tableModel = new DialogTableModelNew(container.getDialogs());
		JTable dialogTable = new JTable(tableModel);
		
		content.add(new JScrollPane(dialogTable));
		JLabel dialogKey = new JLabel("Dialog Key");
		final JTextField dialogKeyField = new JTextField();
		
		JLabel dialogVal = new JLabel("Dialog Val");
		final JTextField dialogValField = new JTextField();
		
		content.add(dialogKey);
		content.add(dialogKeyField);
		
		content.add(dialogVal);
		content.add(dialogValField);
		
		JButton addRowButton = new JButton("Add Dialog Line");
		addRowButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String key = dialogKeyField.getText();
				String val = dialogValField.getText();
				DialogModel dialogLine = new DialogModel();
				dialogLine.setId(key);
				dialogLine.setValue(val);
				tableModel.addDialog(dialogLine);
			}
		});
		
		JButton saveButton = new JButton("Save Dialog");
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				((JButton) e.getSource()).setEnabled(false);
				Writer writer = null;
				try {
					writer = new FileWriter(new File("C:/Users/ayan_/Documents/Programming/libGDX/ElectionGame/android/assets/data/dialog/test.json"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				parser.serializeDialogLines(container.getDialogs(), writer );
				((JButton) e.getSource()).setEnabled(true);
			}
		});
		
		content.add(addRowButton);
		content.add(saveButton);
		frame.pack();
		
		
	}

	
	protected void generateSpriteDialogTrees() {
		content.removeAll();
		
		Map<String, DialogTree> dialogTrees = container.getDialogTrees();
		
		Set<String> keys = dialogTrees.keySet();
		Iterator<String> itr = keys.iterator();
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		while(itr.hasNext()) {
			
			String spriteId = itr.next();
			//generate dialog trees for this sprite:
			JScrollPane spriteTreePane = generateSpriteDialogTreePanels(spriteId);
			//create a seperate scroll pane for the dialog trees for each sprite id
			
			tabbedPane.addTab("Sprite: " + spriteId, spriteTreePane);
		}
		content.add(tabbedPane);
		//frame.pack();
		
		//add save button to save Dialog Trees to file.
		JButton saveButton = new JButton("Save Dialog Trees");
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//convert the tree data model back to the java representation - a Map of sprite IDs to dialog trees
				Map<String, DialogTree> dialogTrees = treeToDialogTreeMap(masterModelList);				
				
				
				//open a file
				Writer writer = null;
				try {
					writer = new FileWriter(new File("C:/Users/ayan_/Documents/Programming/libGDX/ElectionGame/android/assets/data/dialog/test_DialogTree.json"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//then write the MAP as json to a file
				parser.serializeDialogTrees(dialogTrees, writer);
				
			}

		});
		
		content.add(saveButton); 
		frame.pack();
	}
	
	protected JScrollPane generateSpriteDialogTreePanels(String spriteId) {
		
		DialogTree dialogTrees = container.getDialogTrees().get(spriteId);
		List<Map<String, String>> inputList = dialogTrees.getInput();

		DefaultMutableTreeNode topNode = new DefaultMutableTreeNode("Sprite Id: " + spriteId + " Dialog Trees");
		
		
		DefaultTreeModel masterTreeModel = new DefaultTreeModel(topNode);	
		masterModelList.add(masterTreeModel);
		
		
		JTree tree = new JTree(masterTreeModel);
		
		tree.setEditable(true);
	    JScrollPane treeView = new JScrollPane(tree);

		DefaultMutableTreeNode inputNode = new DefaultMutableTreeNode("Input");
		
		int idx=0;
		for (Map<String, String> dialogResponseMap : inputList) {
			DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("tree: " + idx);
			for (String charDialogKey : dialogResponseMap.keySet()) {
				String respKey = dialogResponseMap.get(charDialogKey);
				
				//create node for character dialog line key
				DefaultMutableTreeNode charkeyDialogNode = new DefaultMutableTreeNode(charDialogKey);
				//create new child node for the char dialog value
				DefaultMutableTreeNode charValueDialogNode = new DefaultMutableTreeNode(container.getDialogs().get(charDialogKey).getValue());
				//add value node to key node
				charkeyDialogNode.add(charValueDialogNode);

				//create child node for response dialog key
				DefaultMutableTreeNode respDialogNode = new DefaultMutableTreeNode(respKey);
				//create child node of response dialog value 
				DefaultMutableTreeNode respValueNode = new DefaultMutableTreeNode(container.getDialogs().get(respKey).getValue());
				//add response value node to response key node
				respDialogNode.add(respValueNode);
				
				//add response node to character node
				charkeyDialogNode.add(respDialogNode);
				
				//add character node to main input tree
				treeNode.add(charkeyDialogNode);
			}
			inputNode.add(treeNode);			
			idx++;
		}
		
		
		//create tree node for the characters dialog options from a particular NPC response
		Map<String, String> outputMap = dialogTrees.getOutput(); 
		DefaultMutableTreeNode outputNode = new DefaultMutableTreeNode("Output");

		for (String charKey : outputMap.keySet() ) {
			String dialogTreeId = outputMap.get(charKey);

			DefaultMutableTreeNode charKeyNode = new DefaultMutableTreeNode(charKey);
			DefaultMutableTreeNode charValueNode = new DefaultMutableTreeNode(container.getDialogs().get(charKey).getValue());
			DefaultMutableTreeNode respDialogNode = new DefaultMutableTreeNode(dialogTreeId);

			charKeyNode.add(charValueNode);
			charKeyNode.add(respDialogNode);
			outputNode.add(charKeyNode);
		}

		//create tree node for the characters dialog options from a particular NPC response
		Map<String, String> questMap = dialogTrees.getQuest(); 
		DefaultMutableTreeNode questNode = new DefaultMutableTreeNode("Quest");
		if( questMap==null) {
			questMap = new HashMap<String, String>();
		}
		for (String charKey : questMap.keySet() ) {
			String questId = questMap.get(charKey);

			DefaultMutableTreeNode charKeyNode = new DefaultMutableTreeNode(charKey);
			DefaultMutableTreeNode charValueNode = new DefaultMutableTreeNode(container.getDialogs().get(charKey).getValue());
			DefaultMutableTreeNode questValueNode = new DefaultMutableTreeNode(questId);

			charKeyNode.add(charValueNode);
			charKeyNode.add(questValueNode);
			questNode.add(charKeyNode);
		}
		
		
		//input node and output node to main tree
		topNode.add(inputNode);
		topNode.add(outputNode);
		if(!questMap.isEmpty()) {
			topNode.add(questNode);
		}
		
		//create a new panel, add input/output tree to it, and return
		JPanel panel = new JPanel();
		panel.add(treeView);

		
		JScrollPane scrollPane = new JScrollPane(panel);
		
		return scrollPane;
		//content.add(new JScrollPane(panel));
		//frame.pack();
		
	}

	
	private Map<String, DialogTree> treeToDialogTreeMap(List<DefaultTreeModel> masterModelList) {
		//loop through each scroll pane each represnting dialog trees for one sprite, to build a
		//Map <String, DialogTree> of all the dialogTrees for each sprite
		
		Map <String, DialogTree> dialogTreeMap = new LinkedHashMap<String, DialogTree>();
		int spriteId =0;
		for (DefaultTreeModel defaultTreeModel : masterModelList) {
			DefaultMutableTreeNode topNode = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
			DialogTree dialogTree  = deconstructDMTToDialogObject(topNode);
			//map from sprite id to dialog tree object
			//how to get sprite id?
			dialogTreeMap.put(Integer.toString(spriteId), dialogTree);
			spriteId++;
		}

		
		return dialogTreeMap;
	}
	
	
	//not fully implemented yet
	@SuppressWarnings({ "unchecked" })
	protected DialogTree deconstructDMTToDialogObject(DefaultMutableTreeNode topNode) {
		
		Enumeration<DefaultMutableTreeNode> children =  topNode.children();
		DefaultMutableTreeNode inputNode = children.nextElement();
		DefaultMutableTreeNode outputNode = children.nextElement();
		DefaultMutableTreeNode questNode = null;
		if( children.hasMoreElements()) {
			questNode = children.nextElement();
		}else {
			questNode= new DefaultMutableTreeNode("Quest");
		}
		DialogTree dialogTree = new DialogTree();
		
		//enumerate through input trees (each character input has a set of responses from the NPC) 
		//these are maps of String:String values that indicate a character statement, then a single npc response
		Enumeration<DefaultMutableTreeNode> inputChildren = inputNode.children();

		List<Map<String,String>> treeList = new ArrayList<Map<String,String>>();
		while(inputChildren.hasMoreElements()) {
			//loop through the dialogs in each tree
			DefaultMutableTreeNode dialogTreeNode = inputChildren.nextElement();
			
			Enumeration <DefaultMutableTreeNode> dialogTreeChildNodes = dialogTreeNode.children();
			Map<String, String> charValueRespMap = new HashMap<String, String>();
			while(dialogTreeChildNodes.hasMoreElements()) {
				
				DefaultMutableTreeNode child = dialogTreeChildNodes.nextElement();
								
				//the second child is response key
				DefaultMutableTreeNode respNode = (DefaultMutableTreeNode) child.getChildAt(1);
				
				String charKey = (String) child.getUserObject();
				String respValue = (String) respNode.getUserObject();
				
				charValueRespMap.put(charKey, respValue);
				
			}
			treeList.add(charValueRespMap);
		}
		 
		//enumerate through output trees
		//each output houses a response dialog key, that maps to a particular dialog tree. 
		//
		Enumeration<DefaultMutableTreeNode> outputChildren = outputNode.children();

		Map<String,String> responseMap = new HashMap<String,String>();
		while(outputChildren.hasMoreElements()) {
			//loop through the dialogs in each tree
			DefaultMutableTreeNode dialogTreeNode = outputChildren.nextElement();
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) dialogTreeNode.getChildAt(1);
			
			String respKey = (String) dialogTreeNode.getUserObject();
			String dialogTreeKey = (String) childNode.getUserObject();
			
			responseMap.put(respKey, dialogTreeKey);
		}
		
		Enumeration<DefaultMutableTreeNode> questChildren =  questNode.children();
		
		//enumerate through associated quests
		Map<String,String> questMap = new HashMap<String,String>();
		while(questChildren.hasMoreElements()) {
			//loop through the dialogs in each tree
			DefaultMutableTreeNode charValueNode = questChildren.nextElement();
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) charValueNode.getChildAt(1);
			
			String charKey = (String) charValueNode.getUserObject();
			String questId = (String) childNode.getUserObject();
			
			questMap.put(charKey, questId);
		}
		
		
		dialogTree.setInput( treeList );
		dialogTree.setOutput( responseMap );	
		dialogTree.setQuest(questMap);
		return dialogTree;
		
	}




	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DialogEditorSwingGui gui = new DialogEditorSwingGui();
            	gui.createAndShowGUI();
            }
        });
    }
}
