package ca.ece.ubc.cpen221.mp5.datatypes;

public class YelpUser extends User {

	private String url;
	private Votes votes = new Votes();
	private final String type = "user";
	private double average_stars = 0;

	/*
	 * Abstraction Function: a user with a url, votes given to the user's reviews, a
	 * count of the number of reviews the user has made, a type, an id, a name, the
	 * average number of stars that user has given
	 * 
	 * Rep Invariant: Fields are not null. review_count is non-negative. type is
	 * "user"
	 */

	/**
	 * Updates fields to correspond with the user adding a review with the given
	 * rating
	 * 
	 * @param rating
	 *            given by the user in the added review
	 */
	public void addReview(double rating) {
		average_stars = ((average_stars * review_count) + rating) / (review_count + 1);
		review_count++;
	}
	
	// SETTER METHODS

	public void setUserID(String ID) {
		user_id = ID;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	// GETTER METHODS
	
	public String getUserID() {
		return user_id;
	}

	public int getReviewCount() {
		return review_count;
	}

	public String getUrl() {
		return url;
	}

	public double getAverageStars() {
		return average_stars;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public Votes getVotes() {
		return votes;
	}
}
