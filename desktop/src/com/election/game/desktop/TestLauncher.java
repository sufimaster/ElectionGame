package com.election.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.election.game.dialog.DialogHandlerScene2dUI;
import com.election.game.test.ScaleTest;

public class TestLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Libgdx Tester";
		config.width = 800;
		config.height = 600;
		//new LwjglApplication(new DialogHandlerScene2dUI(), config);
		new LwjglApplication(new ScaleTest(), config);
	}
}
