package com.election.game.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.IntSet;

public class KeyInputProcessor extends InputAdapter{
	private final IntSet downKeys = new IntSet(20);

	@Override
	public boolean keyDown(int keycode) {
		downKeys.add(keycode);
        if (downKeys.size >= 2){
            //onMultipleKeysDown(keycode);
        }
        return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		downKeys.remove(keycode);
        return true;
	}

	

}
