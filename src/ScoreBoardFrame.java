import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

/**
 * A Ranglista ablak
 *
 */
public class ScoreBoardFrame extends JFrame {

	/*
     * Ebben az objektumban vannak a j�t�kosok adatai.
     * A program indul�s ut�n bet�lti az adatokat f�jlb�l, bez�r�skor pedig kimenti oda.
     */	
    public PlayerData data;

    /**
     * T�bl�zat l�trehoz�sa
     */
    private void initComponents() {
        this.setLayout(new BorderLayout());
		//t�bl�zat
		JTable jt=new JTable(data);
		jt.setFillsViewportHeight(rootPaneCheckingEnabled);
		jt.setRowSorter(new TableRowSorter<PlayerData>(data));
		this.add(new JScrollPane(jt),BorderLayout.CENTER);
    }

    /**
     * Konstruktor, indul�skor bet�lti, bez�r�skor kimenti az adatokat.
     */
    @SuppressWarnings("unchecked")
    public ScoreBoardFrame() {
        super("ScoreBoard");
        try {
            data = new PlayerData();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"));
            data.players = (List<Player>)ois.readObject();
            ois.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					try {
						ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"));
						oos.writeObject(data.players);
						oos.close();
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			});
        //ablak fel�p�t�se
        setMinimumSize(new Dimension(500, 200));
        initComponents();
    }

}
