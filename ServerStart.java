import javax.swing.JFrame;

/**
 * <h1>The class that calls the Server class.</h1>
 * Instantiates the Server class object and starts the server.
 * @author Advait Raykar, Vishwa Kalyanaraman, Parv Kapoor
 * @version 1.0
 * @since 5th November 2017
 *
 */
public class ServerStart{
	public static void main(String[] args) {
		Server serv = new Server();
		serv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serv.beginProgram(); 
	}
}