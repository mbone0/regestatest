package regestatest;

import java.util.List;

/**
 * Data containing class for database access informations
 */
public class DBConfig
{

	// not static because one could have multiple DBs
	
	/**
	 * Server network path. May be localhost or an ip address
	 */
	public String serv;
	/**
	 * Database name in the server
	 */
	public String db;
	/**
	 * Username to access the database
	 */
	public String user;
	/**
	 * Password to access the database
	 */
	public String pass;

	private static final int ARG_SERV = 0;
	private static final int ARG_DB__ = 1;
	private static final int ARG_USER = 2;
	private static final int ARG_PASS = 3;

	public DBConfig(String serv, String db, String user, String pass)
	{
		this.serv = serv;
		this.db = db;
		this.user = user;
		this.pass = pass;
	}

	public DBConfig(List<String> cfgs)
	{
		this.serv = cfgs.get(ARG_SERV);
		this.db = cfgs.get(ARG_DB__);
		this.user = cfgs.get(ARG_USER);
		this.pass = cfgs.get(ARG_PASS);
	}

}
