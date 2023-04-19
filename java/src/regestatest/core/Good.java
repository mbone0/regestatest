package regestatest.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * equivalent to the good table in the database.
 * represents an article that can be bought (depending on availability)
 * 
 */
public class Good
{


	
	int id;
	String name;
	

	public static int COLUMN_ID=1;
	public static int COLUMN_NAME=2;
	
	
	// TODO [priority: low] switch to enum
	public Good(int id, String name)
	{
		super();
		this.id = id;
		this.name = name;
	}
	public Good(ResultSet rs) throws SQLException
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
}
