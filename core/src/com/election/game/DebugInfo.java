package com.election.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.election.game.camera.OrthographicCameraMovementWrapper;
import com.election.game.sprites.Candidate;

public class DebugInfo {

	private ShapeRenderer shapeRender;
	private SpriteBatch batch;


	public DebugInfo(){
		
		shapeRender = new ShapeRenderer();
		shapeRender.setAutoShapeType(true);
		shapeRender.setColor(Color.RED);
		
		batch = new SpriteBatch(); 
	}
	
	public void draw(Candidate candidate, OrthographicCameraMovementWrapper camera, OrthographicCamera hudCam, OutsideScreen outsideScreen){
		
		
		
		if(!ElectionGame.GAME_OBJ.isdebug){
			return;
		}
		
		
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "FPS:" + Gdx.graphics.getFramesPerSecond(), 10, hudCam.viewportHeight - 60 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "STATE:" + ElectionGame.GAME_OBJ.state, 10, hudCam.viewportHeight - 76 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Candidate Pos: [" + candidate.getX() + ", " + candidate.getY() + "]", 10, hudCam.viewportHeight - 92 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Camera Position: [" + camera.source.position.x + ", " + camera.source.position.y + "]", 10, hudCam.viewportHeight - 108 );
		

		
		
	}
	
	public void draw(MapSprite candidate, OrthographicCamera camera, OutsideScreen outsideScreen){
		
		
		
		if(!ElectionGame.GAME_OBJ.isdebug){
			return;
		}
		
		
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "FPS:" + Gdx.graphics.getFramesPerSecond(), 10, camera.viewportHeight - 60 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "STATE:" + ElectionGame.GAME_OBJ.state, 10, camera.viewportHeight - 76 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Candidate Pos: [" + candidate.getX() + ", " + candidate.getY() + "]", 10, camera.viewportHeight - 92 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Camera Position: [" + camera.position.x + ", " + camera.position.y + "]", 10, camera.viewportHeight - 108 );
		

		
		
	}
	
	
	public void draw(Candidate candidate, OrthographicCamera camera, OutsideScreen outsideScreen){
		
		
		
		if(!ElectionGame.GAME_OBJ.isdebug){
			return;
		}
		
		
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "FPS:" + Gdx.graphics.getFramesPerSecond(), 10, camera.viewportHeight - 60 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "STATE:" + ElectionGame.GAME_OBJ.state, 10, camera.viewportHeight - 76 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Candidate Pos: [" + candidate.getX() + ", " + candidate.getY() + "]", 10, camera.viewportHeight - 92 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Camera Position: [" + camera.position.x + ", " + camera.position.y + "]", 10, camera.viewportHeight - 108 );
	
	}
	
	public void drawMapObjects(MapObjects mapObjects, float scale, OrthographicCameraMovementWrapper cam){
		shapeRender.setProjectionMatrix(cam.source.combined);
		shapeRender.begin();
		for (MapObject mapObject : mapObjects) {
			
			if( mapObject instanceof RectangleMapObject){
			
				RectangleMapObject rectObj = (RectangleMapObject ) mapObject;
				Rectangle rect = rectObj.getRectangle(); //Utilities.scaleRectangle(rectObj.getRectangle(), scale);
								
				shapeRender.rect(rect.x, rect.y, rect.width, rect.height);
			
			}else if( mapObject instanceof PolygonMapObject){
				
				PolygonMapObject polyObj = (PolygonMapObject) mapObject;
				Polygon poly = polyObj.getPolygon();
				
				//poly.setScale(scale, scale);
				
				shapeRender.polygon(poly.getVertices());
				
				
			}
			
		}
		shapeRender.end();
				
	}
	
	/*
	 * TODO: This method uses hudCam - needs to draw using the worldCam
	 */
	public void drawMapObjectNames(MapObjects mapObjects, float scale, OrthographicCameraMovementWrapper cam){

		batch.setProjectionMatrix(cam.source.combined);
		batch.begin();
		for (MapObject mapObject : mapObjects) {
			
			if( mapObject instanceof RectangleMapObject){
			
				RectangleMapObject rectObj = (RectangleMapObject ) mapObject;
				Rectangle rect = rectObj.getRectangle(); //Utilities.scaleRectangle(rectObj.getRectangle(), scale);
								
				ElectionGame.GAME_OBJ.debugFont.draw(batch, "Collider Name: " + rectObj.getName(), rect.x, rect.y+16);
			
			}else if( mapObject instanceof PolygonMapObject){
				
				PolygonMapObject polyObj = (PolygonMapObject) mapObject;
				Polygon poly = polyObj.getPolygon();
				
				
				
			}
			
			
			
		}
		batch.end();
	}
	
	

	
}
