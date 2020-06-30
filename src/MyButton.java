import java.awt.Dimension;

import javax.swing.JButton;

/**
 * Saját gombtípus, hogy értékeket tudjon tárolni magában.
 * @author zsombor
 *
 */
public class MyButton extends JButton{

	Dimension size = new Dimension(50,50);
	
	int neighbours; //szomszédos aknák száma
	boolean marked; // megjelölés aknaként
	boolean question; //megjelölés kérdéses aknaként
	
	/**
	 * Konstruktor, beállítja az alapértékeket.
	 */
	public MyButton() {
		
		this.setSize(50, 50);
		neighbours =0;
		marked= false;
		question=false;
		
		this.setBorderPainted(true);
		this.setFocusable(true);
	}
	
	// Egy mező szomszédos aknáinak száma.
	public void setNeighbours() {
		neighbours++;
	}
	
	/**
	 * Ha egy mező akna, átállítjuk annak.
	 * Egy mező akkor akna, ha szomszédainak értéke -1.
	 * 
	 * @param mine - Nem számoljuk a szomszédokat, mert akna.
	 */
	public void setNeighbours(int mine) {
		neighbours = mine;
	}
	
	// Egy mező szomszédos aknáinak számának lekérdezése.
	public int getNeighbours() {
		return neighbours;
	}
	
	// Egy mező zászlózásának beállítása.
	public void setFlag(boolean b) {
		marked=b;
	}
	
	// Egy mező zászlózottságának lekérdezése.
	public boolean getFlag() {
		return marked;
	}
	
	// Egy mező kérdésességének beállítása.
	public void setQuestion(boolean b) {
		question=b;
	}
	
	// Egy mező kérdésességének lekérdezése.
	public boolean getQuestioned() {
		return question;
	}
	
	//Alapból létrehozott függvények a méret beállítására
	@Override
	public Dimension getPreferredSize() {
		return size;
		
	}

	@Override
	public Dimension getMaximumSize() {
		return size;
		
	}
	@Override
	public Dimension getMinimumSize() {
		return size;
		
	}
	
}
