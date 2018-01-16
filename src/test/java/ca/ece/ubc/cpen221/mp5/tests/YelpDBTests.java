package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;

import ca.ece.ubc.cpen221.mp5.YelpDB;
import ca.ece.ubc.cpen221.mp5.datatypes.Point;
import ca.ece.ubc.cpen221.mp5.datatypes.YelpRestaurant;

public class YelpDBTests {

	private YelpDB aiya;

	@Test
	public void test0() {
		try {
			YelpDB aiya = new YelpDB("data/restaurants.json", "data/reviews.json", "data/users.json");

			assertEquals(135, aiya.getRestaurants().size());
			assertEquals(8556, aiya.getUsers().size());
			assertEquals(17396, aiya.getReviews().size());

		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	// outputting some voronoi stuff
	public void test1() throws FileNotFoundException {

		try {
			YelpDB aiya = new YelpDB("data/restaurants.json", "data/reviews.json", "data/users.json");

			assertEquals(5, aiya.kMeansList(5).size());

			// System.out.println("k-means cluster, k = 5: ");
			// System.out.println(aiya.kMeansClusters_json(5));
			// System.out.println("k-means cluster, k = 5: ");
			// System.out.println(aiya.kMeansClusters_json(5));
			// System.out.println("k-means cluster, k = 3: ");
			// System.out.println(aiya.kMeansClusters_json(3));

		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	// tests that no restaurant is closer to another centroid
	public void test2() {

		try {
			YelpDB aiya = new YelpDB("data/restaurants.json", "data/reviews.json", "data/users.json");

			aiya.kMeansClusters_json(45);

			Map<YelpRestaurant, Point> kMeansResult = aiya.getCurrentState();
			Set<Point> centroids = new HashSet<Point>(kMeansResult.values());

			for (YelpRestaurant res : aiya.getRestaurants()) {
				for (Point p : centroids) {
					if (res.distanceTo(p) < res.distanceTo(kMeansResult.get(res)))
						fail();
				}
			}
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	// tests that no cluster is empty i.e. no set corresponding to a centroid is
	// empty
	public void test3() {

		try {
			YelpDB aiya = new YelpDB("data/restaurants.json", "data/reviews.json", "data/users.json");
			boolean noEmpty = true;

			LinkedList<Set<YelpRestaurant>> list = aiya.kMeansList(45);

			for (int i = 0; i < list.size(); i++) {
				noEmpty = (!list.get(i).isEmpty());
			}
			assertTrue(noEmpty);

		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	// tests that if k > number of restaurants, throw an exception
	public void test4() {
		try {
			YelpDB aiya = new YelpDB("data/restaurants.json", "data/reviews.json", "data/users.json");

			aiya.kMeansList(136);
			fail();

		} catch (FileNotFoundException e) {
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	// testing the yROfNonLonelyCentroid or something like that
	public void test5() {
		try {
			YelpDB aiya = new YelpDB("data/leastLonelyRestTest.json", "data/reviews.json", "data/users.json");

			boolean noEmpty = true;

			LinkedList<Set<YelpRestaurant>> list = aiya.kMeansList(3);

			for (int i = 0; i < list.size(); i++) {
				noEmpty = (!list.get(i).isEmpty());
			}
			assertTrue(noEmpty);
		} catch (FileNotFoundException e) {
			fail();
		}
	}

}
