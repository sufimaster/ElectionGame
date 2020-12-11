package com.election.game;

public class BattleAttributes {

	private static final int INFLUENCE_MAX = 10;
	private static final int INTELLECT_MAX = 10;
	private static final int PERSUARION_MAX = 10;


	/*
	 * TODO: ad some variables or a matrix for attributes relating to each
	 * person's voting reasons
	 */
	public int influence;
	public int intellect;
	public int persuasion;	
	public int hp = 20;
	
	public BattleAttributes() {
		this.influence = ElectionGame.randGen.nextInt(INFLUENCE_MAX);
		this.intellect = ElectionGame.randGen.nextInt(INTELLECT_MAX);
		this.persuasion = ElectionGame.randGen.nextInt(PERSUARION_MAX);

	}
	public BattleAttributes(int influence, int intellect, int persuasion) {
		super();
		this.influence = influence;
		this.intellect = intellect;
		this.persuasion = persuasion;
	}
	
	public int getInfluence() {
		return influence;
	}
	public void setInfluence(int influence) {
		this.influence = influence;
	}
	public int getIntellect() {
		return intellect;
	}
	public void setIntellect(int intellect) {
		this.intellect = intellect;
	}
	public int getPersuasion() {
		return persuasion;
	}
	public void setPersuasion(int persuasion) {
		this.persuasion = persuasion;
	}
	

}
