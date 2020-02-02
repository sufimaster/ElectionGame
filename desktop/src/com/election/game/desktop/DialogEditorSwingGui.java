package com.election.game.desktop;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.election.game.Utilities;
import com.election.game.dialog.DialogContainer;
import com.election.game.dialog.DialogModel;
import com.election.game.dialog.DialogTree;
import com.election.game.dialog.editor.DialogTableModel;
import com.election.game.json.JsonParser;
import com.google.gson.Gson;

public class DialogEditorSwingGui {
	
	
	private DialogContainer container;
	private JFrame frame;
	private JPanel content;
	private JsonParser parser;
	private DefaultTreeModel masterTreeModel;

	public DialogEditorSwingGui(){
	
				
		parser = new JsonParser();
		

		String dialogTreesContent = Utilities.fileToString (	Paths.get("C:/Users/ayan_/Documents/Programming/libGDX/ElectionGame/android/assets/data/dialog", "dialog_trees.json")  );
		
		String dialogLinesContent  = Utilities.fileToString (	Paths.get("C:/Users/ayan_/Documents/Programming/libGDX/ElectionGame/android/assets/data/dialog", "dialog_lines.json")  );
		
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
				System.out.println("event: " + e.getActionCommand());
				System.out.println("Selected " + e.getSource());
				
				generateDialogLinesTable();
			}
		});
		
		menu.add(dialogLinesMenuItem);
		
		//add sub-menu for sprite dialog trees
		JMenu dialogTreesMenu = new JMenu("Sprite Dialog Trees");
		menu.add(dialogTreesMenu);
		
		for ( String spriteId : container.getDialogTrees().keySet()) {
			JMenuItem menuItem = new JMenuItem("Sprite ID " + spriteId);
			
			//set custom property for each menu item, so listener can dynamically generate content
			menuItem.putClientProperty("spriteId", spriteId);
			
			menuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String spriteId = (String) ((JMenuItem)e.getSource()).getClientProperty("spriteId");
					System.out.println("event: " + e.getActionCommand());
					System.out.println("Selected menu for spriteId " + spriteId);
				    
					
					content.removeAll();
				  					
					//update content pane when menu item selection is done 
					//add label for selected sprite 
					JLabel spriteLabel =  new JLabel("Dialog Trees for Sprite Id " + spriteId);
					  
					content.add(spriteLabel); 
					
					//DOESNT WORK properly YET
					generateSpriteDialogTrees(spriteId);
					  
					//display dialog trees for selected Sprite
					//generateSpriteInputTreesPane(spriteId);
					  
					//create seperate table for viewing the sprite responses, //and how they map
					//to character dialog trees 
					//generateSpriteOutputTreesPane(spriteId); 
					
				}
				 
			});
			dialogTreesMenu.add(menuItem);
		}
		
		frame.setJMenuBar(menuBar);
	}

	protected void generateDialogLinesTable() {
		
		content.removeAll();

		JLabel dialogLabel =  new JLabel("Dialog Lines for Game");
		  
		content.add(dialogLabel); 
				
		JTable dialogTable = new JTable(new DialogTableModel(container.getDialogs()));
		
		content.add(new JScrollPane(dialogTable));
		
		JButton saveButton = new JButton("Save Dialog");
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				Writer writer = null;
				try {
					writer = new FileWriter(new File("C:/Users/ayan_/Documents/Programming/libGDX/ElectionGame/android/assets/data/dialog/test.json"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				parser.serializeDialogLines(container.getDialogs(), writer );
			}
		});
		
		content.add(saveButton);
		frame.pack();
		
		
	}
	
	protected void deconstructDMTToDialogObject(DefaultMutableTreeNode topNode) {
		
		Enumeration<DefaultMutableTreeNode> children = topNode.children();
		DefaultMutableTreeNode inputNode = children.nextElement();
		DefaultMutableTreeNode outputNode = children.nextElement();
		
		DialogTree dialogTree = new DialogTree();
		dialogTree.setInput((List<Map<String, String>>) inputNode);
		dialogTree.setOutput((Map<String, String>) outputNode);	
		
	}
	
	
	protected void generateSpriteDialogTrees(String spriteId) {
		content.removeAll();

		
		DialogTree dialogTrees = container.getDialogTrees().get(spriteId);
		List<Map<String, String>> inputList = dialogTrees.getInput();

		DefaultMutableTreeNode topNode = new DefaultMutableTreeNode("Sprite Id: " + spriteId + " Dialog Trees");
		
		if( masterTreeModel== null) {
			masterTreeModel = new DefaultTreeModel(topNode);	
		}else {
			masterTreeModel.setRoot(topNode);
		}
		
		JTree tree = new JTree(masterTreeModel);
		
		tree.setEditable(true);
	    JScrollPane treeView = new JScrollPane(tree);

		DefaultMutableTreeNode inputNode = new DefaultMutableTreeNode("Input");
		
		int idx=0;
		for (Map<String, String> dialogResponseMap : inputList) {
			DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("tree: " + idx);
			for (String charDialogKey : dialogResponseMap.keySet()) {
				String respKey = dialogResponseMap.get(charDialogKey);
				
				DefaultMutableTreeNode charkeyDialogNode = new DefaultMutableTreeNode(charDialogKey);
				DefaultMutableTreeNode charValueDialogNode = new DefaultMutableTreeNode(container.getDialogs().get(charDialogKey).getValue());
				DefaultMutableTreeNode respDialogNode = new DefaultMutableTreeNode(respKey+ ":" + container.getDialogs().get(respKey).getValue());
				
				charkeyDialogNode.add(charValueDialogNode);
				charkeyDialogNode.add(respDialogNode);
				treeNode.add(charkeyDialogNode);
			}
			inputNode.add(treeNode);			
			idx++;
		}
		
		Map<String, String> outputMap = dialogTrees.getOutput(); 
		DefaultMutableTreeNode outputNode = new DefaultMutableTreeNode("Output");

		for (String charKey : outputMap.keySet() ) {
			String respKey = outputMap.get(charKey);

			DefaultMutableTreeNode charKeyNode = new DefaultMutableTreeNode(charKey);
			DefaultMutableTreeNode charValueNode = new DefaultMutableTreeNode(container.getDialogs().get(charKey).getValue());
			DefaultMutableTreeNode respDialogNode = new DefaultMutableTreeNode(respKey);

			charKeyNode.add(charValueNode);
			charKeyNode.add(respDialogNode);
			outputNode.add(charKeyNode);
		}


		topNode.add(inputNode);
		topNode.add(outputNode);
		content.add(treeView);
		
		JButton saveButton = new JButton("Save Dialog Tree ");
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) masterTreeModel.getRoot();
				
				//convert treeNode into Map<String, DialogTree>
				//
				//
				Map<String, DialogTree> dialogTree = (Map<String, DialogTree>) rootNode.getUserObject();
				
				
				
				Writer writer = null;
				try {
					writer = new FileWriter(new File("C:/Users/ayan_/Documents/Programming/libGDX/ElectionGame/android/assets/data/dialog/test_DialogTree.json"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				parser.serializeDialogTrees(dialogTree, writer);
				
			}
		});
		
		content.add(saveButton);
		
		
		frame.pack();

	}

	protected void generateSpriteInputTreesPane(String spriteId) {

		//create a structure to hold the dialog options
		//the root structure is the frame content pane.
		//After that, a JPanel content is added to the content pane
		//Then I add a  Jpanel for each dialog tree . Ecah JPanel contains a JTable
		//that includes the mapping from each dialog a character can select 
		//to a corresponding sprite response. 
		
		DialogTree dialogTrees = container.getDialogTrees().get(spriteId);
		List<Map<String, String>> inputList = dialogTrees.getInput();
		
		JLabel lbl = new JLabel("Input List:");
		content.add(lbl);
		
		//JPanel inputPanel = new JPanel();
		
		int charTreeId = 0;
		for (Map<String, String> map : inputList) {
			// create object of table and table model
			JTable inputListHolder = new JTable();
			DefaultTableModel dtm = new DefaultTableModel(0, 0);
			 
			//Create col headers
			String headers[] = new String [] {"Character Input", "Sprite Response"};
			
			//add header in model
			dtm.setColumnIdentifiers(headers);
			
			//set model into table object
			inputListHolder.setModel(dtm);
			
			JPanel treePanel = new JPanel();
		    treePanel.setLayout(new BoxLayout(treePanel, BoxLayout.PAGE_AXIS));
		    
		    JLabel label = new JLabel("Tree: " + charTreeId);
		    treePanel.add(label);
			treePanel.add(inputListHolder);
			content.add(treePanel);
			
			for (String key : map.keySet()) {	
				
				//the key is the key to the character dialog line
				//the restpKey is the key to the npc response dialog line
				String respKey = map.get(key);
				
				//inputTextFields.put(key, container.getDialogs().get(key).getValue());
				
				Object[] rowObject = new Object[] { key + ":" + container.getDialogs().get(key).getValue(), respKey + ":" + container.getDialogs().get(respKey).getValue() };
				
				dtm.addRow(rowObject);
			}
			
			charTreeId++;
		}
		
	}

	protected void generateSpriteOutputTreesPane(String spriteId) {

		DialogTree dialogTrees = container.getDialogTrees().get(spriteId);

		Map<String, String> outputList = dialogTrees.getOutput();
		
		JLabel lbl2 = new JLabel("Output List:");
		content.add(lbl2);
		
		 // create object of table and table model
		JTable outputListHolder = new JTable();
		DefaultTableModel dtm2 = new DefaultTableModel(0, 0);
		 
		//Create col headers
		String headers2[] = new String [] {"Sprite Response", "Character Response Choices Set"};
		
		//add header in model
		dtm2.setColumnIdentifiers(headers2);
		
		//set model into table object
		outputListHolder.setModel(dtm2);
		content.add(outputListHolder);

		for( String key: outputList.keySet()) {
			
			String charRespSetKey = outputList.get(key);
			dtm2.addRow(new Object[] {key +":" + container.getDialogs().get(key).getValue(), "response tree: "+ charRespSetKey});
		}

		frame.pack();
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
