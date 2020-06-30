import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;





public class ListTest {

	PlayerData pd;
	
	@Before
	public void make() {
		pd = new PlayerData();
	}
	
	@Test
	public void testAdd() {
		pd.addPlayer("TestZs", "nehéz", "99\\99", 300);
		Assert.assertEquals(1, pd.getRowCount());
		
		Assert.assertEquals("TestZs", pd.getValueAt(0, 0));
		
	}

	@Test
	public void testColumns() {
		pd.addPlayer("TestZs", "nehéz", "99\\99", 300);
		Assert.assertEquals(4, pd.getColumnCount());
		Assert.assertEquals("Név", pd.getColumnName(0));
	}
}
