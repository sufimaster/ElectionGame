package com.election.game;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Utilities {

	
	public static boolean isCollision(Polygon p, Rectangle r) {
	    Polygon rPoly = new Polygon(new float[] { 0, 0, r.width, 0, r.width,
	            r.height, 0, r.height });
	    rPoly.setPosition(r.x, r.y);
	    if (Intersector.overlapConvexPolygons(rPoly, p))
	        return true;
	    return false;
	}
	
	public static void relativeVector(Vector2 vec, OrthographicCamera c){

		
		vec.set(vec.x+c.position.x-c.viewportWidth/2,  vec.y+c.position.y-c.viewportHeight/2);
		
		//return relativeVector( vec.x, vec.y, c);
		
		
	}
	
	
	public static Rectangle scaleRectangle( Rectangle rect, float scale){
		
		
		
		
		Rectangle newRect  = new Rectangle( rect);
		
		newRect.set( rect.x * scale, rect.y * scale, rect.getWidth() * scale, rect.getHeight() * scale );
		
		return newRect;
		
	}
	
	
	public static String fileToString(Path path){
		
		//Path file = Paths.get("E:/Projects/libgdx/electionDay/android/assets", "dialogTrees.json");
	    Charset charset = Charset.forName("ISO-8859-1");
		final String EoL = System.getProperty("line.separator");

	    
	    
	    List<String> lines = new ArrayList<String>();
	    StringBuilder builder = new StringBuilder();
		try {
		      lines = Files.readAllLines(path, charset);

		      for (String line : lines) {
		    	  builder.append(line).append(EoL);
		      }
		}catch (IOException e) {
			
		      System.out.println(e);
		}
		
		return builder.toString();
		
	}
	
}
