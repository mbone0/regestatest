package regestatest.lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector
{
	private static final String CONNECTIONSTRING_PIECE1 = "jdbc:mysql://";
	private static final String CONNECTIONSTRING_PIECE2 = "/";
	private static final String CONNECTIONSTRING_PIECE3 = "?user=";
	private static final String CONNECTIONSTRING_PIECE4 = "&password=";

	private String server;
	private String dbname;
	private String user;
	private String pass;

	Connection conn = null;

	public String getDbname()
	{
		return dbname;
	}

	/**
	 * initializes a connection to a database, given access informations
	 */
	public DBConnector(String server, String dbname, String user, String password)
	{
		load();

		this.server = server;
		this.dbname = dbname;
		this.user = user;
		this.pass = password;
	}

	/**
	 * initializes the mySQL connector driver. internal use only.
	 */
	private void load()
	{
		try
		{
			// TODO [priority: low] check if there's no need to do this more than once
			// TODO [priority: low] remove the deprecated warning with a better solution

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex)
		{
			System.out.println("error initializing: " + ex.getMessage());
		}

	}

	/**
	 * Connects to a database. To be called prior to issuing queries.
	 */
	public void connect()
	{
		try
		{
			conn = DriverManager.getConnection(CONNECTIONSTRING_PIECE1 + server + CONNECTIONSTRING_PIECE2 + dbname
					+ CONNECTIONSTRING_PIECE3 + user + CONNECTIONSTRING_PIECE4 + pass);

		} catch (SQLException ex)
		{
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	/**
	 * Queries the database
	 */
	public QueryResult query(String query)
	{
		Statement stmt = null;
		ResultSet rs = null;

		try
		{
			stmt = conn.createStatement();

			// rs = stmt.executeQuery(query);

			// or alternatively, if you don't know ahead of time that the query will be a
			// SELECT...
			if (stmt.execute(query))
			{
				rs = stmt.getResultSet();
			}

		} catch (SQLException ex)
		{
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally
		{
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed
			
			// > done outside.

		}
		return new QueryResult(rs, stmt);
	}

}