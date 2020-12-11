package com.election.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class HudComponent {

	Stage stage;
	public Skin skin;
	String title;
	BitmapFont font;
	Window window;
	public Table contentTable;
	public Vector2  position;
	public Vector2  size;
	public boolean debug = false;
	public boolean visible = false;
	
	
	public HudComponent(String title, BitmapFont font, Skin skin, Vector2 position, Vector2 size, boolean debug, boolean visible){
		
		this.title = title;
		this.font = font;

		this.skin = skin;
		stage = new Stage();

		this.position = position;
		this.size = size;
		this.debug = debug;
		this.visible = visible;
		createHud();
		
	}
	
	public HudComponent(String title, BitmapFont font, Skin skin, float winPosX, float winPosY, int width, int height, boolean debug, boolean visible){
		
		this.title = title;
		this.font = font;

		this.skin = skin;
		stage = new Stage();

		this.position = new Vector2(winPosX, winPosY);
		this.size = new Vector2(width, height);
		this.debug = debug;
		this.visible = visible;
		createHud();
		
	}

	public void setBackground(TextureRegion background) {
		window.setBackground(new TextureRegionDrawable(background));
	}
	
	private void createHud() {
		window = new Window(title, skin);
		window.debug();

		window.setSize(size.x, size.y);
		//dialogDisplay.setDebug(true);
		window.setPosition( position.x, position.y );
		stage.addActor(window);
				
		contentTable = new Table();
		if(this.debug){ 
			contentTable.debug();
		}else{
			contentTable.debug(Debug.none);
		}
		
		contentTable.top().left();		
		
		final ScrollPane scroll = new ScrollPane(contentTable, skin);
		scroll.setFadeScrollBars(false);
		
		
		window.add(scroll).expand().fill().colspan(4);
		window.row().space(10).padBottom(10);
		
	}
	
	
	public void draw(float delta){			
		
		stage.act(Gdx.graphics.getDeltaTime());
		
		if(!visible) return;

		
		stage.draw();	
		
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	
}
