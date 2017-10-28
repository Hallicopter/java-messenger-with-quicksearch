import javax.swing.JFrame;

public class ServerStart{
	public static void main(String[] args) {
		Server serv = new Server();
		serv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serv.beginProgram(); 
	}
}