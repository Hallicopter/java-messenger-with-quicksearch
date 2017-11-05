import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.net.URL;

/**
 * <h1>Browser Class</h1>
 * An object of the browser class is called whenever the quicksearch utility is used.
 * @author Advait Raykar, Vishwa Kalyanaraman, Parv Kapoor
 * @version 1.0
 * @since 5th November 2017
 *
 */
class Browser extends JFrame{
	//private JTextField addressBar;
	private JEditorPane display;

	// Constructor
	/**
	 * The constructor intialises the window,
	 * and adds ActionListeners to all the HyperLinks to the JEditorPane
	 */
	public Browser(){
		super("Quick search");

		display = new JEditorPane();
		display.setEditable(false);
		display.addHyperlinkListener(
			new HyperlinkListener(){
				public void hyperlinkUpdate(HyperlinkEvent event){
					if(event.getEventType()==HyperlinkEvent.EventType.ACTIVATED){
						loadPage(event.getURL().toString());
					}
				}
			}
		);

		add(new JScrollPane(display), BorderLayout.CENTER);
		setSize(640, 480);
		setVisible(true);
	}

	// Load page
	/**
	 * Loads the page using the built in {@code JEditorPane.setPage()}
	 * method.
	 * @param query The URL of the webpage to be visited.
	 */
	public void loadPage(String query){
		try{
			display.setPage(query);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * This function is called when
	 * an action is performed in the JTextField in either the server or the 
	 * client.
	 * @param br Instance of the browser
	 * @param st The URL of the query that is entered by the user.
	 */
	public void pseudoMain(Browser br, String st){
		//Browser br = new Browser();
		br.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		br.loadPage(st);
	}
}