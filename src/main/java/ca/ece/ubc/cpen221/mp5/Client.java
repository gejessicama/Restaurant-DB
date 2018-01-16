package ca.ece.ubc.cpen221.mp5;

import java.io.*;
import java.net.Socket;

/**
 * This was created to help us test the server and check that it can accept
 * multiple clients at once We've decided to keep it in here just in case you
 * would like to use it to test out the server.
 */
public class Client {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	/**
	 * Make a YelpClient, connect it to a server running on hostname at the
	 * specified port.
	 */
	public Client(String hostname, int port) throws IOException {

		socket = new Socket(hostname, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

	}

	// sends a request
	public void sendRequest(String s) throws IOException {
		out.print(s + "\n");
		out.flush();
	}

	// obtains response from the server
	public String getReply() throws IOException {
		String reply = in.readLine();
		if (reply == null) {
			throw new IOException();
		}
		return reply;
	}

	/**
	 * closes connection to server: presumes the server is open when called
	 * 
	 * @throws IOException
	 *             if close fails
	 */
	public void close() throws IOException {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		in.close();
		out.close();
		socket.close();
	}

	// performs a series of requests and prints out the response of each

	public static void main(String[] args) {
		try {
			Client client = new Client("localhost", YelpDBServer.YELP_PORT);
			client.sendRequest("GETRESTAURANT 93sW9Y_3rJQn305_n8epng");// Racha Cafe 
			System.out.println(client.getReply());
			client.sendRequest("GETRESTAURANT MjHULXYJDc9XMM2r24oddg");// Berkeley Floor Cafe
			System.out.println(client.getReply());
			client.sendRequest("ADDUSER {\"name\": \"A. Marziali\"}");
			System.out.println(client.getReply());
			client.sendRequest(
					"ADDREVIEW {\"type\": \"review\", \"business_id\": \"1CBs84C-a-cuA3vncXVSAw\", \"text\": \"These violent delights have violent ends, and in their triumph die, like fire and powder.\", \"stars\": 2, \"user_id\": \"90wm_01FAIqhcgV_mPON9Q\", \"date\": \"2006-07-26\"}");
			System.out.println(client.getReply());
			client.sendRequest("ADDUSER {\"review_count\": 5, \"name\": \"Ali G.\"}");
			System.out.println(client.getReply());
			client.sendRequest("ADDUSER {\"name\": \"Slim Shady\", \"review_count\": 5}");
			System.out.println(client.getReply());
			client.sendRequest("ADDUSER {\"review_count\": 5}");
			System.out.println(client.getReply());
			client.sendRequest(
					"ADDRESTAURANT {\"open\": true, \"longitude\": -420.000, \"neighborhoods\": [\"Telegraph Ave\", \"UC Campus Area\"], \"name\": \"Da Cribbb\", \"categories\": [\"Korean\", \"Restaurants\"], \"state\": \"CA\", \"type\": \"business\", \"stars\": 3.5, \"city\": \"Berkeley\", \"full_address\": \"2521 Durant Ave\\nSte F\\nTelegraph Ave\\nBerkeley, CA 94704\", \"schools\": [\"University of California at Berkeley\"], \"latitude\": 66.00, \"price\": 2}\r\n");
			System.out.println(client.getReply());
			client.close();
		} catch (IOException e) {
			System.err.println("ERROR CREATING SERVER. Suggestion: check ports");
		}
	}
}
