import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
	// User should input text in this field
	private JTextField userText;
	// This is very all the actual chat is displayed
	private JTextArea chatWindow;
	// Creates streams for data output and output.
	private ObjectOutputStream output;
	private ObjectInputStream input;

	private ServerSocket server;
	private Socket connection;

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
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(480, 360);
		setVisible(true);
	}

	// Start-up method
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
	private void waitForConnection() throws IOException{
		showMessage("Waiting for external connections.\n");
		connection = server.accept();
		showMessage("Connected to " + 
			connection.getInetAddress().getHostName());
	}

	// Sets up all the stream appropriately
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();	
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\nStreams successfuly setup!\n");			
	}

	// Method called when conversing via chat
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
	private void allowTyping(final boolean flag){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					userText.setEditable(flag);
				}
			}
		);
	}
}