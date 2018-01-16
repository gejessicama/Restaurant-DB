package ca.ece.ubc.cpen221.mp5.datatypes;

public class YelpRestaurant extends Business {

	private boolean open = true;
	private String url="";
	private double longitude;
	private String[] neighborhoods;
	private String state;
	private String type = "business";
	private double stars;
	private String city;
	private String full_address;
	private int review_count = 0;
	private String photo_url = "";
	private String[] schools;
	private double latitude;
	private double price;

	private Point location;

	/*
	 * Abstraction Function: a Restaurant that is either currently open (true) or
	 * closed (false), has a url, a list of neighborhoods it is in, an id, a name, a
	 * list of categories it falls under, a US State it is in, a type of business, a
	 * star rating, a city it is is, a full address, the number of reviews written
	 * about it, a photo url, a list of universities it is nearby, a pricieness
	 * rating, and a latitude and longitude which together give its location
	 * 
	 * Rep Invariant: fields are not null. Both stars and price are between 1 and 5.
	 * State is a valid US State. categories and neighborhoods are not empty.
	 * review_count is non negative. latitude and longitude are valid latitude and
	 * longitude values and correspond with the point fields of location
	 */

	/**
	 * Updates the Restaurant to correspond to the added rating
	 * 
	 * @param rating rating given by the new review
	 */
	public void addReview(double rating) {
		stars = ((stars * review_count) + rating) / (review_count + 1);
		review_count++;
	}

	/**
	 * @param p a point to calculate the distance to
	 * @return the distance from this Restaurant to the given point
	 */
	public double distanceTo(Point p) {
		return location.distanceTo(p);
	}

	/**
	 * initializes the stored location value of the Restaurant
	 */
	public void setLocation() {
		location = new Point(latitude, longitude);
	}
	
	// SETTER METHODS
	
	public void setBusinessID(String ID) {
		super.business_id = ID;
	}
	
	public void setUrl(String URL) {
		url = URL;
	}
	
	// GETTER METHODS

	public Point getLocation() {
		return this.location;
	}


	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public String getBusinessID() {
		return business_id;
	}

	public String getName() {
		return name;
	}

	public String[] getCategories() {
		return categories;
	}

	public double getPrice() {
		return price;
	}

	public String[] getNeighborhoods() {
		return neighborhoods;
	}

	public double getStars() {
		return stars;
	}

	public int getReviewCount() {
		return review_count;
	}


}
