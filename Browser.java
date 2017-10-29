import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.net.URL;

class Browser extends JFrame{
	//private JTextField addressBar;
	private JEditorPane display;

	// Constructor
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
	public void loadPage(String query){
		try{
			display.setPage(query);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void pseudoMain(Browser br, String st){
		//Browser br = new Browser();
		br.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		br.loadPage(st);
	}
}