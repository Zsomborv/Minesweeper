import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Maga a játék.
 * @author zsombor
 *
 */
@SuppressWarnings("serial")
public class MineSweeper extends JFrame implements ActionListener, MouseListener, Runnable{
	
	//Létrehozzuk a számlálókat és komponenseket, amit mindenkinek látnia kell az osztályban
	MyButton[][] field;
	Container grid = new Container();
	final int mine = -1;
	
	JPanel felsoPanel = new JPanel(new BorderLayout());
	JLabel timerLabel = new JLabel();
	JLabel counter= new JLabel();
	JLabel timerTim= new JLabel();
	
	JMenuBar mn = new JMenuBar();
	
	Timer tim;
	int count =1;
	
	int setting[] = {16,16,40};
	
	int mineCounter;
	int actualMindFound =0;
	
	
	/**
	 * Az időzítő miatt Threadben van futtatva, itt elhelyezzük 
	 * a már létrehozott játékmezőn az aknákat, majd a
	 * szomszédossági számlálót beállítjuk és elindítjuk az időzítőt.
	 * 
	*/
	@Override
	public void run() {
		mineMaker();
		neighbourMaker();
		tim = new Timer(500, this);
        tim.setInitialDelay(0);
        tim.start();
	}
	
	/**
	 * Konstruktor, létrehozza a játékmezőt, hozzáadja a Listenereket, 
	 * elhelyezi az akna és időszámlolót.
	 * Hozzáadja a JMenuBart, hogy játék közben is tudjunk pl. segítséget nézni.
	 * 
	 * @param settings - A Beállításokban vagy defaultként megadott mező és aknaszám.
	 */
	public MineSweeper(int[] settings) {
		this.setTitle("Minesweeper");
		
		setting = settings;
		mineCounter = setting[2];
		
		field = new MyButton[setting[0]][setting[1]];
		this.setSize(setting[1]*50,setting[0]*50);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		counter.setText("<html><h2>Maradék aknák: " + mineCounter + "</h2></html>");
		
		felsoPanel.add(timerLabel, BorderLayout.EAST);
		felsoPanel.add(counter, BorderLayout.WEST);
		
		grid.setLayout(new GridLayout(settings[0],settings[1]));
		
		for(int i = 0; setting[0]>i; i++) {
			for(int j=0; setting[1]>j; j++) {
				field[i][j]= new MyButton();
				JPanel jp = new JPanel(new GridBagLayout());
				jp.add(field[i][j]);
				jp.setSize(60,60);
				field[i][j].addMouseListener(this);
				grid.add(jp);
			}
		}
		//JMenuBar
		this.setJMenuBar(mn);
		JMenu f = new JMenu("File");
		mn.add(f);
		
		JMenuItem reset = new JMenuItem("Reset");
		f.add(reset);
		
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reset();	
			}
		});
		
		f.addSeparator();
		
		JMenuItem exit = new JMenuItem("Exit");
		f.add(exit);
		
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MineSweeper.this.dispatchEvent(new WindowEvent(MineSweeper.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		JMenu help = new JMenu("Help");
		mn.add(f);
		
		JMenuItem Help = new JMenuItem("Help");
		f.add(Help);
		
		Help.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HelpWindow hw;
					try {
						hw = new HelpWindow();
						hw.setVisible(true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}
		});
		
		help.add(Help);
		mn.add(help);
		//--------------------------------------------------------------------------------
		
		this.add(felsoPanel, BorderLayout.NORTH);
		this.add(grid, BorderLayout.CENTER);
		
	}
	
	/**
	 * JMenuBar/File/Reset gombra aktiválódó függvény
	 * Újraindítja  a játékot és a számlálót.
	 */
	public void reset() {
		MineSweeper ms = new MineSweeper(setting);
		ms.setVisible(true);
		Thread t = new Thread(ms);
		t.run();
		this.dispatchEvent(new WindowEvent(MineSweeper.this, WindowEvent.WINDOW_CLOSING));
	}
	
	/**
	 * Elhelyezi a beállításokban meghatározott számú aknákat véletlenszerűen.
	 */
	public void mineMaker() {
		
		//A lehetséges aknahelyek (kezdetben minden)
		ArrayList<Integer> feltolt = new ArrayList<Integer>();
		for(int i= 0; setting[0]>i; i++) {
			for(int j=0; setting[1]>j;j++) {
				feltolt.add(i*100+j);
			}
		}
		for(int i=0; setting[2]>i; i++) {
			int minePlace = (int)(Math.random()*feltolt.size());
			field[feltolt.get(minePlace)/100][feltolt.get(minePlace)%100].setNeighbours(mine); //Akna kiválasztása és betöltése
			feltolt.remove(minePlace); // Kivesszük a listából ahova már tettünk akna
		}
							
	}
	
	/**
	 * Minden egyes nem akna mezőnek bellítja a szomszédaiban lévő aknák száma szerint
	 * a saját értékét, ami alapján a jétkos játszhat.
	 */
	public void neighbourMaker() {
		for(int i= 0; setting[0]>i; i++) {
			for(int j=0; setting[1]>j;j++) {
				if(field[i][j].getNeighbours() != mine) { //Ha amit nézünk nem akna, számoljuk a szomszédos aknákat
					//Bal fent
					if(i > 0 && j > 0 && field[i-1][j-1].getNeighbours() == mine) { 
						field[i][j].setNeighbours();
					}
					//Fent
					if(j > 0 && field[i][j-1].getNeighbours() == mine) { 
						field[i][j].setNeighbours();
					}
					//Jobb fent
					if(setting[0] -1 > i && j > 0 && field[i+1][j-1].getNeighbours() == mine) { 
						field[i][j].setNeighbours();
					}
					
					//Balra
					if(i > 0 && field[i-1][j].getNeighbours() == mine) { 
						field[i][j].setNeighbours();
					}
					//Jobbra
					if(setting[0] -1 > i && field[i+1][j].getNeighbours() == mine) { 
						field[i][j].setNeighbours();
					}
					
					//Bal lent
					if(i > 0 && setting[1] -1 > j && field[i-1][j+1].getNeighbours() == mine) { 
						field[i][j].setNeighbours();
					}
					//Alatta
					if(setting[1] -1 > j && field[i][j+1].getNeighbours() == mine) { 
						field[i][j].setNeighbours();
					}
					//Jobb lent
					if(setting[0] -1 > i && setting[1] -1 > j && field[i+1][j+1].getNeighbours() == mine) { 
						field[i][j].setNeighbours();
					}
				}
			}
		}
	}
	
	/**
	 * Ellenőrző függvény, ha egy aknára már kattintottak, vége a játéknak,
	 * kirajzol az akna helyére egy megadott képet. 
	 */
	public void mineFound() { 
		
		for(int x=0; setting[0]>x; x++) {
			for(int y=0;setting[1]>y; y++) {
				field[x][y].setEnabled(false);
				if(field[x][y].getNeighbours() == mine) {
					
					try {
			    		Image img = ImageIO.read(getClass().getResource("mine.jpg"));
			    		field[x][y].setIcon(new ImageIcon(img));
			    					    		
			  		} catch (Exception ex) {
			    		System.out.println(ex);
			  		}
				}
			}
		}
		endGame(0);
	}
	
	/**
	 * Ha olyan mezőre kattintunk, aminek nincs a közelében akna, 
	 * a játék egyértelműsége miatt felfedjük a szomszédban lévő nem aknákat.
	 * Ha a szomszédban is ilyen 0 aknaszomszédos mező van, addig csináljuk, amíg a 
	 * "határokon" csak szám lesz.
	 *  
	 * @param x - a sor, amiben a 0-ra kattintottunk.
	 * @param y - az oszlop, amiben a 0-ra kattintottunk.
	 */
	public void zeroRecurser(int x, int y) {
		if(field[x][y].getNeighbours()== 0) 
			field[x][y].setText("");
		
		if(y>0) { //FELETTE
			if(field[x][y-1].getNeighbours() != mine && field[x][y-1].isEnabled()) {
				field[x][y-1].setText("<html><h2>" + field[x][y-1].getNeighbours()+ "</h2></html>");
				field[x][y-1].setEnabled(false);
				if(field[x][y-1].getNeighbours()== 0)

					zeroRecurser(x, y-1);
			}
		}
		if(x>0) { //BALRA
			if(field[x-1][y].getNeighbours() != mine && field[x-1][y].isEnabled()) {
				field[x-1][y].setText("<html><h2>" +field[x-1][y].getNeighbours()+ "</h2></html>");
				field[x-1][y].setEnabled(false);
				if(field[x-1][y].getNeighbours()== 0)

					zeroRecurser(x-1, y);
			}
		}
		if(setting[0]-1>x) { //JOBBRA
			if(field[x+1][y].getNeighbours() != mine && field[x+1][y].isEnabled()) {
				field[x+1][y].setText("<html><h2>" +field[x+1][y].getNeighbours()+ "</h2></html>");
				field[x+1][y].setEnabled(false);
				if(field[x+1][y].getNeighbours()== 0)
					zeroRecurser(x+1, y);
			}
		}
		if(setting[1]-1>y) { //ALATTA
			if(field[x][y+1].getNeighbours() != mine && field[x][y+1].isEnabled()) {
				field[x][y+1].setText("<html><h2>" +field[x][y+1].getNeighbours()+ "</h2></html>");
				field[x][y+1].setEnabled(false);
				if(field[x][y+1].getNeighbours()== 0)
					zeroRecurser(x, y+1);
			}
		}
		
		if(y>0 && x>0) { //BAL FELETTE
			if(field[x-1][y-1].getNeighbours() != mine && field[x-1][y-1].isEnabled()) {
				field[x-1][y-1].setText("<html><h2>" +field[x-1][y-1].getNeighbours()+ "</h2></html>");
				field[x-1][y-1].setEnabled(false);
				if(field[x-1][y-1].getNeighbours()== 0)
					zeroRecurser(x-1, y-1);
			}
		}
		if(y>0 && setting[0]-1>x) { //JOBB FELETTE
			if(field[x+1][y-1].getNeighbours() != mine && field[x+1][y-1].isEnabled()) {
				field[x+1][y-1].setText("<html><h2>" +field[x+1][y-1].getNeighbours()+ "</h2></html>");
				field[x+1][y-1].setEnabled(false);
				if(field[x+1][y-1].getNeighbours()== 0)
					zeroRecurser(x+1, y-1);
			}
		}
		if(setting[1]-1>y && x>0) { //BAL ALATTA
			if(field[x-1][y+1].getNeighbours() != mine && field[x-1][y+1].isEnabled()) {
				field[x-1][y+1].setText("<html><h2>" +field[x-1][y+1].getNeighbours()+ "</h2></html>");
				field[x-1][y+1].setEnabled(false);
				if(field[x-1][y+1].getNeighbours()== 0)
					zeroRecurser(x-1, y+1);
			}
		}
		if(setting[1]-1>y && setting[0]-1>x) { //JOBB ALATTA
			if(field[x+1][y+1].getNeighbours() != mine && field[x+1][y+1].isEnabled()) {
				field[x+1][y+1].setText("<html><h2>" +field[x+1][y+1].getNeighbours()+ "</h2></html>");
				field[x+1][y+1].setEnabled(false);
				if(field[x+1][y+1].getNeighbours()== 0)
					zeroRecurser(x+1, y+1);
			}
		}
		return;
	}

	/**
	 * Ha a játékos aknára lép, vagy felfedi az összes nemakna mezőt, a játék véget ér.
	 * Minden akna felfedődik, a játékosnak pedig lehetősége nyílik beírnia magát a Ranglistába
	 * Egy kis üzenet is jelzi a játékosnak hogy mi történt (Win/Lose, JOptionPane formátumban)
	 * 
	 * 
	 * @param vegeremeny - ez alapján tudjuk hogy nyert-e vagy nem.
	 */
	public void endGame(int vegeremeny) {
		JFrame winFrame = new JFrame("Pontszám");
		winFrame.setSize(400, 300);
		winFrame.setVisible(true);
		winFrame.setResizable(false);
		JTextField nameField = new JTextField();
		JLabel nameLabel = new JLabel("<html><h3>Add meg a neved: </html></h3>");
		JLabel mineLabel1 = new JLabel("<html><h3>Talált aknák: </html></h3>");
		JLabel mineLabel2 = new JLabel("<html><h3>" + (actualMindFound) + "\\" + setting[2] + "</html></h2>");
		JLabel timeLabel1= new JLabel("<html><h3>Eltelt idő: </html></h3>");
		JLabel timeLabel2 = new JLabel("<html><h3>" + timerTim.getText() + "</html></h3>\"");
		
		JButton nem = new JButton("<html><h3>Nem mentek</html></h3>");
		JButton ment = new JButton("<html><h3>Mentés</html></h3>");
		
		
		nem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				winFrame.dispatchEvent(new WindowEvent(winFrame, WindowEvent.WINDOW_CLOSING));
				MineSweeper.this.dispatchEvent(new WindowEvent( MineSweeper.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		ment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(nameField.getText().equals("")) {
					JOptionPane.showMessageDialog(winFrame,
						    "Kérlek adj meg egy nevet!",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				} else {
					
					ScoreBoardFrame sf = new ScoreBoardFrame();
			        sf.setVisible(false);
			        
			        String diffi = new String();
			        if(setting[2]>=75) {
			        	diffi= new String("nehéz");
			        } else if(setting[2]>= 40 && 75>setting[2]) {
			        	diffi = new String("közepes");
			        } else {
			        	diffi = new String("könnyű");
			        }
			        
			        sf.data.addPlayer(nameField.getText(), diffi, actualMindFound + "\\" + setting[2], count/2);
			        try {
						ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"));
						oos.writeObject(sf.data.players);
						oos.close();
					} catch(Exception ex) {
						ex.printStackTrace();
					}
			        sf.dispatchEvent(new WindowEvent(winFrame, WindowEvent.WINDOW_CLOSING));
					winFrame.dispatchEvent(new WindowEvent(winFrame, WindowEvent.WINDOW_CLOSING));
					MineSweeper.this.dispatchEvent(new WindowEvent( MineSweeper.this, WindowEvent.WINDOW_CLOSING));
				}
				
				
			}
		});
		
		JPanel panePanel = new JPanel(new GridLayout(4,2,2,2));
		
		panePanel.add(nameLabel); 
		panePanel.add(nameField);
		panePanel.add(timeLabel1); 
		panePanel.add(timeLabel2);
		panePanel.add(mineLabel1); 
		panePanel.add(mineLabel2); 
		panePanel.add(nem); panePanel.add(ment);
		
		winFrame.add(panePanel);
		if(vegeremeny == 1)
			JOptionPane.showMessageDialog(winFrame,
					"Gratulálok, nyertél!", "Win", JOptionPane.INFORMATION_MESSAGE);
		else JOptionPane.showMessageDialog(winFrame,
				"Sajnos aknára léptél!", "Akna", JOptionPane.ERROR_MESSAGE);
		tim.stop();
	}
	
	/**
	 * Ellenőrző függvény, hogy a felfedetlen mezők csak aknák-e.
	 * Ha igen, vége a játéknak, ha nem, megy tovább.
	 */
	public void checkWin() {
		int disabled=0;
		for(int i=0; setting[0]>i; i++) {
			for(int j=0;setting[1]>j; j++) {
				if(field[i][j].isEnabled()==false) {
					disabled++;
				}
			}
		}
		if(disabled==setting[0]*setting[1]-setting[2]) {
			this.setEnabled(false);
			endGame(1);
			
		}
	}
	
	/**
	 * A jobb és bal klikket külön kell kezelni, miven a játékos bal
	 * klikkel felfed egy mezőt, míg egy jobb klikkel megjelölheti aknának,
	 * még egy jobb klikkel pedig megkérdőjelezheti, ha nem biztos a dolgában.
	 * Zászlós jobb klikk hatására az aknaszámláló csökkenti magát, hogy a játékos tudja, 
	 * hogy mennyi akna van még hátra (Nem biztos, hogy tökrözi a valóságot).
	 * 
	 * @param e - jobb vagy bal klikk
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			for(int i=0; setting[0]>i; i++) {
				for(int j=0;setting[1]>j; j++) {
					if(e.getSource().equals(field[i][j])) {
						field[i][j].setEnabled(false);
						if(field[i][j].getNeighbours() == mine) {
							mineFound();
						}else if(field[i][j].getNeighbours()==0) {
								zeroRecurser(i,j);
							} else {						
							field[i][j].setText("<html><h2>" +field[i][j].getNeighbours()+ "</h2></html>");
							checkWin();
						}
						
					}			
				}
			}
		} else if(SwingUtilities.isRightMouseButton(e)) {
			for(int i=0; setting[0]>i; i++) {
				for(int j=0;setting[1]>j; j++) {
					if(e.getSource().equals(field[i][j])) {
						if(field[i][j].getFlag()) {
							if(field[i][j].getQuestioned()) {
								field[i][j].setQuestion(false);
								field[i][j].setFlag(false);
								field[i][j].setText("");
							} else {
								field[i][j].setQuestion(true);
								field[i][j].setText("<html><h2><font color='black'>?</font></h2></html>");
								if(setting[2]>mineCounter) 
									mineCounter++;
								if(field[i][j].getNeighbours() == -1) {
									actualMindFound++;
								}
							}
						} else {
							field[i][j].setFlag(true);
							field[i][j].setText("<html><h2><font color='red'>F</font></h2></html>");
							
							if(mineCounter > 0)
								mineCounter--;
						}
					}
				}
			}
			counter.setText("<html><h2>Maradék aknák: " + mineCounter + "</h2></html>");
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * A számláló label állítása, és a Nem mentés gomb lekezelése. 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals("nem")) {
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		count++;
        if (count < 100000) {
          timerLabel.setText("<html><h2>Eltelt idő: " + Integer.toString(count/2) + "</h2></html>");
          timerTim.setText(Integer.toString(count/2));
        } else {
          ((Timer) (e.getSource())).stop();
        }
	}
	
}
