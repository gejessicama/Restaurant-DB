package ca.ece.ubc.cpen221.mp5.datatypes;

public class Business {

	protected String business_id;
	protected String name;
	protected String[] categories;

	/*
	 * Abstraction Function: a business with an ID, a name, and a set of categories
	 * it falls under
	 * 
	 * Rep Invariant: businessID and name are not null
	 */

	public Business() {
		business_id = "";
		name = "";
	}


}
