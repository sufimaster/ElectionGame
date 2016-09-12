package com.election.game.render;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.election.game.Electorate;
import com.election.game.sprites.Candidate;

public class SpriteAndTiledRenderer extends OrthogonalTiledMapRenderer {

	private OrthographicCamera cam;
	public int drawBoundsafterLayer = 5;
	public int drawSpritesAfterLayer = 5;
	private Candidate candidate;
	private ArrayList<Electorate> sprites;
	
	public SpriteAndTiledRenderer(TiledMap map, OrthographicCamera cam) {
		super(map);
		this.map = map;
		this.cam = cam;

	}
	
	public void setCandidate(Candidate c){
		this.candidate = c;
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
					
					if(currentLayer == drawSpritesAfterLayer){
						
						for (Electorate elector : sprites) {
							
							elector.sprite.draw(this.getBatch());

						}
						
						//draw candidate						
						candidate.sprite.draw(this.getBatch());
					}
					
					
					
					
				}else {
					
					for(MapObject object: layer.getObjects()){
						renderObject(object);
					}
				}
			}
				
			
		}
		
		
		
		
		endRender();
				
	}

	public void setSprites(ArrayList<Electorate> electorate) {
		sprites = electorate;
	}
	
	

}
