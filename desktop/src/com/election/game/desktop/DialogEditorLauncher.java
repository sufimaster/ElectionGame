package com.election.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.election.game.dialog.editor.DialogGUIEditor;

public class DialogEditorLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Election Day";
		config.width = 800;
		config.height = 600;
		new LwjglApplication(new DialogGUIEditor(), config);
	}
}
