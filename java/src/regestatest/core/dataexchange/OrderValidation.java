package regestatest.core.dataexchange;

import regestatest.core.SaleTime;
import regestatest.lib.Money;



/**
 * A class for data exchange that represents the response to the question
 * in the form of a buying proposal.
 */
public class OrderValidation extends Request
{
	private static final String PRESENTATION_P2 = " for ";
	private static final String PRESENTATION_P1 = " units of the good can be purchased from the supplier ";
	float price;
	int shippingTime;
	int supplier;

	public float getPrice()
	{
		return price;
	}

	public int getShippingTime()
	{
		return shippingTime;
	}

	public int getSupplier()
	{
		return supplier;
	}

	public OrderValidation(int good, int quanity, SaleTime ordertime, float price, int shippingTime, int supplier)
	{
		super(good, quanity, ordertime);
		this.price = price;
		this.shippingTime = shippingTime;
		this.supplier = supplier;
	}

	public String[] toStringForPresentation()
	{
		String p1 = getQuanity() + PRESENTATION_P1;
		String p2 = PRESENTATION_P2 + Money.roundForPresentation(getPrice(), Money.ROUND_CONFIG_TWO_PLACES);
		String[] ar ={ p1, p2 };

		return ar;
	}

}
