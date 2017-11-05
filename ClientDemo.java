import javax.swing.*;

/**
 * <h1>The class that calls the Client class.</h1>
 * Instantiates the Client class object and starts the Client.
 * The host passed in the constructor is ip of the localhost, so that the program can run locally.
 * This can be changed per convience.
 * @author Advait Raykar, Vishwa Kalyanaraman, Parv Kapoor
 * @version 1.0
 * @since 5th November 2017
 *
 */
public class ClientDemo{
	public static void main(String[] args) {
		Client cl;
		cl = new Client("127.0.0.1");
		cl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cl.beginProgram();
	}
}