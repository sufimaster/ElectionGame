package com.election.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.election.game.camera.OrthographicCameraMovementWrapper;
import com.election.game.render.DebugRenderer;
import com.election.game.sprites.Candidate;

public class DebugInfo {

	private ShapeRenderer shapeRender;
	private SpriteBatch batch;

	boolean debugCollisions=false;
	boolean debugText=true;
	boolean debugRegions=true;
	boolean debugCamera=false;
	boolean debugCandidate=true;
	

	public DebugInfo(){
		
		shapeRender = new ShapeRenderer();
		shapeRender.setAutoShapeType(true);
		shapeRender.setColor(Color.RED);
		
		batch = new SpriteBatch(); 
	}
	
	public void drawDebugText(OutsideScreen screen){
		
		if(!debugText){
			return;
		}
		
		
		ElectionGame.GAME_OBJ.hudBatch.setProjectionMatrix(screen.hudCam.combined);
		ElectionGame.GAME_OBJ.hudBatch.begin();		
		
		Region region = screen.getRegion( (int)screen.candidate.getX(), (int) screen.candidate.getY());
		
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "FPS:" + Gdx.graphics.getFramesPerSecond(), 10, screen.hudCam.viewportHeight - 60 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "STATE:" + ElectionGame.GAME_OBJ.state, 10, screen.hudCam.viewportHeight - 76 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Candidate Pos: [" + screen.candidate.getX() + ", " + screen.candidate.getY() + "]", 10, screen.hudCam.viewportHeight - 92 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Candidate Region: [" + region.xLoc + ", " + region.yLoc + "]", 10, screen.hudCam.viewportHeight - 108 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Camera Position: [" + screen.worldCam.source.position.x + ", " + screen.worldCam.source.position.y + "]", 10, screen.hudCam.viewportHeight - 125 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Mouse Position: [" + screen.mousePos.x + ", " + screen.mousePos.y + "]", 10, screen.hudCam.viewportHeight - 141 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Mouse Region: [" + screen.mouseRegion.x + ", " + screen.mouseRegion.y + "]", 10, screen.hudCam.viewportHeight - 155 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Alpha transition: [" + screen.alphaBlend + "]", 10, screen.hudCam.viewportHeight - 169 );

		ElectionGame.GAME_OBJ.hudBatch.end();		
		
	}
	
	public void drawRegions(Region[][] regions, OrthographicCameraMovementWrapper worldCam, Vector2 mouseRegion, float scale){
		
		if(!debugRegions){
			return;
		}
		
		/*shapeRender.setProjectionMatrix(worldCam.source.combined);
		shapeRender.begin();
		shapeRender.set(ShapeType.Line);

		shapeRender.setColor(Color.CORAL);*/
		for (int i=0; i<regions.length; i++){
			for (int j=0; j<regions[i].length; j++){
				
				Region region =regions[i][j];
				
				if( mouseRegion.x == i && mouseRegion.y == j){
					
					//draw selection rectangle
					shapeRender.setColor(Color.RED);
					shapeRender.rect( region.rect.x + .01f, region.rect.y+ .01f, region.rect.width-.03f, region.rect.height-.03f);

					
					//draw region borders					
					//shapeRender.setColor(Color.WHITE);
					//shapeRender.rect( region.rect.x, region.rect.y, region.rect.width, region.rect.height);
					
					
					//draw boundary rects of electors in selected region
					for (Electorate elector: region.electorsInRegion ) {
						Rectangle boundRect =elector.sprite.getBoundingRectangle(); 

						shapeRender.rect( boundRect.x, boundRect.y, boundRect.x, boundRect.y, boundRect.width, boundRect.height,scale, scale, 0f );

						
					}
					
					
				}
					
				//just draw region borders
				shapeRender.setColor(Color.WHITE);
				shapeRender.rect( region.rect.x, region.rect.y, region.rect.width, region.rect.height);
				
			}	
		}
		
		/*shapeRender.end();*/
		
	}
	
	
	public void drawMapObjects(MapObjects mapObjects, float scale, OrthographicCameraMovementWrapper cam){
		
		if(!debugCollisions){
			return;
		}
		
		/*shapeRender.setProjectionMatrix(cam.source.combined);
		shapeRender.begin();
		shapeRender.set(ShapeType.Line);
		shapeRender.setColor(Color.RED);*/
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
/*		shapeRender.end();
*/				
	}
	
	
	
	/*
	 * TODO: This method uses hudCam - needs to draw using the worldCam
	 * supposed to draw map object names
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
	
	public void drawCandidateBounds(Candidate candidate, OrthographicCameraMovementWrapper worldCam){
		
		if(!debugCandidate){
			return;
		}
		
		shapeRender.rect( candidate.getBoundingRectangle().x, candidate.getBoundingRectangle().y, candidate.getBoundingRectangle().width, candidate.getBoundingRectangle().height);

		
	}
	
	public void drawCameraBounds(OrthographicCameraMovementWrapper worldCam){
		
		if( !debugCamera){
			return;
		}
				
		DebugRenderer.DrawDebugCameraScrollBounds( worldCam);
	}

	public void render(OutsideScreen screen) {

		if(!ElectionGame.GAME_OBJ.isdebug){
			return;
		}
		
		
		//draw debug text
		drawDebugText(screen);
		//draw camera scroll bouds		
		drawCameraBounds( screen.worldCam);
						

		//these next debug methods all use shape renderer so just call begin/end once
		shapeRender.setProjectionMatrix(screen.worldCam.source.combined);
		shapeRender.begin();
		shapeRender.set(ShapeType.Line);
		shapeRender.setColor(Color.RED);
				
				
		//draw map collision objects
		drawMapObjects(screen.tileMap.getAllMapObjects(), screen.mapRenderer.getUnitScale(), screen.worldCam);
		
		//draw each region

		drawRegions(screen.currentTownMap.regions, screen.worldCam, screen.mouseRegion, screen.mapRenderer.getUnitScale());


		//draw cnadidate location
		drawCandidateBounds(screen.candidate, screen.worldCam);
		
		
		shapeRender.end();
		


	}
	
	
}
