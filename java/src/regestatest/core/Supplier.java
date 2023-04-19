package regestatest.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import regestatest.core.dataexchange.OrderValidation;

/**
 * equivalent to the supplier table in the database. represents an actor that
 * sells goods.
 * 
 */
public class Supplier
{
	int id;
	String name;
	public static int COLUMN_ID = 1;
	public static int COLUMN_NAME = 2;

	ArrayList<Sale> sales = new ArrayList<Sale>();

	public void addSale(Sale item)
	{
		// don't add duplicates to avoid wrong results
		if (!sales.contains(item))
		{
			sales.add(item);
		}
	}

	public Supplier(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public Supplier(ResultSet rs) throws SQLException
	{
		this.id = rs.getInt(COLUMN_ID);
		this.name = rs.getString(COLUMN_NAME);
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	/**
	 * given the order, verifies if this supplier can provide sales, and if true,
	 * applies them on a returned object
	 */
	public OrderValidation verifyandApplySale(OrderValidation requirements)
	{
		if (requirements.getSupplier() != this.id)
		{
			return null;
		} else
		{
			ArrayList<Sale> applicableSales = getApplicableSales(requirements);

			float ultimatePrice = applySales(requirements.getPrice(), applicableSales);

			OrderValidation result = new OrderValidation(requirements.getGood(), requirements.getQuanity(),
					requirements.getOrderPlacementTime(), ultimatePrice, requirements.getShippingTime(),
					requirements.getSupplier());
			return result;
		}

	}

	/**
	 * given the order, determines which sales from this supplier can be applied
	 * 
	 */
	private ArrayList<Sale> getApplicableSales(OrderValidation requirements)
	{
		ArrayList<Sale> ret = new ArrayList<Sale>();

		for (Sale toBeAdded : sales)
		{
			if (toBeAdded.isSaleApplicable(requirements))
			{
				ret.add(toBeAdded);

				if (!toBeAdded.getType().equals(SaleType.TIME))
				{
					deduplicateSale(ret, toBeAdded);
				}

				// by the (implicit) requirements, there cannot be
				// two or more sale of this type
				// active at the same time, because
				// the periods of time are months, therefore are enums,
				// and enums are mutually exclusive
			}

		}

		return ret;
	}

	/**
	 * Removes sales of the same type of the parameter if they are weaker: sales of
	 * the same type cannot be cumulated (implicit requirement)
	 */
	private void deduplicateSale(ArrayList<Sale> list, Sale recentlyAdded)
	{
		// find if this supersedes another quantity sale and replace it

		for (Sale alreadyAdded : list)
		{
			if (alreadyAdded.getType().equals(recentlyAdded.getType()) && alreadyAdded != recentlyAdded)
			{
				// ^ if there is a quantity sale
				// v and is worse than the current one

				if (((int) recentlyAdded.SaleValue) > ((int) alreadyAdded.SaleValue))
				{
					// we remove the old one
					list.remove(alreadyAdded);
				} else // this sale is not the best, we remove it
				{
					list.remove(recentlyAdded);
				}
			}
		}

	}

	/**
	 * given a normal price, returns the discounted price after all the wanted sales
	 * have been applied
	 */
	private float applySales(float basePrice, ArrayList<Sale> sales)
	{
		float returnprice = basePrice;

		if (sales != null)
		{
			for (Sale i : sales)
			{// non applica piu di 1 sconto perche usa var sbagliata
				returnprice = i.applydiscount(returnprice);
			}
		}
		return returnprice;
	}

}
