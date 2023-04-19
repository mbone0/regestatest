package regestatest;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import regestatest.core.Good;
import regestatest.core.Sale;
import regestatest.core.SaleTime;
import regestatest.core.Supplier;
import regestatest.core.Supply;
import regestatest.core.dataexchange.OrderRequest;
import regestatest.core.dataexchange.OrderValidation;
import regestatest.lib.DBConnector;
import regestatest.lib.IO;
import regestatest.lib.QueryResult;

/**
 * Contains back-facing program logic: anything regarding communicating with the DB.
 */
public class ProblemDomain
{

	private static final String NO_RESULTS = "no results";

	/*
	 * todo [priority: average] Use a money library.
	 * 
	 * 
	 * the priority was selected according to this example-program's requirements,
	 * but obviously in any real-world scenario it would be high or even critical.
	 * 
	 * after thinking about it, this is not really a todo because we never do more
	 * than three operations on prices, therefore there's no risk of float
	 * corruption.
	 * 
	 * 
	 */
	

	private static final String SQL_SELECT_ALL_FROM = "select * from ";
	private static final String SQL_POPULATE_SALES_P2 = ".sale where supplier_id=";
	private static final String SQL_SUPPLY_P2 = ".supply";
	private static final String SQL_SUPPLIER_P2 = ".supplier";
	private static final String SQL_QUERY_GOODS_P2 = ".good";


	/**
	 * given database access infos and file paths, writes to file the necessary info 
	 * to populate the GUI from the database. This is done to avoid data duplication:
	 * instead of DB+GUI, we only have data on the DB.
	 */
	public static void getInfosForGUI(String server, String dbname, String user, String password, String pathGoods, String pathTimes)
			throws FileNotFoundException
	{
		ArrayList<Good> goods = new ArrayList<Good>();

		DBConnector db = connectDB(server, dbname, user, password);

		String query = SQL_SELECT_ALL_FROM + dbname + SQL_QUERY_GOODS_P2;

		QueryResult goodsTable = db.query(query);

		ResultSet rs = goodsTable.getResult();

		// populate the result
		try
		{
			while (rs.next())
			{
				goods.add(new Good(rs));
			}
		} catch (SQLException e)
		{
			goods.add(new Good(0, "null")); // base case handling

			// TODO [priority: low] write error to file. in case of more errors do nothing
			e.getStackTrace();
			e.getMessage();

		}

		goodsTable.close();

		// write the result
		ArrayList<String> res= new ArrayList<String>();
		for (Good i : goods)
		{
			res.add(Integer.toString(i.getId()));
			res.add(i.getName());
		}
		
		IO.writeToFile(res, true, pathGoods);

		
		// now get all the valid times and print them to file
		
		ArrayList<String> validTimes=new ArrayList<String>();
		for (SaleTime i : SaleTime.values())
		{
			validTimes.add(i.toString());
		}
		IO.writeToFile(validTimes, true, pathTimes);
				
		
	}

	private static DBConnector connectDB(String server, String dbname, String user, String password)
	{
		DBConnector db = new DBConnector(server, dbname, user, password);
		db.connect();
		return db;
	}

	/**
	 * Given database access info and a file path to write, returns on that file the result
	 * of the problem specified in the OrderRequest variable.
	 */
	public static void getSupplyForGUI(String server, String dbname, String user, String password, String filePath,
			OrderRequest requirements) throws Exception
	{
		// various initializations
		
		DBConnector db = connectDB(server, dbname, user, password);

		 ArrayList<Good> goods = populateGoods(db);
		ArrayList<Supplier> suppliers = populateSuppliers(db);
		ArrayList<Supply> supplies = populateSupplies(db);

		populateSales(suppliers, db);

		//find valid articles
		
		ArrayList<OrderValidation> preliminaryOffers = step1(requirements, supplies);
		ArrayList<OrderValidation> offersWithSales = step2(requirements, suppliers, preliminaryOffers);

		// find the correct answer among all and write it
		
		OrderValidation result = findBest(offersWithSales, requirements);

		if (result != null)
		{
			String supName = getSupplierById(suppliers, result.getSupplier()).getName();
			printResultToFile(result, supName, filePath);
		}
		else
		{
			IO.writeToFile(NO_RESULTS, filePath);
		}
	}

	private static Supplier getSupplierById(ArrayList<Supplier> suppliers, int id)
	{
		for (Supplier i : suppliers)
		{
			if (i.getId() == id)
			{
				return i;
			}
		}
		return null;
	}

	/**
	 * A part of the algorithm used to solve the problem: after getting the initial proposals 
	 * from the DB, we need to ask each supplier for applicable sales and apply them
	 * to the initial proposals. This works because sales are unique to the supplier, not the good. 
	 */
	private static ArrayList<OrderValidation> step2(OrderRequest requirements, ArrayList<Supplier> suppliers,
			ArrayList<OrderValidation> proposals)
	{
		ArrayList<OrderValidation> finalOffers = new ArrayList<OrderValidation>();

		for (OrderValidation i : proposals)
		{

			Supplier sup = getSupplierById(suppliers, i.getSupplier());

			OrderValidation proposalWithSale = sup.verifyandApplySale(i);

			finalOffers.add(proposalWithSale);
		}

		return finalOffers;

	}
	/**
	 * A part of the algorithm used to solve the problem: we get the initial proposals
	 * from the list of supplies that can solve the request 
	 */
	private static ArrayList<OrderValidation> step1(OrderRequest requirements, ArrayList<Supply> supplies)
	{
		ArrayList<OrderValidation> preliminaryOffers = new ArrayList<OrderValidation>();

		for (Supply i : supplies)
		{

			OrderValidation tmp = i.meetsOrder(requirements);
			if (tmp != null)
			{
				preliminaryOffers.add(tmp);
			}

		}
		return preliminaryOffers;
	}

	private static ArrayList<Supply> populateSupplies(DBConnector database)
	{
		String query = SQL_SELECT_ALL_FROM + database.getDbname() + SQL_SUPPLY_P2;

		QueryResult suppliesTable = database.query(query);

		ResultSet rs = suppliesTable.getResult();

		ArrayList<Supply> ret = new ArrayList<Supply>();

		try
		{
			while (rs.next())
			{
				Supply tmp = new Supply(rs);

				ret.add(tmp);
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ret.add(new Supply(0, 0, 0, 0, 0)); // edge cases handling

		suppliesTable.close();

		return ret;
	}

	private static OrderValidation findBest(ArrayList<OrderValidation> offersWithSales, OrderRequest requirements)
	{
		int bestIndex; // the index of the best proposal
		if (offersWithSales.size() == 0)
		{
			return null;
		}
		if (requirements.prefersSpeed()) // the best shipping time is selected, regardless of price
		{
			int bestShipment = offersWithSales.get(0).getShippingTime(); // initialize with the first
			bestIndex = 0;

			for (int i = 0; i < offersWithSales.size(); i++)
			{
				if (offersWithSales.get(i).getShippingTime() < bestShipment)
				{
					bestShipment = offersWithSales.get(i).getShippingTime();
					bestIndex = i;
				}
			}

		} else // the best price is selected, regardless of shipping time
		{
			float bestPrice = offersWithSales.get(0).getPrice(); // initialize with the first
			bestIndex = 0;

			for (int i = 0; i < offersWithSales.size(); i++)
			{
				if (offersWithSales.get(i).getPrice() < bestPrice)
				{
					bestPrice = offersWithSales.get(i).getPrice();
					bestIndex = i;
				}
			}
		}

		return offersWithSales.get(bestIndex);

	}

	private static void printResultToFile(OrderValidation result, String supplierName, String path)
			throws FileNotFoundException
	{
		String[] toprint = result.toStringForPresentation();
		String finals = toprint[0] + supplierName + toprint[1];
		IO.writeToFile(finals, path);

	}

	/**
	 * For each supplier, find its sales and add them
	 * 
	 * @throws Exception
	 */
	private static void populateSales(ArrayList<Supplier> suppliers, DBConnector database) throws Exception
	{

		for (Supplier i : suppliers)
		{
			String query = SQL_SELECT_ALL_FROM + database.getDbname() + SQL_POPULATE_SALES_P2 + i.getId();

			QueryResult qr = database.query(query);

			ResultSet rs = qr.getResult();

			try
			{
				while (rs.next())
				{

					Sale tmp = new Sale(rs);

					i.addSale(tmp);
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			qr.close();

		}

	}
	
	private static <T> ArrayList<T> populate(DBConnector database, Class type)
	//private static <T> ArrayList<T> populate(DBConnector database, Class<T> types)
	//private static <T> ArrayList<T> populate(DBConnector database, Type t)

	{
		//String query = SQL_SELECT_ALL_FROM + database.getDbname() + t.toString().toLowerCase();
		//QueryResult genericsTable = database.query(query);

		//ResultSet rs = genericsTable.getResult();

		//ArrayList<T> ret = new ArrayList<T>();
		
		//try
		//{
		//	while (rs.next())
			{

				//T tmp = new T(rs);
				
				//Object a= type.newInstance();
				//Constructor<?> c = type.getConstructor(String.class); 
				
				//ret.add(tmp);
			}
		//} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		//genericsTable.close();
		//return ret;
		return null;
	}
	
	private static ArrayList<Supplier> populateSuppliers(DBConnector database)
	{
		// TODO [priority: average] see if can reduce code duplication
		// with generics.
		// populate<T>
		// see the method "populate" for an attempt. 

		String query = SQL_SELECT_ALL_FROM + database.getDbname() + SQL_SUPPLIER_P2;

		QueryResult suppliersTable = database.query(query);

		ResultSet rs = suppliersTable.getResult();

		ArrayList<Supplier> ret = new ArrayList<Supplier>();

		try
		{
			while (rs.next())
			{

				Supplier tmp = new Supplier(rs);
				ret.add(tmp);
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ret.add(new Supplier(0, "")); // edge cases handling

		suppliersTable.close();

		return ret;
	}

	private static ArrayList<Good> populateGoods(DBConnector database)
	{
		String query = SQL_SELECT_ALL_FROM + database.getDbname() + SQL_QUERY_GOODS_P2;

		QueryResult goodsTable = database.query(query);

		ResultSet rs = goodsTable.getResult();

		ArrayList<Good> ret = new ArrayList<Good>();

		try
		{
			while (rs.next())
			{
				int id = rs.getInt(Good.COLUMN_ID);
				String name = rs.getString(Good.COLUMN_NAME);

				Good tmp = new Good(id, name);

				ret.add(tmp);
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ret.add(new Good(0, "")); // edge cases handling

		goodsTable.close();

		return ret;
	}

}
