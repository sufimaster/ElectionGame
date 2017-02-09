package com.election.game.dialog;

import java.util.List;
import java.util.Map;

public class DialogTree {

	
	int id;
	

	List< Map<String, String>> input;
	
	
	
	//Map<String, String> input;
	Map<String, String> output;
	
	Map<String, String> quest;
	
	
	public int getId() {
		return id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	
	public List< Map<String, String>> getInput() {
		return input;
	}
	
	public void setInput(List< Map<String, String>> input) {
		this.input = input;
	}
	
	public Map<String, String> getOutput() {
		return output;
	}
	
	public void setOutput(Map<String, String> output) {
		this.output = output;
	}
	

}
