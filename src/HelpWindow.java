import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * A játék szabályait még nem ismerő, vagy
 * elfelejtő játékosok számára létrehozott 
 * segítő/súgó ablak.
 * @author zsombor
 *
 */
public class HelpWindow extends JFrame{
	/**
	 * Beolvassa a segítség leírását egy txt-ből. 
	 * Soronként hozzáadja egy új JLabel-höz.
	 * @throws IOException - fileolvasás miatt.
	 */
	void helpReader() throws IOException {
		File file = new File("help.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String st;
		while((st = br.readLine()) != null) {
			JLabel lb = new JLabel();
			lb.setText(st);
			lb.setSize(700,100);
			add(lb);
		}
		br.close();
	}
	/**
	 * Létrehozza az ablakot.
	 * @throws IOException - a helpReader miatt
	 */
	public HelpWindow() throws IOException {
		super("Segtség");
		this.setLocationRelativeTo(null);
		setMinimumSize(new Dimension(700, 500));
		this.setLayout(new GridLayout(30,1));
		helpReader();
	}
}
