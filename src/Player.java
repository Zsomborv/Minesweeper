import java.io.Serializable;

/*
 * Egy játékos adatait tároló osztály.
 * 
 */
public class Player implements Serializable {

    // Játékos neve
    private String name;

    // Játék nehézsége
    private String diffi;

    // Talált aknák
    private String mines;

    // Eltelt idő
    private int timePlayed;

    
    // Játékos nevének lekérdezése. 
    public String getName() {
        return name;
    }
    
    // Játékos nevének beállítása.
    public void setName(String name) {
        this.name = name;
    }
    
    // Játékos nehézségi fokának lekérdezése.
    public String getDiff() {
        return diffi;
    }
    
    // Játékos nehézségi fokának beállítása.
    public void setDiff(String diff) {
        this.diffi = diff;
    }
    
    // Játékos által talált aknák számának lekérdezése.
    public String getMines() {
        return mines;
    }
    
    // Játékos által talált aknák számának beállítása.
    public void setMines(String mines) {
        this.mines = mines;
    }

    // Játékos játékidejének lekérdezése.
    public Integer getTime() {
        return timePlayed;
    }
    
    // Játékos játékidejének beállítása.
    public void setTime(Integer timePlayed) {
	        this.timePlayed = timePlayed;
    }

    // Játékos létrehozása
    public Player(String name, String diff, String mines, Integer timeP) {
        this.name = name;
        this.diffi = diff;
        this.mines = mines;
        this.timePlayed = timeP;
    }
    
}
