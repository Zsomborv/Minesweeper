import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class ButtonTest {

	MyButton butt;
	MyButton min;
	
	@Before
	public void make() {
		butt = new MyButton();
		min = new MyButton();
		for(int i=0; 3>i; i++)
			butt.setNeighbours();
		min.setNeighbours(-1);
	}
	
	@Test
	public void testNeighbours() {
		Assert.assertEquals(3, butt.getNeighbours());
	}
	
	@Test
	public void testMine() {
		Assert.assertEquals(-1, min.getNeighbours());
	}

}
