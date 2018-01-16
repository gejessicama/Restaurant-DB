package ca.ece.ubc.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import ca.ece.ubc.cpen221.mp5.*;

public class ServerTests {
	@Test
	public void test0() throws InterruptedException {

		Thread testServer = new Thread(new Runnable() {
			public void run() {
				try {
					YelpDBServer server = new YelpDBServer(4949);
					server.serve();
				} catch (IOException e) {

				}
			}

		});
		testServer.start();

		Thread client1Test = new Thread(new Runnable() {
			public void run() {
				try {
					Client client1 = new Client("localhost", 4949);
					client1.sendRequest("QUERY in(UC Campus Area)");
					client1.getReply();
					client1.close();
				} catch (IOException e) {
				}
			}

		});
		client1Test.start();
		client1Test.join();

		Thread client2Test = new Thread(new Runnable() {
			public void run() {
				try {
					Client client2 = new Client("localhost", 4949);
					client2.sendRequest("ADDUSER {ddd}");
					assertEquals(client2.getReply(), "ERR: INVALID_JSON_STRING");
					client2.sendRequest("jkjk");
					assertEquals(client2.getReply(), "ERR: ILLEGAL_REQUEST");
					client2.sendRequest("QUERY in(China)");
					assertEquals(client2.getReply(), "ERR: NO_MATCH");
					client2.sendRequest("ADDUSER {\"name\":\"Maria\"}");
					assertTrue(client2.getReply() != null);
					client2.sendRequest("GETUSER GenID#1");
					assertEquals("Maria", client2.getReply());
					client2.sendRequest(
							"ADDRESTAURANT {\"open\": true, \"url\": \"http://www.yelp.com/biz/gather-berkeley\", \"longitude\": -122.26602, \"neighborhoods\": [\"Downtown Berkeley\", \"UC Campus Area\"], \"name\": \"Gather\", \"categories\": [\"American (New)\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 4.0, \"city\": \"Berkeley\", \"full_address\": \"2200 Oxford St\\nDowntown Berkeley\\nBerkeley, CA 94704\", \"photo_url\": \"http://s3-media1.ak.yelpcdn.com/bphoto/spcQdylyupt4Dy1fxYJ55A/ms.jpg\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 37.869348, \"price\": 4}\r\n");
					assertTrue(client2.getReply() != null);
					client2.sendRequest(
							"ADDREVIEW {\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"text\": \"I think they\", \"stars\": 4, \"user_id\": \"ej9j-Vof_xAq9cZQ5WxRbg\", \"date\": \"2006-11-30\"}\r\n");
					assertTrue(client2.getReply() != null);
					client2.sendRequest("QUERY in(Telegraph Ave)");
					assertTrue(client2.getReply() != null);
					client2.close();
				} catch (IOException e) {
				}
			}

		});
		client2Test.start();
		client2Test.join();
	}

	@Test
	public void test1() throws InterruptedException {

		Thread testServer = new Thread(new Runnable() {
			public void run() {
				try {
					YelpDBServer server = new YelpDBServer(4249);
					server.serve();
				} catch (IOException e) {

				}
			}

		});
		testServer.start();

		Thread client3Test = new Thread(new Runnable() {
			public void run() {
				try {
					Client client3 = new Client("localhost", 4249);
					client3.sendRequest("GETRESTAURANT 3333fds");
					assertEquals(client3.getReply(), "ERR: NO_SUCH_RESTAURANT");
					client3.sendRequest("GETRESTAURANT 93sW9Y_3rJQn305_n8epng");
					assertEquals(client3.getReply(), "Racha Cafe");
					client3.close();
				} catch (IOException e) {
				}
			}

		});
		client3Test.start();
		client3Test.join();
	}

}
