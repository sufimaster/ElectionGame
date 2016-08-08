package com.election.game.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.election.game.ElectionGame;
import com.election.game.camera.OrthographicCameraMovementWrapper;

public class DebugRenderer {
	private static ShapeRenderer debugRenderer = new ShapeRenderer();

    public static void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    public static void DrawDebugLine(Vector2 start, Vector2 end, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(2);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.WHITE);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }
    
    public static void DrawDebugCameraScrollBounds( OrthographicCameraMovementWrapper cam )
    {
    
    	
    	if( ElectionGame.GAME_OBJ.isdebug ){
			
		    Gdx.gl.glLineWidth(1);
		    debugRenderer.setProjectionMatrix(cam.source.combined);
		    debugRenderer.begin(ShapeRenderer.ShapeType.Line);
		    debugRenderer.setColor(Color.RED);
		    debugRenderer.rect( cam.boundsRect.x, cam.boundsRect.y, cam.boundsRect.width, cam.boundsRect.height);
	    
		    //line(start, end);
		    debugRenderer.end();
		    Gdx.gl.glLineWidth(1);        
    	}
    	
    }
    
    
    public static void DrawDebugRect(Rectangle rect, OrthographicCamera cam )
    {
    	if( ElectionGame.GAME_OBJ.isdebug ){
	        Gdx.gl.glLineWidth(1);        
	        debugRenderer.setProjectionMatrix(cam.combined);
	        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
	        debugRenderer.setColor(Color.RED);
	        debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
	        
	        //line(start, end);
	        debugRenderer.end();
	        Gdx.gl.glLineWidth(1);
    	}
    	
    }
    
}
