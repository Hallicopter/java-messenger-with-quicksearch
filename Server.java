import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <h1>The Main Server Class</h1>
 * This class is called when a Server instance is launched.
 * @author Advait Raykar, Vishwa Kalyanaraman, Parv Kapoor
 * @version 1.0
 * @since 5th November 2017
 *
 */
public class Server extends JFrame{
	// User should input text in this field
	private JTextField userText;
	// This is very all the actual chat is displayed
	private JTextArea chatWindow;
	// Creates streams for data output and output.
	private ObjectOutputStream output;
	private ObjectInputStream input;
	// Creating a JTextField for quick search feature
	private JTextField search;
	private Browser br;

	private ServerSocket server;
	private Socket connection;
	/**
	 * This is the constructor, which intialises the actual
	 * window, adds the necessary action listners, and adds the
	 * swing elements to the window. 
	 */
	public Server(){
		super("Better than whatsapp/telegram/others");
		userText = new JTextField();
		
		// The textfield is not editable by default
		// The textfield becomes editable only when connected
		// to another user
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

	// Start-up method
	/**
	 * This method when called does the following three things:
	 *<ul>
  	 *<li>Waits for a client to connect</li>
     *<li>Sets up all the required streams</li>
     *<li>Makes sure messages are sent and recieved properly</li>
	 *</ul> 
	 */
	public void beginProgram(){
		try{
			//Port no. = 5050, Max backlog = 100 
			server = new ServerSocket(5050, 100);
			while(true){
				try{
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofE){
					showMessage("\nConnection ended.");
				}finally{
					endProgram();
				}
			}
		}catch(IOException ioEx){
			ioEx.printStackTrace();
		}
	}

	// Wait for connection, after which display connection information
	/**
	 * Waits for connection on starting the server.
	 * @exception IOException
	 */
	private void waitForConnection() throws IOException{
		showMessage("Waiting for external connections.\n");
		connection = server.accept();
		showMessage("Connected to " + 
			connection.getInetAddress().getHostName());
	}

	// Sets up all the stream appropriately
	/**
	 * Creates an {@code ObjectOutputStream} object
	 * and an {@code ObjectInputStream} object for sending and recieving messages
	 * @exception IOException
	 */
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();	
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\nStreams successfuly setup!\n");			
	}

	// Method called when conversing via chat
	/**
	 * Manages calls to {@code showMessage()}
	 * Note, the prgram quits connection when user inputs <i> xoxo </i>
	 * which is slang for <i> 'hearts and kisses' </i>
	 * @exception IOException
	 */
	private void whileChatting() throws IOException{
		String message = "You are connected.";
		sendMessage(message);
		allowTyping(true);
		do{
			try{
				message = (String)input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException clex){
				showMessage("\n Sick glitch.");
			}
		}while(!message.equals("CLIENT - xoxo"));
	}

	// Cleanup after chat
	/**
	 * Closes all the streams after severing 
	 * connections with the client.
	 */
	private void endProgram(){
		showMessage("\nClosing connections\n");
		allowTyping(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioexc){
			ioexc.printStackTrace();
		}
	}

	// Send message to client
	/**
	 * Sends the message to the output stream object
	 * @param message This is the message that is to be sent downstream.
	 */
	private void sendMessage(String message){
		try{
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\nSERVER - " + message);
		}catch(IOException ioexc){
			chatWindow.append("\n Error, message can't be sent.");
		}
	}

	// Update chat window
	/**
	 * This prints out the message on the
	 * chat window so it's visible to the user.
	 * @param text The text to be displayed
	 */
	private void showMessage(final String text){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatWindow.append(text);
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