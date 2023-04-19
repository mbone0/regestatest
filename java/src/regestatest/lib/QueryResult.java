package regestatest.lib;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Represents the output of the database.
 */
public class QueryResult
{
	
	private ResultSet result;
	private Statement smt;
	
	
	
	public QueryResult(ResultSet result, Statement smt)
	{
		super();
		this.result = result;
		this.smt = smt;
	}

	/**
	 * returns the MySQL connector object for the program to navigate it natively
	 */
	public ResultSet getResult()
	{
		return result;
	}

	public Statement getStatement()
	{
		return smt;
	}

	/**
	 * Free resources. Do not call other methods of this object after this.
	 * 
	 */
	public void close()
	{
		if (result != null)
		{
			try
			{
				result.close();
			} catch (SQLException sqlEx)
			{
			}

			result = null;
		}

		if (smt != null)
		{
			try
			{
				smt.close();
			} catch (SQLException sqlEx)
			{
			}

			smt = null;
		}
	}
	
	/**
	 * Converts a table from the native data representation format of mySQL to a more common arraylist.
	 * The elements of the list are rows/records.
	 */
	public ArrayList<String[]> resultSetAsList() throws SQLException
	{

		ArrayList<String[]> resultTable = new ArrayList<String[]>();

		while (result.next())
		{
			resultTable.add(rowAsList(result));
		}
		return resultTable;
	}

	/**
	 * Converts a row/record from the native data representation format of mySQL to a more common array.
	 * Remember to set the resultset's cursor to the first/subsequent position with next(), out of this method.
	 * @throws SQLException
	 */
	public static String[] rowAsList(ResultSet rs) throws SQLException
	{
		// to optimise, don't calculate num of columns every time, but outside of here and give the parameter?
		
		int numOfColumns = rs.getMetaData().getColumnCount();
		String[] rowAsList = new String[numOfColumns];

		for (int i = 0; i < numOfColumns; i++)
		{
			//System.out.println(i);
			//rs.getNString(i);
			rowAsList[i]= rs.getString(i+1); // <-- columns start at 1!
		}
		return rowAsList;
	}

}
