package regestatest.core;

import java.sql.ResultSet;
import java.sql.SQLException;

import regestatest.core.dataexchange.OrderValidation;



/**
 * equivalent to the sale table in the database.
 * represents a better price applied when certain conditions are met.
 * 
 */
public class Sale
{
	/**
	 * the strength of the sale
	 */
	float percent; // todo maybe make an invariant:
					// can never be bigger than 100?
					// no, it's not stated in the requirements.
	/**
	 * The criterium of sale application
	 */
	SaleType type;
	/**
	 * the characteristic value of the sale, depending on its type. can be: int for
	 * QUANTITY float for BILL_TOTAL SaleTime for TIME
	 */
	Object SaleValue;

	public static int COLUMN_ID = 1;
	public static int COLUMN_PERCENT = 2;
	public static int COLUMN_TYPE = 3;
	public static int COLUMN_VALUE = 4;
	public static int COLUMN_SUPPLIER_ID = 5;

	private static final int FULL_PRICE_PERCENT = 100;

	public float getPercent()
	{
		return percent;
	}

	public SaleType getType()
	{
		return type;
	}

	public Object getSaleValue()
	{
		return SaleValue;
	}

	public Sale(float percent, SaleType type, Object saleValue)
	{
		this.percent = percent;
		this.type = type;
		SaleValue = saleValue;
	}

	public Sale(ResultSet rs) throws SQLException, Exception
	{
		float percent;
		SaleType type;
		Object value;

		percent = rs.getFloat(COLUMN_PERCENT);
		type = SaleType.valueOf(rs.getString(COLUMN_TYPE));
		switch (type)
		{
		case QUANTITY:

			value = rs.getInt(COLUMN_VALUE);

			break;

		case BILLTOTAL:
			value = rs.getInt(COLUMN_VALUE);
			break;

		case TIME:
			value = SaleTime.valueOf(rs.getString(COLUMN_VALUE));
			break;

		default:
			throw new Exception("");
		}

		this.percent = percent;
		this.type = type;
		SaleValue = value;

	}
	
	/**
	 * given an order, returns whether the sale can be applied to the order.
	 */
	public boolean isSaleApplicable(OrderValidation requirements)
	{
		SaleType key = getType();
		switch (key)
		{
		case QUANTITY:

			if (requirements.getQuanity() >= ((int) getSaleValue()))
			{
				return true;
				
			}
			break;

		case BILLTOTAL:

			if (requirements.getPrice() >= (int) getSaleValue())
			{
				return true;
			}
			break;

		case TIME:

			if (requirements.getOrderPlacementTime() != null)
			{
				if (requirements.getOrderPlacementTime().equals(getSaleValue()))
				{
					return true;
					
				}
			}
			break;

		default:
			throw new IllegalArgumentException("Unexpected value: " + key);
		}
		return false;
	}
	
	/**
	 * returns the discounted price of a given price, obtained with this sale.
	 */
	
	public float applydiscount(float defaultPrice)
	{
		float tmp = (defaultPrice * (FULL_PRICE_PERCENT - percent)) / FULL_PRICE_PERCENT;
		return tmp;
	}

}
