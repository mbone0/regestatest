package regestatest.core.dataexchange;

import regestatest.core.SaleTime;


/**
 * A class for data exchange that contains the most common data
 */

public class Request
{
	int good;
	int quanity;
	/**
	 * the time the request is sent to the supplier.
	 */
	SaleTime orderPlacementTime;
	
	public int getGood()
	{
		return good;
	}
	public int getQuanity()
	{
		return quanity;
	}
	public SaleTime getOrderPlacementTime()
	{
		return orderPlacementTime;
	}
	public void setOrderPlacementTime(SaleTime orderPlacementTime)
	{
		this.orderPlacementTime = orderPlacementTime;
	}
	public Request(int good, int quanity, SaleTime orderPlacementTime)
	{
		super();
		this.good = good;
		this.quanity = quanity;
		this.orderPlacementTime = orderPlacementTime;
	}
	
	
	
	
	
}
