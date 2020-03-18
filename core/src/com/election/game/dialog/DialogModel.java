package com.election.game.dialog;

public class DialogModel {

	public String id;
	public String value;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

		
	@Override
	public String toString(){
		
		return "\t" + id + ": " + value;
		
	}
	
}
