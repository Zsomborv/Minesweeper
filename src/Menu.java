
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



/**
 * Menü, ahonnan navigálhatja magát a játékos. 
 *
 * @author zsombor
 *
 */
@SuppressWarnings("serial")
public class Menu extends JFrame implements  Runnable {
	
	int[] sett={16,16,40}; //Beállítások meghatározása, default közepesre állítva.
	
	/**
	 * Al-Menu-class, ami egy kibővített JPanel ActionListenerrel
	 * a gombok tárolására és elhelyzésére.
	 * @author zsombor
	 *
	 */
	public class MenuPanel extends JPanel implements ActionListener{
		
		//Gombok létrehozása
		JButton startB = new JButton("Új Játék");
		JButton settingsB = new JButton("Beállítások");
		JButton helpB = new JButton("Segítség");
		JButton highsB = new JButton("Ranglista");
		
		/**
		 * Layout beállító konstruktor.
		 * Elhelyezzük a gombokat és listenereket adunk nekik.
		 */
		public MenuPanel() {
			
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.NORTH;
            
            add(new JLabel("<html><h1><strong>MineSweeper</strong></h1><hr></html>"), gbc);
            
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JPanel buttons = new JPanel(new GridBagLayout());
            buttons.add(startB, gbc);
            buttons.add(settingsB, gbc);
            buttons.add(highsB, gbc);
            buttons.add(helpB, gbc);
            
            gbc.weighty = 1;
            add(buttons, gbc);
            
            startB.addActionListener(this);
    		settingsB.addActionListener(this);
    		helpB.addActionListener(this);
    		highsB.addActionListener(this);
		}
		
		/**
		 * Kötelező függvény mert a MenuPanelem ActionListener.
		 * Minden gombra előhozza a neki meghatározott framet.
		 * 
		 * @param e - A lenyomott gomb eventje.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == startB) { //Új Játék gomb
				MineSweeper ms = new MineSweeper(sett);
				ms.setVisible(true);
				Thread t = new Thread(ms);
				t.run();
			}
			if(e.getSource() == settingsB) { //Beállítások gomb
				Settings s = new Settings(sett);
				s.setVisible(true);
				sett= s.getActual();
			}
			if(e.getSource() == helpB) { //Segítség gomb
				HelpWindow hw;
				try {
					hw = new HelpWindow();
					hw.setVisible(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
			if(e.getSource() == highsB) { //Ranglista gomb
				ScoreBoardFrame sf = new ScoreBoardFrame();
		        sf.setVisible(true);
			}
		}
	}

	/**
	 * Mivel a Menu Threadből fut, így kötelező
	 * létrehozni neki egy run()-t. 
	 * Itt azt csinál, amit egyébként egy konstruktorban csinálna.
	 */
	@Override
	public void run() {
		this.setTitle("Minesweeper");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Hogy X-re ténylegesen bezárjon
		setResizable(false); //A gombok helye miatt jobb nem változtatni a méretet
		this.add(new MenuPanel());
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setSize(270, 400);
		
	}
}
