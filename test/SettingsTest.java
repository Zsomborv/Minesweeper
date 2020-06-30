import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class SettingsTest {

	Settings s;
	int[] a = {16,16,40};
	
	@Before
	public void make() {
		s = new Settings(a);
	}
	
	@Test
	public void testActual() {
		Assert.assertEquals(a[1], s.getActual()[1]);
	}

}
