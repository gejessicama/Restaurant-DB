package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.YelpDB;

public class MoreYelpDBTests {
	// need to test simple queries methods from the yelpDB
	// for part 4 and 5

	@Test
	// everything to do with restaurants
	public void test0() {
		YelpDB ydb;
		try {
			ydb = new YelpDB("data/restaurants.json", "data/reviews.json", "data/users.json");
			assertEquals("Cafe 3", ydb.getRestNameFromId("gclB3ED6uk6viWlolSb_uA"));
			ydb.addRestaurant(
					"{\"open\": true, \"longitude\": -122.2565107, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"name\": \"TESTS\", \"categories\": [\"Food\", \"Coffee & Tea\", \"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"city\": \"Berkeley\", \"full_address\": \"2625 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94720\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.8681313, \"price\": 4}\r\n");
			assertEquals("TESTS", ydb.getRestaurant("GenID#1").getName());
			assertEquals("ERR: INVALID_RESTAURANT_STRING", ydb.addRestaurant(
					"{\"open\": true, \"longitude\": -122.2565107, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"categories\": [\"Food\", \"Coffee & Tea\", \"Cafes\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"city\": \"Berkeley\", \"full_address\": \"2625 Durant Ave\\nTelegraph Ave\\nBerkeley, CA 94720\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.8681313, \"price\": 8}\r\n"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	// everything to do with users and reviews
	public void test1() {
		YelpDB ydb;
		try {
			ydb = new YelpDB("data/restaurants.json", "data/reviews.json", "data/users.json");
			assertEquals("ERR: INVALID_USER_STRING",ydb.addUser("{\"review_count\": 5}"));
			ydb.addUser("{\"name\" : \"Jessica\"}");
			assertEquals("Jessica",ydb.getUser("GenID#1").getName());
			//get new stars
			ydb.addReview("{\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"text\": \"content\", \"stars\": 4, \"user_id\": \"GenID#1\", \"date\": \"2012-01-28\"}");
			assertEquals(ydb.getUser("GenID#1").getReviewCount(),1);
			//no such restaurant
			assertEquals("ERR: NO_SUCH_RESTAURANT",ydb.addReview("{\"type\": \"review\", \"business_id\": \"ddd\", \"text\": \"content\", \"stars\": 4, \"user_id\": \"GenID#1\", \"date\": \"2012-01-28\"}"));
			//no such user
			assertEquals("ERR: NO_SUCH_USER",ydb.addReview("{\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"text\": \"content\", \"stars\": 4, \"user_id\": \"GenID#132\", \"date\": \"2012-01-28\"}"));
			//impossible rating
			assertEquals("ERR: INVALID_REVIEW_STRING",ydb.addReview("{\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"text\": \"content\", \"stars\": 8, \"user_id\": \"GenID#1\", \"date\": \"2012-01-28\"}"));
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


}
