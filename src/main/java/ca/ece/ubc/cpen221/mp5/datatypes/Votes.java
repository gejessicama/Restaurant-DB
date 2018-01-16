package ca.ece.ubc.cpen221.mp5.datatypes;

public class Votes {
	private int cool = 0;
	private int useful = 0;
	private int funny = 0;

	/*
	 * Abstraction Function: a record of the number of votes for what type of vote
	 * 
	 * Rep Invariant: all fields are non negative integers
	 */

	public void addCool() {
		cool++;
	}

	public void addFunny() {
		funny++;
	}

	public void addUseful() {
		useful++;
	}

	public int getCool() {
		return cool;
	}

	public int getUseful() {
		return useful;
	}

	public int getFunny() {
		return funny;
	}
}