/**
 * 
 */
package unittests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import regestatest.core.Sale;
import regestatest.core.SaleType;
import regestatest.core.Supplier;
import regestatest.core.dataexchange.OrderValidation;

class SupplierTest
{

	Supplier tested= new Supplier(1, "sup");
	
	
	@BeforeClass
    public static void setUpClass() 
	{
		
    }
	/**
	 * Test method for {@link regestatest.core.Supplier#verifyandApplySale(regestatest.core.dataexchange.OrderValidation)}.
	 */
	@Test
	void testVerifySale()
	{
		tested.addSale(new Sale(5,SaleType.QUANTITY, 50));
		
		OrderValidation act= tested.verifyandApplySale(new OrderValidation(0, 49, null, 100*49, 5, 1));

		assertEquals(4900, act.getPrice());
		
		act= tested.verifyandApplySale(new OrderValidation(0, 50, null, 100*50, 5, 1));
		
		assertEquals(4750, act.getPrice());

		
	}

}
