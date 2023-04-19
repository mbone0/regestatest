package regestatest.core.dataexchange;

import java.util.List;

import regestatest.Main;
import regestatest.core.SaleTime;



/**
 * A class for data exchange that represents the request for an order 
 */
public class OrderRequest extends Request
{
	private static final String URL_TIME = Main.URL_AND+"time=";
	private static final String URL_SPEED = Main.URL_AND+"speed=";
	private static final String URL_QUANTITY = Main.URL_AND+"quantity=";
	private static final String URL_GOOD = Main.URL_AND+"good=";
	private static final int CONSTRUCTOR_TIME = 3;
	private static final int CONSTRUCTOR_PREFERSPEED = 2;
	private static final int CONSTRUCTOR_QUANTITY = 1;
	private static final int CONSTRUCTOR_GOOD = 0;
	boolean preferSpeed;
	
	public boolean prefersSpeed()
	{
		return preferSpeed;
	}

/**
 * 
 * @param good	the index of the requested good. use the GUI to determine it.
 * @param quanity the quantity of the wanted good.
 * @param preferSpeed whether the winning supply must be chosen by speed rather than price
 * @param orderTime the time the order will be forwarded to the supplier
 */
	public OrderRequest(int good, int quanity, boolean preferSpeed, SaleTime orderTime)
	{
		super(good, quanity, orderTime);
		this.preferSpeed = preferSpeed;
	}
	
	
	public static OrderRequest parse(String urlspecs)
	{
		
		String substring;
		
		int index=urlspecs.indexOf(URL_GOOD)+URL_GOOD.length();
		int finish=urlspecs.indexOf(Main.URL_AND, index+1);

		substring=urlspecs.substring(index, finish);
		
		int good=Integer.parseInt(substring);
				

		
		index=urlspecs.indexOf(URL_QUANTITY)+URL_QUANTITY.length();
		finish=urlspecs.indexOf(Main.URL_AND, index+1);
		
		substring=urlspecs.substring(index, finish);
		
		int quantity=Integer.parseInt(substring);
		
		index=urlspecs.indexOf(URL_SPEED)+URL_SPEED.length();
		finish=urlspecs.indexOf(Main.URL_AND, index+1);
		
		
		substring=urlspecs.substring(index, finish);
		
		boolean preferSpeed=Boolean.parseBoolean(substring);
		
		index=urlspecs.indexOf(URL_TIME)+URL_TIME.length();
		finish=urlspecs.length();
		
		substring=urlspecs.substring(index, finish);
		
		
		SaleTime time=SaleTime.valueOf(substring);
		
		return new OrderRequest(good, quantity, preferSpeed, time);
		
	}

	
	
}
