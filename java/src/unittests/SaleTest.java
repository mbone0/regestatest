package unittests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import regestatest.core.Sale;
import regestatest.core.SaleType;

class SaleTest
{

	@Test
	public void testApplySale()
	{
		Sale tested = new Sale(5, SaleType.QUANTITY, 12);
		int defaultprice= 120;
		float actual = tested.applydiscount(defaultprice);
		float expected = 114;
		assertEquals(actual, expected);
				
	}
}
