package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.*;
import ca.ece.ubc.cpen221.mp5.datatypes.YelpRestaurant;

public class QueryTests {

	@Test
	public void test00() {
		try {
			YelpDB yelpQT1 = new YelpDB("data/QueryTest1.json", "data/reviews.json", "data/users.json");
			Set<YelpRestaurant> result = yelpQT1.getMatches("category(Chinese)");

			Stream<YelpRestaurant> predictedStream = yelpQT1.getRestaurants().stream()
					.filter(yr -> new HashSet<String>(Arrays.asList(yr.getCategories())).contains("Chinese"));
			Set<YelpRestaurant> predictedSet = predictedStream.collect(Collectors.toCollection(HashSet::new));

			assertEquals(3, predictedSet.size());
			assertEquals(predictedSet, result);

		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	public void test01() {
		try {
			YelpDB yelpQT1 = new YelpDB("data/QueryTest1.json", "data/reviews.json", "data/users.json");
			Set<YelpRestaurant> result = yelpQT1.getMatches("price < 2");

			Stream<YelpRestaurant> predictedStream = yelpQT1.getRestaurants().stream().filter(yr -> yr.getPrice() < 2);
			Set<YelpRestaurant> predictedSet = predictedStream.collect(Collectors.toCollection(HashSet::new));

			assertEquals(5, predictedSet.size());
			assertEquals(predictedSet, result);

		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	public void test02() {
		try {
			YelpDB yelpQT1 = new YelpDB("data/QueryTest1.json", "data/reviews.json", "data/users.json");
			Set<YelpRestaurant> result = yelpQT1.getMatches("category(Chinese) && price < 2");

			Stream<YelpRestaurant> predictedStream = yelpQT1.getRestaurants().stream().filter(yr -> yr.getPrice() < 2
					&& new HashSet<String>(Arrays.asList(yr.getCategories())).contains("Chinese"));
			Set<YelpRestaurant> predictedSet = predictedStream.collect(Collectors.toCollection(HashSet::new));

			assertEquals(1, predictedSet.size());
			assertEquals(predictedSet, result);

		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	public void test03() {
		try {
			YelpDB yelpQT1 = new YelpDB("data/QueryTest1.json", "data/reviews.json", "data/users.json");
			Set<YelpRestaurant> result = yelpQT1.getMatches("category(Chinese) || price < 2");

			Stream<YelpRestaurant> predictedStream = yelpQT1.getRestaurants().stream().filter(yr -> yr.getPrice() < 2
					|| new HashSet<String>(Arrays.asList(yr.getCategories())).contains("Chinese"));

			Set<YelpRestaurant> predictedSet = predictedStream.collect(Collectors.toCollection(HashSet::new));

			assertEquals(7, predictedSet.size());
			assertEquals(predictedSet, result);

		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	public void test0() {
		try {

			YelpDB yelpQT1 = new YelpDB("data/QueryTest1.json", "data/reviews.json", "data/users.json");
			Set<YelpRestaurant> set1 = yelpQT1.getMatches(
					"(category(Chinese) && rating < 4) || ((category(Cafes) || category(Italian)) && price =2)");

			Predicate<YelpRestaurant> predicate1 = (yr -> yr.getStars() < 4
					&& new HashSet<String>(Arrays.asList(yr.getCategories())).contains("Chinese"));
			Predicate<YelpRestaurant> predicate2 = (yr -> new HashSet<String>(Arrays.asList(yr.getCategories()))
					.contains("Cafes") || new HashSet<String>(Arrays.asList(yr.getCategories())).contains("Italian"));

			Stream<YelpRestaurant> predictedStream1 = yelpQT1.getRestaurants().stream().filter(predicate2)
					.filter(yr -> yr.getPrice() == 2);
			Stream<YelpRestaurant> predictedStream2 = yelpQT1.getRestaurants().stream().filter(predicate1);

			Set<YelpRestaurant> predictedSet = predictedStream1.collect(Collectors.toCollection(HashSet::new));
			predictedSet.addAll(predictedStream2.collect(Collectors.toCollection(HashSet::new)));

			assertEquals(predictedSet, set1);

		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	// Test an impossible case
	public void test1() {
		try {
			YelpDB yelpQT1 = new YelpDB("data/QueryTest1.json", "data/reviews.json", "data/users.json");
			Set<YelpRestaurant> set1 = yelpQT1.getMatches("rating > 4 && rating < 2");

			assertTrue(set1.isEmpty());

		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	// sets and subsets
	public void test2() {
		try {

			YelpDB yelpQT1 = new YelpDB("data/QueryTest1.json", "data/reviews.json", "data/users.json");
			Set<YelpRestaurant> set1 = yelpQT1.getMatches("price <= 2 && rating >= 4");
			Set<YelpRestaurant> set2 = yelpQT1.getMatches("rating >= 4");

			Stream<YelpRestaurant> predictedStream1 = yelpQT1.getRestaurants().stream()
					.filter(yr -> yr.getPrice() <= 2 && yr.getStars() >= 4);
			Stream<YelpRestaurant> predictedStream2 = yelpQT1.getRestaurants().stream()
					.filter(yr -> yr.getStars() >= 4);

			Set<YelpRestaurant> predictedSet1 = predictedStream1.collect(Collectors.toCollection(HashSet::new));
			Set<YelpRestaurant> predictedSet2 = predictedStream2.collect(Collectors.toCollection(HashSet::new));

			assertEquals(predictedSet1, set1);
			assertEquals(predictedSet2, set2);

			assertEquals(set1.size(), set2.size());
			assertEquals(6, set1.size());
			assertFalse(set1.retainAll(set2));

		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	// just another normal test
	public void test3() {

		try {

			YelpDB yelpQT1 = new YelpDB("data/QueryTest1.json", "data/reviews.json", "data/users.json");
			Set<YelpRestaurant> set1 = yelpQT1
					.getMatches("(in(UC Campus Area) || in(Telegraph Ave)) && category(Chinese)");

			String[] ans = { "Peking Express", "Sun Hong Kong Restaurant", "Happy Valley" };

			Set<String> resultNames = new HashSet<String>(Arrays.asList(ans));

			for (YelpRestaurant yr : set1) {
				if (!resultNames.contains(yr.getName()))
					fail();

			}
			assertEquals(ans.length, set1.size());

		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	public void test4() {
		try {
			YelpDB yelp = new YelpDB("data/restaurants.json", "data/reviews.json", "data/users.json");
			yelp.getMatches("in UC Berkeley Area & Chinese");

			fail();
		} catch (IllegalArgumentException e) {
		} catch (FileNotFoundException e) {
			fail();
		}
	}
	
	@Test
	public void test5() {
		try {
			YelpDB yelp = new YelpDB("data/restaurants.json", "data/reviews.json", "data/users.json");
			assertTrue(yelp.getMatches("name(Cream)").isEmpty());

		} catch (FileNotFoundException e) {
			fail();
		}
	}

}
