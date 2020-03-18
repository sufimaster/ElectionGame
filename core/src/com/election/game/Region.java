package com.election.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;

public class Region {
	
	List<Electorate> electorsInRegion;
	
	int xLoc,yLoc;
	Rectangle rect;
	
	
	
	public Region(int xLoc, int yLoc) {
		super();
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		
		rect = new Rectangle(xLoc, yLoc, 1, 1);
		
		electorsInRegion = new ArrayList<Electorate>();
	}
	
	public void addElectors(Electorate elector){
		
		if( !electorsInRegion.contains(elector)){
			electorsInRegion.add(elector);
		}
	}
	

}
