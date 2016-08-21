package com.election.game;

import java.util.ArrayList;
import java.util.List;

public class Region {
	
	List<Electorate> electorsInRegion;
	
	int xLoc,yLoc;

	public Region(int xLoc, int yLoc) {
		super();
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		
		electorsInRegion = new ArrayList<Electorate>();
	}
	
	public void addElectors(Electorate elector){
		
		
		electorsInRegion.add(elector);
		
	}
	

}
