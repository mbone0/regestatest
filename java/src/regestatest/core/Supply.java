package regestatest.core;

import regestatest.core.dataexchange.OrderValidation;

import java.sql.ResultSet;
import java.sql.SQLException;

import regestatest.core.dataexchange.OrderRequest;


/**
 * equivalent to the supply table in the database.
 * represents a buy possibility.
 * 
 */
public class Supply
{
	int good_id;
	int supplier_id;
	int stock;
	float price;
	int shippingTime;

	// public static int COLUMN_ID=1;
	public static int COLUMN_GOOD_ID = 2;
	public static int COLUMN_SUPPLIER_ID = 3;
	public static int COLUMN_STOCK = 4;
	public static int COLUMN_PRICE = 5;
	public static int COLUMN_SHIPPINGTIME = 6;

	public Supply(int good, int supplier, int shippingDays, int stock, float price)
	{
		this.good_id = good;
		this.supplier_id = supplier;
		this.shippingTime = shippingDays;
		this.stock = stock;
		this.price = price;
	}

	public Supply(ResultSet table) throws SQLException
	{
		int good= table.getInt(COLUMN_GOOD_ID);
		int supplier=table.getInt(COLUMN_SUPPLIER_ID);
		int shippingDays=table.getInt(COLUMN_SHIPPINGTIME);
		int stock=table.getInt(COLUMN_STOCK);
		float price=table.getFloat(COLUMN_PRICE);
		
		this.good_id = good;
		this.supplier_id = supplier;
		this.shippingTime = shippingDays;
		this.stock = stock;
		this.price = price;
		
	}

	/**
	 * given the order details, returns feasibility informations. can be null if
	 * criteria are not met
	 */
	public OrderValidation meetsOrder(OrderRequest requests)
	{
		float defaultPrice = price * requests.getQuanity();

		if (requests.getQuanity() > stock)
		{
			return null;
		} else
		{
			OrderValidation feasibility = new OrderValidation(requests.getGood(), requests.getQuanity(),
					requests.getOrderPlacementTime(), defaultPrice, shippingTime, supplier_id);
			return feasibility;
		}
	}

}
