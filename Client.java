import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <h1>The Main Client Class</h1>
 * This class is called when a Server instance is launched.
 * @author Advait Raykar, Vishwa Kalyanaraman, Parv Kapoor
 * @version 1.0
 * @since 5th November 2017
 *
 */

public class Client extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	private JTextField search;
	private Browser br;

	// Constructor
	/**
	 * The constructor initialises window elements,
	 * and adds the approriate event handlers.
	 * @param host The host id for finding the server.
	 */
	public Client(String host){
		super("Lit Client");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					sendMessage(event.getActionCommand());
					userText.setText("");
				}
			}
		);
		search = new JTextField();
		search.setEditable(true);
		search.setText("Quick search");
		search.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					br = new Browser();
					br.pseudoMain(br, formatForGoogle(event.getActionCommand()));
					search.setText("");
				}
			}
		);
		add(userText, BorderLayout.NORTH);
		add(search, BorderLayout.SOUTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(480, 360);
		setVisible(true);
	}

	// Method to connect to server
	/**
	 * This method when called does the following three things:
	 *<ul>
  	 *<li>Tries to connect to the server</li>
     *<li>Sets up all the required streams</li>
     *<li>Makes sure messages are sent and recieved properly</li>
	 *</ul> 
	 */
	public void beginProgram(){
		try{
			connectToServer();
			setupStreams();
			whileChatting();
		}catch(EOFException eof){
			showMessage("\nClient terminated connection\n");
		}catch(IOException io){
			io.printStackTrace();
		}finally{
			endProgram();
		}
	}

	// Connect to server
	/**
	 * Attempts to connect to the server at PORT 5050
	 * which is what is used in the Server source.
	 * @exception IOException
	 */
	private void connectToServer() throws IOException{
		showMessage("\n Attempting to connect\n");
		connection = new Socket(InetAddress.getByName(serverIP), 5050);
		showMessage("\nConnected to "+connection.getInetAddress().getHostName());
	}

	// Setup streams
	/**
	 * Creates an {@code ObjectOutputStream} object
	 * and an {@code ObjectInputStream} object for sending and recieving messages
	 * @exception IOException
	 */
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams setup.\n");
	}

	// While chatting part
	/**
	 * Manages calls to {@code showMessage()}
	 * Note, the prgram quits connection when user inputs <i> xoxo </i>
	 * which is slang for <i> 'hearts and kisses' </i>
	 * @exception IOException
	 */
	private void whileChatting() throws IOException{
		allowTyping(true);
		do{
			try{
				message = (String)input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException cnf){
				showMessage("\nSick glitch");
			}
		}while(!message.equals("SERVER - xoxo"));
	}

	// Cleanup after closing
	/**
	 * Closes all the streams after severing 
	 * connections with the client.
	 */
	private void endProgram(){
		showMessage("\nClosing downs\n");
		allowTyping(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException io){
			io.printStackTrace();
		}
	}

	// Send messaging functionality
	/**
	 * Sends the message to the output stream object
	 * @param message This is the message that is to be sent downstream.
	 */
	private void sendMessage(String message){
		try{
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\nCLIENT - "+message);
		}catch(IOException io){
			chatWindow.append("\n Error\n");
		}
	}

	// Show message
	/**
	 * This prints out the message on the
	 * chat window so it's visible to the user.
	 * @param text The text to be displayed
	 */
	private void showMessage(final String message){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatWindow.append(message);
				}
			}
		);
	}

	// Allow typing?
	/**
	 * Should typing be allowed?
	 * @param flag True/False value
	 */
	private void allowTyping(final boolean flag){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					userText.setEditable(flag);
				}
			}
		);	
	}

	/**
	 * Formats the user query into a form that yeilds
	 * search results in the browser
	 * @param plain The plaintext to be searched.
	 */
	private String formatForGoogle(String plain){
		String def = "https://in.search.yahoo.com/search?p=";//"http://www.google.com/search?q=";
		String[] words = plain.split(" ");

		for(String word: words){
			def+=(word+"+");
		}
		
		return def;
		
	}
}