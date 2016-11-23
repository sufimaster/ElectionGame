package com.election.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Utilities {

	
	
	
	public static void relativeVector(Vector2 vec, OrthographicCamera c){

		
		vec.set(vec.x+c.position.x-c.viewportWidth/2,  vec.y+c.position.y-c.viewportHeight/2);
		
		//return relativeVector( vec.x, vec.y, c);
		
		
	}
	
	
	public static Rectangle scaleRectangle( Rectangle rect, float scale){
		
		
		
		
		Rectangle newRect  = new Rectangle( rect);
		
		newRect.set( rect.x * scale, rect.y * scale, rect.getWidth() * scale, rect.getHeight() * scale );
		
		return newRect;
		
	}
	
	
	
}
