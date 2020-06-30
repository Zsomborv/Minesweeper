import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/*
 * A játékos adatait tároló osztály.
 */
public class PlayerData extends AbstractTableModel{

	//Játékos adatokat tároló lista
    List<Player> players = new ArrayList<>();

	@Override
	public int getRowCount() {
		return players.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int i, int i1) {
		Player player = players.get(i);
		switch(i1) {
			case 0: return player.getName();
			case 1: return player.getDiff();
			case 2: return player.getMines();
			default: return player.getTime();
		}
	}

	@Override
	public void addTableModelListener(TableModelListener tl) {
		super.addTableModelListener(tl); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public String getColumnName(int i)
	{
		switch(i)
		{
			case 0: return "Név";
			case 1: return "Nehézség";
			case 2: return "Talált aknák";
			default: return "Idő";
		}
	}
    
	@Override
	public Class<?> getColumnClass(int i)
	{
		switch(i)
		{
			case 0: return String.class;
			case 1: return String.class;
			case 2: return String.class;
			default: return Integer.class;
		}
	}

	@Override
	public boolean isCellEditable(int i, int i1)
	{
		boolean[] b={false,false,false,false};
		return (i1<getColumnCount() && i1>=0)?b[i1]:false;
	}

	public void addPlayer(String name, String diff, String mines,int time)
	{
		players.add(new Player(name, diff, mines, time));
		this.fireTableDataChanged();
	}
}
