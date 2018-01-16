package ca.ece.ubc.cpen221.mp5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.ece.ubc.cpen221.mp5.datatypes.*;
import ca.ece.ubc.cpen221.mp5.antlr.*;

public class YelpDB implements MP5Db<YelpRestaurant> {

	private Map<String, YelpRestaurant> restaurantMap;
	private Map<String, YelpUser> userMap;
	private Map<String, YelpReview> reviewMap;

	// Only for Kmeans
	private Map<YelpRestaurant, Point> currentState;
	private List<YelpRestaurant> restaurantList;

	// Only for adding restaurants, users, reviews
	private long IDcount;

	// For convenience
	private Gson gson;
	private JsonParser parser;

	/*
	 * Abstraction Function: a database containing restaurants, users, and reviews
	 * which all have IDs and are contained in the respective restaurantMap,
	 * userMap, and reviewMap
	 * 
	 * Rep Invariant: no *Map is null
	 * 
	 * Additional Fields are for use in specified methods and are not the main
	 * representation of YelpDB
	 */

	/**
	 * Creator method for YelpDB. Initializes all fields and converts the contents
	 * of the given JSON files to the corresponding objects and maps
	 * 
	 * @param restaurantFile
	 *            json file name containing all the restaurants
	 * @param reviewFile
	 *            json file name containing all the reviews
	 * @param userFile
	 *            json file name containing all the users
	 * @throws FileNotFoundException
	 *             if the method cannot find files from the given file names
	 */
	public YelpDB(String restaurantFile, String reviewFile, String userFile) throws FileNotFoundException {

		gson = new Gson();
		parser = new JsonParser();

		IDcount = 0;

		restaurantMap = new HashMap<String, YelpRestaurant>();
		userMap = new HashMap<String, YelpUser>();
		reviewMap = new HashMap<String, YelpReview>();
		restaurantList = new ArrayList<YelpRestaurant>();

		Scanner restaurantScan = new Scanner(new File(restaurantFile));
		Scanner reviewScan = new Scanner(new File(reviewFile));
		Scanner userScan = new Scanner(new File(userFile));

		while (restaurantScan.hasNext()) {
			JsonObject obj = (JsonObject) parser.parse(restaurantScan.nextLine());
			YelpRestaurant yr = gson.fromJson(obj, YelpRestaurant.class);
			restaurantList.add(yr);
			yr.setLocation();
			restaurantMap.put(yr.getBusinessID(), yr);
		}

		while (reviewScan.hasNext()) {
			JsonObject obj = (JsonObject) parser.parse(reviewScan.nextLine());
			YelpReview yr = gson.fromJson(obj, YelpReview.class);
			reviewMap.put(yr.getReviewID(), yr);
		}

		while (userScan.hasNext()) {
			JsonObject obj = (JsonObject) parser.parse(userScan.nextLine());
			YelpUser yu = gson.fromJson(obj, YelpUser.class);
			userMap.put(yu.getUserID(), yu);
		}

		restaurantScan.close();
		reviewScan.close();
		userScan.close();
	}

	// BEGINNING OF SERVER FUNCTIONS FOR PART 4

	public String getRestNameFromId(String businessID) {
		return restaurantMap.get(businessID).getName();
	}

	public String addRestaurant(String s) {
		YelpRestaurant yr = gson.fromJson((JsonObject) parser.parse(s), YelpRestaurant.class);

		if (yr.getReviewCount() != 0 || yr.getName() == null || yr.getPrice() > 5 || yr.getPrice() < 1)
			return "ERR: INVALID_RESTAURANT_STRING";
		yr.setLocation();
		yr.setBusinessID(getNewID());
		yr.setUrl(newBusinessURL(yr.getBusinessID()));

		restaurantMap.put(yr.getBusinessID(), yr);
		restaurantList.add(yr);

		return gson.toJson(yr);
	}

	public String addUser(String s) {
		YelpUser user = gson.fromJson((JsonObject) parser.parse(s), YelpUser.class);

		if (user.getName() == null)
			return "ERR: INVALID_USER_STRING";

		user.setUserID(getNewID());
		user.setUrl(newUserURL(user.getUserID()));

		userMap.put(user.getUserID(), user);

		return gson.toJson(user);
	}

	public String addReview(String s) {
		YelpReview rev = gson.fromJson((JsonObject) parser.parse(s), YelpReview.class);

		if (rev.getStars() < 1 || rev.getStars() > 5)
			return "ERR: INVALID_REVIEW_STRING";

		else if (getRestaurant(rev.getRestaurantID()) == null)
			return "ERR: NO_SUCH_RESTAURANT";

		else if (getUser(rev.getUserID()) == null)
			return "ERR: NO_SUCH_USER";

		rev.setReviewID(getNewID());

		reviewMap.put(rev.getReviewID(), rev);
		restaurantMap.get(rev.getRestaurantID()).addReview(rev.getStars());
		userMap.get(rev.getUserID()).addReview(rev.getStars());

		return gson.toJson(rev);
	}

	private String getNewID() {
		return "GenID#" + Long.toString(++IDcount);
	}

	private String newBusinessURL(String id) {
		return "http://www.yelp.com/biz/" + id;
	}

	private String newUserURL(String id) {
		return "http://www.yelp.com/user_details?userid=" + id;
	}

	// END OF SERVER FUNCTIONS FOR PART 4

	// BEGIN ALL THE NECESSARY GETTER METHODS FOR YELPDB FIELDS

	public Set<YelpRestaurant> getRestaurants() {
		return new HashSet<YelpRestaurant>(restaurantMap.values());
	}

	public Set<YelpReview> getReviews() {
		return new HashSet<YelpReview>(reviewMap.values());
	}

	public Set<YelpUser> getUsers() {
		return new HashSet<YelpUser>(userMap.values());
	}

	public Map<String, YelpUser> getUserMap() {
		return new HashMap<String, YelpUser>(userMap);
	}

	public YelpRestaurant getRestaurant(String restID) {
		return restaurantMap.get(restID);
	}

//	public YelpReview getReview(String revID) {
	//	return reviewMap.get(revID);
	//}

	public YelpUser getUser(String userID) {
		return userMap.get(userID);
	}

	/**
	 * Should only be called after Kmeans has been run
	 * 
	 * @return a map of all YelpRestaurants to the nearest centroid, from the last
	 *         run of kMeans
	 */
	public Map<YelpRestaurant, Point> getCurrentState() {
		return new HashMap<YelpRestaurant, Point>(currentState);
	}
	// END NECESSARY GETTER METHODS

	@Override
	public Set<YelpRestaurant> getMatches(String queryString) {

		CharStream charStream = CharStreams.fromString(queryString);
		QueryLexer queryLexer = new QueryLexer(charStream);

		queryLexer.removeErrorListeners();
		queryLexer.addErrorListener(ThrowingErrorListener.INSTANCE);

		CommonTokenStream commonTokenStream = new CommonTokenStream(queryLexer);
		QueryParser queryParser = new QueryParser(commonTokenStream);

		queryParser.removeErrorListeners();
		queryParser.addErrorListener(ThrowingErrorListener.INSTANCE);

		// Gets the context for the root node
		QueryParser.QueryContext queryContext = queryParser.query();
		// Creates the visitor and gives it access to the instance of YelpDB
		QueryBaseVisitor visitor = new QueryBaseVisitor(this);

		// All of the action happens in the QueryBaseVisitor class
		return visitor.visitQuery(queryContext);
	}

	/**
	 * Puts the given set of YelpRestaurants into JSON format
	 * 
	 * @param set
	 * @return JSON formatted String
	 */
	public String getMatchesToJson(Set<YelpRestaurant> set) {
		return gson.toJson(set);
	}

	@Override
	public ToDoubleBiFunction<MP5Db<YelpRestaurant>, String> getPredictorFunction(String user) {

		Set<Point> priceAndStars = new HashSet<Point>();
		ToDoubleBiFunction<MP5Db<YelpRestaurant>, String> returnFunction;

		double priceMean = 0;
		double starsMean = 0;
		double sxx = 0;
		double sxy = 0;
		double b;
		double a;
		int count = 0;

		// Picks out the reviews made by the given user and saves data on price of
		// restaurant and stars given
		for (YelpReview yr : reviewMap.values()) {
			if (yr.getUserID().equals(user)) {

				double tempPrice = restaurantMap.get(yr.getRestaurantID()).getPrice();
				double tempStars = yr.getStars();
				priceMean += tempPrice;
				starsMean += tempStars;

				priceAndStars.add(new Point(tempPrice, tempStars));
				count++;
			}
		}

		// If there are no reviews by this user, we cannot calculate a prediction
		// function
		if (count == 0)
			throw new IllegalArgumentException();
		else {

			// Initially, *Mean stored the total, now it actually stores the mean
			priceMean = priceMean / count;
			starsMean = starsMean / count;

			// calculates least squares
			for (Point p : priceAndStars) {
				sxx += Math.pow(p.getX() - priceMean, 2);
				sxy += (p.getX() - priceMean) * (p.getY() - starsMean);
			}

			// If all reviewed restaurants are at the same price point, we cannot calculate
			// a prediction function
			if (sxx == 0)
				throw new IllegalArgumentException();
			else {

				b = sxy / sxx;
				a = starsMean - b * priceMean;

				returnFunction = (dataBase, restaurantID) -> {
					double price = ((YelpDB) dataBase).restaurantMap.get(restaurantID).getPrice();
					return Math.min(5, Math.max(1, b * price + a));
				};

				return returnFunction;
			}
		}
	}

	/**
	 * Creates a JSON string in the same format as voronoi.json.
	 * 
	 * @param k
	 *            number of clusters
	 * @return String in JSON format
	 */
	@Override
	public String kMeansClusters_json(int k) {

		List<Set<YelpRestaurant>> source = kMeansList(k);
		Set<kMeansToJson> toBeJson = new HashSet<kMeansToJson>();
		Gson gson = new Gson();

		for (int i = 0; i < source.size(); i++) {

			for (YelpRestaurant r : source.get(i)) {
				toBeJson.add(new kMeansToJson(r.getLatitude(), r.getLongitude(), r.getName(), i));
			}
		}
		return gson.toJson(toBeJson);
	}

	/**
	 * Runs the k-means algorithm on all YelpRestaurants in the database for the
	 * given number of clusters
	 * 
	 * @param k
	 *            the number of clusters
	 * @return a List of Sets of YelpRestaurants such that there are k sets and no
	 *         set is empty
	 */
	public LinkedList<Set<YelpRestaurant>> kMeansList(int k) {

		// If k is greater than the number of restaurants, we cannot run the algorithm
		if (k > restaurantMap.size())
			throw new IllegalArgumentException();
		else {

			currentState = new HashMap<YelpRestaurant, Point>();
			Set<Point> centroidSet = new HashSet<Point>();
			Set<YelpRestaurant> tempYRSet = new HashSet<YelpRestaurant>();
			boolean reassigned = true;

			LinkedList<Set<YelpRestaurant>> kMeansList = new LinkedList<Set<YelpRestaurant>>();
			Map<Point, Set<YelpRestaurant>> kMeansMap = new HashMap<Point, Set<YelpRestaurant>>();

			createInitialCentroids(k, currentState, centroidSet);

			mapToClosestCentroid(currentState, centroidSet, reassigned);

			currentState = runKMeansAlgorithm(currentState, centroidSet, tempYRSet);

			// Converts from a Map<Yelp Restaurant, Point> to List<Set<YelpRestaurant>>
			for (Point p : currentState.values()) {
				if (!kMeansMap.containsKey(p))
					kMeansMap.put(p, new HashSet<YelpRestaurant>());
			}

			for (YelpRestaurant res : currentState.keySet())
				kMeansMap.get(currentState.get(res)).add(res);

			for (Point p : kMeansMap.keySet())
				kMeansList.add(kMeansMap.get(p));

			return kMeansList;
		}
	}

	/**
	 * Creates the initial centroid mappings for K-Means to run. Does not
	 * necessarily map to the closest centroid
	 * 
	 * @param k
	 *            the number of clusters
	 * @param currentState
	 *            a map to put all the centroid assignments in
	 * @param centroidSet
	 *            a set to add the k centroids to
	 */
	private void createInitialCentroids(int k, Map<YelpRestaurant, Point> currentState, Set<Point> centroidSet) {

		// Maps first k restaurants to themselves as a centroid
		for (int i = 0; i < k; i++) {
			currentState.put(restaurantList.get(i), restaurantList.get(i).getLocation());
			centroidSet.add(restaurantList.get(i).getLocation());
		}

		// maps rest of restaurants to the location of the first as a centroid
		for (int i = k; i < restaurantList.size(); i++)
			currentState.put(restaurantList.get(i), restaurantList.get(0).getLocation());
	}

	/**
	 * Maps all YelpRestaurants to the nearest centroid
	 * 
	 * @param currentState
	 *            a map of all YelpRestaurants to their current centroid assignment
	 * @param centroidSet
	 *            a set of all the current centroids
	 * @param reassigned
	 *            included to avoid the creation of new objects
	 * @return true if any YelpRestaurants were reassigned to a different centroid,
	 *         false otherwise
	 */
	private boolean mapToClosestCentroid(Map<YelpRestaurant, Point> currentState, Set<Point> centroidSet,
			boolean reassigned) {

		// Checks if any YelpRestaurants are closer to another centroid than to their
		// own
		for (YelpRestaurant yr : currentState.keySet()) {
			for (Point p : centroidSet) {
				if (yr.distanceTo(p) < yr.distanceTo(currentState.get(yr))) {
					currentState.replace(yr, p);
					reassigned = true;
				}
			}
		}

		// If any YelpRestaurants were reassigned, checks to make sure that every
		// centroid has at least one YelpRestaurant mapped to it
		if (reassigned) {
			for (Point p : centroidSet) {
				if (!currentState.containsValue(p))
					// Ensures any centroid with no YelpRestaurats is assigned a YelpRestaurant
					currentState.replace(yROfNonLonelyCentroid(currentState), p);
			}
		}
		return reassigned;
	}

	/**
	 * Finds a Centroid with more than one YelpRestaurant assigned to it and returns
	 * one of those YelpRestaurants
	 * 
	 * @param currentState
	 *            all YelpRestaurants and the centroid they are currently mapped to
	 * @return a YelpRestaurant
	 */
	private YelpRestaurant yROfNonLonelyCentroid(Map<YelpRestaurant, Point> currentState) {

		Set<Point> centroidCount = new HashSet<Point>();
		for (Point p : currentState.values()) {
			if (centroidCount.contains(p)) { // we have previously come across p, meaning more than one yelpRestaurant
												// maps to that centroid
				for (YelpRestaurant yr : currentState.keySet()) { // we now just want to return any yelpRestaurant that
																	// maps to that centroid
					if (currentState.get(yr).equals(p))
						return yr;
				}
			} else // we haven't seen this centroid before, we want to acknowledge we have seen it
				centroidCount.add(p);
		}
		// this should never happen
		return null;
	}

	/**
	 * The recursive step for running K-Means algorithm
	 * 
	 * @param currentState
	 *            all YelpRestaurants and the centroid each is currently assigned to
	 * @param centroidSet
	 *            the set of all current centroids
	 * @param tempYRSet
	 *            included to avoid creating new objects and causing stack overflow
	 * @return a Map that maps all YelpRestaurants the the nearest centroid
	 */
	private Map<YelpRestaurant, Point> runKMeansAlgorithm(Map<YelpRestaurant, Point> currentState,
			Set<Point> centroidSet, Set<YelpRestaurant> tempYRSet) {

		reCalculateCentroids(currentState, centroidSet, tempYRSet);

		if (mapToClosestCentroid(currentState, centroidSet, false))
			// We reassigned some YelpRestaurants and need to run recursion again
			return runKMeansAlgorithm(currentState, centroidSet, tempYRSet);

		else
			// We did not reassign any YelpRestaurants and are done with recursion
			return currentState;
	}

	/**
	 * Re-calculates the centroid for each group of YelpRestaurants assigned to the
	 * same centroid
	 * 
	 * @param currentState
	 *            all YelpRestaurants mapped to a centroid
	 * @param centroidSet
	 *            all current centroids; is updated by this method
	 * @param tempYRSet
	 *            included to avoid creating new objects and causing stack overflow
	 */
	private void reCalculateCentroids(Map<YelpRestaurant, Point> currentState, Set<Point> centroidSet,
			Set<YelpRestaurant> tempYRSet) {

		// Calculate new Centroids
		for (Point p : centroidSet) {
			tempYRSet.clear();

			for (YelpRestaurant yr : currentState.keySet()) {
				if (currentState.get(yr).equals(p))
					tempYRSet.add(yr);
			}

			Point mean = calculateMean(tempYRSet);

			// Reassign these YelpRestaurants to their new centroid
			for (YelpRestaurant yr : tempYRSet)
				currentState.replace(yr, mean);
		}

		// Updates centroidSet to reflect the current centroids
		centroidSet.clear();
		centroidSet.addAll(currentState.values());
	}

	/**
	 * Given a set of restaurants, calculates the mean of their locations
	 * 
	 * @param yRSet
	 *            has at least one element
	 * @return the mean location
	 */
	private Point calculateMean(Set<YelpRestaurant> yRSet) {
		double x = 0;
		double y = 0;
		for (YelpRestaurant yr : yRSet) {
			x += yr.getLatitude();
			y += yr.getLongitude();
		}
		return new Point(x / yRSet.size(), y / yRSet.size());
	}

	// Helper class to make the conversion of List<Set<YelpResturant>> into
	// veroni.json format easier
	private class kMeansToJson {
		private double x;
		private double y;
		private String name;
		private int cluster;
		private final double weight = 1.0;

		kMeansToJson(double x, double y, String name, int cluster) {
			this.x = x;
			this.y = y;
			this.name = name;
			this.cluster = cluster;
		}
	}
}
