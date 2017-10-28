import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;

	// Constructor
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
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(480, 360);
		setVisible(true);
	}

	// Method to connect to server
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
	private void connectToServer() throws IOException{
		showMessage("\n Attempting to connect\n");
		connection = new Socket(InetAddress.getByName(serverIP), 5050);
		showMessage("\nConnected to "+connection.getInetAddress().getHostName());
	}

	// Setup streams
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams setup.\n");
	}

	// While chatting part
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