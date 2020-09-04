package com.election.game.render;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.election.game.Constants;

import com.election.game.Electorate;
import com.election.game.camera.OrthographicCameraMovementWrapper;
import com.election.game.maps.TownMap;
import com.election.game.sprites.Candidate;

public class SpriteAndTiledRenderer extends OrthogonalTiledMapRenderer {

	private Texture debugTex;
	private OrthographicCamera cam;
	public int drawBoundsafterLayer = 5;
	public int drawSpritesAfterLayer = 5;
	private Candidate candidate;
	private ArrayList<Electorate> sprites;
	
	public SpriteAndTiledRenderer(TownMap map, OrthographicCameraMovementWrapper cam, float unitScale) {
		super(map.tiledMap, unitScale);
		this.map = map.tiledMap;
		this.cam = cam.source;
		
		sprites = new ArrayList<Electorate>();
		
		debugTex = new Texture(Gdx.files.internal("debughash.png"));
		
	}
	
	public void setCandidate(Candidate c){
		this.candidate = c;
	}
	
	
	public void resetMap(TownMap map, float unitScale){
		this.map = map.tiledMap;
		this.unitScale = unitScale;
	}
	
	
	@Override
	public void render(){

		int currentLayer=0;
		beginRender();
	
		for(MapLayer layer: map.getLayers()){
			if(layer.isVisible()){
				if( layer instanceof TiledMapTileLayer){
					
					renderTileLayer((TiledMapTileLayer) layer);
					currentLayer++;
				}else {
					currentLayer++;

					for(MapObject object: layer.getObjects()){
						renderObject(object);
					}
				}
				
				
				if(currentLayer == drawSpritesAfterLayer ||
						(currentLayer == map.getLayers().getCount()-1)   ){
					
					/*	for (Electorate elector : sprites) {
						
						ElectionGame.GAME_OBJ.debugFont.draw(this.getBatch(), "id" + elector.id, elector.sprite.getX(), elector.sprite.getY() );
						
						elector.sprite.draw(this.getBatch());

					}*/
					
					//draw candidate						
					//candidate.draw(this.getBatch());
					candidate.draw(this.getBatch());
				}
			}
		}	
		
		endRender();
				
	}
	
	@Override
	public void renderObject(MapObject object){
		 
		if(object instanceof TextureMapObject) {
			TextureMapObject textureObj = (TextureMapObject) object;
	        //batch.draw(textureObj.getTextureRegion(), textureObj.getX(), textureObj.getY());
	        
	         batch.draw(textureObj.getTextureRegion(), textureObj.getX(), textureObj.getY(), 
	        		    textureObj.getX()/2, textureObj.getY()/2, 
	        		    textureObj.getTextureRegion().getRegionWidth(), 
	        		    textureObj.getTextureRegion().getRegionHeight(),
	        		    unitScale, unitScale, 0);
	        		    
	         
		 }
		 		 
	}
	

	public void setSprites(ArrayList<Electorate> electorate) {
		sprites = electorate;
	}
	
	

}
