package ca.ece.ubc.cpen221.mp5;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Pretty much the same format as Client and Client2, except this will take in
 * input from the console/command line and has some interactive text. Again, we
 * kept this in here just in case it might be useful for getting to know our
 * implementation of the server.
 */
public class CMDYelpClient {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	/**
	 * Make a YelpClient, connect it to a server running on hostname at the
	 * specified port.
	 */
	public CMDYelpClient(String hostname, int port) throws IOException {

		socket = new Socket(hostname, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public void sendRequest(String s) throws IOException {
		out.print(s + "\n");
		out.flush();
	}

	public String getReply() throws IOException {
		String reply = in.readLine();
		if (reply == null) {
			throw new IOException();
		}
		return reply;
	}

	/**
	 * Closes the client's connection to the server. This client is now "closed".
	 * Requires this is "open".
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

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		String[] input = sc.nextLine().split(" ", 2);
		String hostname = input[0];
		int port = Integer.valueOf(input[input.length - 1]);
		try {
			CMDYelpClient client = new CMDYelpClient(hostname, port);

			while (true) {
				System.out.println("What's your next request?"); 
				String s = sc.nextLine();

				// end the thread
				if (s.equals("bye")) {
					System.out.println("Bye bye!");
					sc.close();
					client.close();
					break;
				}
				client.sendRequest(s);
				System.out.println(client.getReply());
			}

		} catch (IOException e) {
			System.err.println("ERROR CREATING SERVER. Suggestion: check ports");
		}
	}
}
