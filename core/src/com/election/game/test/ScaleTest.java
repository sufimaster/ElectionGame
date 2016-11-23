package com.election.game.test;



import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.election.game.MapSprite;
import com.election.game.TownMap;
import com.election.game.camera.OrthographicCameraMovementWrapper;
import com.election.game.render.SpriteAndTiledRenderer;

public class ScaleTest extends ApplicationAdapter {
	
	private static final float VIRTUAL_HEIGHT = 15;
	OrthographicCameraMovementWrapper cam;
	//OrthographicCamera cam;
	SpriteBatch batch;
	
	MapSprite texture;
	//Texture texture;
	
	float y;
	float gravity = -9.81f;
	float velocity;
	float jumpHeight=1f;
	
	TownMap townMap;
	SpriteAndTiledRenderer renderer;
	
	public void create(){
		batch = new SpriteBatch();
		texture = new MapSprite(new Texture("man.png"));
		townMap = new TownMap("maps/house1.tmx", false);
		
		
		cam = new OrthographicCameraMovementWrapper();
		//cam = new OrthographicCamera();
		renderer = new SpriteAndTiledRenderer(townMap, cam, 1f/8f);
		
	}
	
	public void render(){
		
		if( Gdx.input.justTouched()){
			y+= jumpHeight;
		}
		
		float delta = Math.min(1/10f, Gdx.graphics.getDeltaTime());
		velocity += gravity * delta;
		y+= velocity * delta;
		if( y<=0){
			y= velocity = 0;
		}
		
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderer.render();
		
		//cam.update();
		batch.begin();
		batch.draw(texture.textureRegion, 0, y, 1.8f, 1.8f);
		batch.end();
		
	}
	
	public void resize(int width, int height){
		cam.setToOrtho(false, VIRTUAL_HEIGHT *width/(float)height, VIRTUAL_HEIGHT);
		batch.setProjectionMatrix(cam.source.combined);
	}
	
	public void dispose(){
		//texture.dispose();
		batch.dispose();
	}
	

}
