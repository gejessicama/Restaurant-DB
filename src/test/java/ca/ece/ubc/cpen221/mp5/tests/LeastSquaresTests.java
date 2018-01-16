package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.function.ToDoubleBiFunction;

import ca.ece.ubc.cpen221.mp5.MP5Db;
import ca.ece.ubc.cpen221.mp5.YelpDB;
import ca.ece.ubc.cpen221.mp5.datatypes.YelpRestaurant;

public class LeastSquaresTests {

	private static final double TOLERANCE = 0.000001;

	private YelpDB theStuff;

	@Test
	// tests a user not included in the database
	public void test0() {

		try {
			theStuff = new YelpDB("data/testRest.json", "data/reviewsTest.json", "data/usersTest.json");

			String userID = "idgaf";
			theStuff.getPredictorFunction(userID);
			fail();

		} catch (IllegalArgumentException e) {
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	// tests a user who wrote no reviews
	public void test1() {
		try {
			theStuff = new YelpDB("data/testRest.json", "data/reviewsTest.json", "data/usersTest.json");

			String userID = "_NH7Cpq3qZkByP5xR4gXog";
			theStuff.getPredictorFunction(userID);
			fail();

		} catch (IllegalArgumentException e) {
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	// tests a user with only one review
	public void test2() {
		try {
			theStuff = new YelpDB("data/testRest.json", "data/reviewsTest.json", "data/usersTest.json");

			String userID = "QScfKdcxsa7t5qfE0Ev0Cw";
			theStuff.getPredictorFunction(userID);
			fail();

		} catch (IllegalArgumentException e) {
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	// tests a user with reviews all at same price point
	public void test3() {
		try {
			theStuff = new YelpDB("data/testRest.json", "data/reviewsTest.json", "data/usersTest.json");

			String userID = "754HGCLgGJLh1VU_WtGjsw";
			theStuff.getPredictorFunction(userID);
			fail();

		} catch (IllegalArgumentException e) {
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	// tests the function generation with two separate points to make sure line is
	// correct. Also tests using a different database than the one used to generate
	// the function
	public void test4() {
		try {
			theStuff = new YelpDB("data/testRest.json", "data/reviewsTest.json", "data/usersTest.json");
			String preFix = "data/";
			YelpDB aiya = new YelpDB(preFix + "restaurants.json", preFix + "reviews.json", preFix + "users.json");

			String userID = "7RsdY4_1Bb_bCf5ZbK6tyQ";
			ToDoubleBiFunction<MP5Db<YelpRestaurant>, String> predictorFn = theStuff.getPredictorFunction(userID);

			assertEquals(1.7, predictorFn.applyAsDouble(theStuff, "gclB3ED6uk6viWlolSb_uA"), TOLERANCE);
			assertEquals(5, predictorFn.applyAsDouble(aiya, "XD5ybqI0BHcTj5cLQyIPLA"), TOLERANCE);
			
		} catch (FileNotFoundException e) {
			fail();
		}
	}
}
