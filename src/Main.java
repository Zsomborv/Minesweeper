import java.io.IOException;

/**
 * Main class, innen h�v�dik a Men�.
 * @author zsombor
 *
 */
public class Main {
	
	public static void main(String[] args) throws IOException {
		
		Menu menu = new Menu();
		menu.setVisible(true);
		Thread t = new Thread(menu);
		t.run();
	}
}
