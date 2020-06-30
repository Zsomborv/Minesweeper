import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.CardLayout;

/**
 * Beállítások ablak
 * @author zsombor
 *
 */
public class Settings extends JFrame{
	
	int[] defaultSettings = {16,16,40}; //h,l,m
	JFrame frame = new JFrame();
	
	// Minimum és maximumbeállítások az aknákra és mezőkre.
	int min=8;
	int maxH=30;
	int maxL=16;
	int maxM=99;
	int minM=10;
	
	int[] actual = new int[3];
	
	
	Object lengthList[] = new Object[23];
	Object heigthList[] = new Object[9];
	Object[] mineList = new Object[90];
	
	/**
	 * Az ablak beállításainak meghatározása.
	 * A kapott int lista alapján beállítja a maximálisan
	 * és minimálisan megadható akna és mezőszámot.
	 * 
	 * @param aktualis - a kapott int lista alapján beállítja a default, 
	 * vagy az előző beállításban meghatározott értéket.
	 */
	public Settings(int[] aktualis) {
		
		//Comboboxok feltöltése
		for(int i=minM; maxM>=i; i++) {
			mineList[i-minM]=i;
		}
		for(int i=min; maxL+1>i; i++) {
			heigthList[i-min]=i;
		}
		for(int i=min; maxH+1>i; i++) {
			lengthList[i-min]=i;
		}
		//-------------------------------
		
		frame.setTitle("Beállítások");
		getContentPane().setLayout(new CardLayout(0, 0));		
		
		for(int i=0; 3>i; i++) {
			actual[i]= aktualis[i];
		}
		frame.getContentPane().add(new MenuPanel());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setSize(300,400);
	}
	
	/**
	 * Al-Menu-class, ami egy kibővített JPanel ActionListenerrel,
	 * a gombok és comboboxok tárolására és elhelyzésére.
	 * @author zsombor
	 *
	 */
	public class MenuPanel extends JPanel implements ActionListener{
		JLabel magassag = new JLabel("Magasság:");
		JLabel szelesseg = new JLabel("Szélesség:");
		JLabel aknak = new JLabel("Aknák száma:");
		
		JComboBox hList = new JComboBox(heigthList);
		JComboBox lList = new JComboBox(lengthList);
		JComboBox mList = new JComboBox(mineList);
		
		JButton okB = new JButton("Alkalmaz");
      	JButton megseB = new JButton("Mégse");
      	JButton defaultB =  new JButton("Default");
      	
      	JLabel nehezseg = new JLabel();
      	
      	
     
      	
		/**
		 * Konstruktorában elhelyezzük a gombokat, comboboxokat 
		 * és Listenereket adunk nekik.
		 */
		public MenuPanel() {
		 	
			setLayout(new GridLayout(5,2));
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            hList.setSelectedItem(actual[0]);
            lList.setSelectedItem(actual[1]);
            mList.setSelectedItem(actual[2]);
            
            JPanel magassagPanel = new JPanel(new GridBagLayout());
            magassagPanel.add(magassag, gbc);
            magassagPanel.add(hList, gbc);
            
            JPanel szelessegPanel = new JPanel(new GridBagLayout());
            szelessegPanel.add(szelesseg, gbc);
            szelessegPanel.add(lList, gbc);
            
            JPanel aknaPanel = new JPanel(new GridBagLayout());
            aknaPanel.add(aknak, gbc);
            aknaPanel.add(mList, gbc);
            
            JPanel buttonsPanel = new JPanel(new GridBagLayout());
            buttonsPanel.add(defaultB, gbc);
            buttonsPanel.add(okB, gbc);
            buttonsPanel.add(megseB, gbc);
            
            add(magassagPanel, gbc);
            add(szelessegPanel, gbc);
            add(aknaPanel, gbc);
           
            JPanel nehezsegPanel = new JPanel(new GridBagLayout());
            nehezsegPanel.add(nehezseg, gbc);
            
            add(nehezsegPanel, gbc);
            
            gbc.anchor = GridBagConstraints.SOUTH;
            add(buttonsPanel, gbc);
            
            gbc.weighty = 1;
            
            hList.addActionListener(this);
            lList.addActionListener(this);
            mList.addActionListener(this);
            
            okB.addActionListener(this);
            megseB.addActionListener(this);
            defaultB.addActionListener(this);
            labelSet();
            
		}

		/**
		 * Kötelezően létrehozott függvény. 
		 * A combobox modosítása után kiírja a nehézséget. 
		 * Az Alkalmaz gombra kattintva a comboboxokban látható értéket adjuk meg 
		 * aktuális értéknek, ez alapján fog kirajzolódni a játék.
		 * A Mégse gombra kilép a beállításokból, az adatok nem módosulnak.
		 * A Default gombra a beallítások visszamennek a megadott értékbe, jelen esetben
		 * 16x16-os mező 40 aknával, ami a Windowsos aknakereső standard közepesének felel meg.
		 * 
		 * @param e - A lenyomott gomb, vagy módosított combobox eventje.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			double minePercent = (int)mList.getSelectedItem()/ ((int) hList.getSelectedItem()* (int) lList.getSelectedItem());			
			if(e.getSource() == okB) { // Alkalmaz gomb
				if(0.25>minePercent) {
					actual[0]= (int) hList.getSelectedItem();
					actual[1]= (int) lList.getSelectedItem();
					actual[2]= (int) mList.getSelectedItem();
				
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				} else {
					JOptionPane.showMessageDialog(frame, "Rossz Mező/Akna arány", "Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
			if(e.getSource() ==defaultB) { // Deafult gomb
				actual[0]= defaultSettings[0];
				actual[1]= defaultSettings[1];
				actual[2]= defaultSettings[2];
				
				hList.setSelectedItem(defaultSettings[0]);
	            lList.setSelectedItem(defaultSettings[1]);
	            mList.setSelectedItem(defaultSettings[2]);
				
			}
			if(e.getSource() == megseB) { // Mégse gomb
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			if(e.getSource() == hList || e.getSource() == lList || e.getSource() == mList) { //Combobox modosítás Event
				labelSet();
			}
		}
		
		/**
		 * Combobox modosítása után kiírja a nehézségét a játéknak.
		 */
		public void labelSet() {
			if((int) mList.getSelectedItem()>=10 && 40>(int) mList.getSelectedItem()) {
				nehezseg.setText("<html><h2><font color='green'>könnyű</font></h2></html>");
			} else if((int) mList.getSelectedItem()>=40 && 75>(int) mList.getSelectedItem()) {
				nehezseg.setText("<html><h2><font color='orange'>közepes</font></h2></html>");
			} else if((int) mList.getSelectedItem()>=75) {
				nehezseg.setText("<html><h2><font color='red'>nehéz</font></h2></html>");
			}
		}
	}
	
	//Visszaadja az aktuálisan beallított mező és aknaszámot.
	public int[] getActual() {
		return actual;
	}
	
}