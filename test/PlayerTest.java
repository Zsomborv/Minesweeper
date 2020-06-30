import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayerTest {

	Player p;
	
	@Before
	public void make() {
		p = new Player("TestZs", "nehéz", "99\\99", 300);
	}
	
	@Test
	public void testName() {
		Assert.assertEquals("TesztZs", p.getName());
	}
	
	@Test
	public void testMines() {
		Assert.assertEquals("99\\99", p.getMines());
	}
	
	@Test
	public void testTime() {
		Assert.assertEquals("300", p.getTime());
	}
	
	@Test
	public void testDiff() {
		Assert.assertEquals("nehéz", p.getDiff());
	}

}
