package regestatest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import regestatest.core.dataexchange.OrderRequest;
import regestatest.lib.IO;

/**
 * Contains front-facing program logic: anything regarding communicating with
 * the UI.
 */
public class Main
{

	public static final String ARGUMENT_EXCEPTION_MESSAGE = "use jerp://path=path/to/folder&command=command&good=good&quantity=quantity&speed=speed&time=time";
	public static final String URL_AND = "&";
	private static final String URL_COMMAND = URL_AND + "command=";
	private static final String URL_PATH = "path=";
	private static final String MAIN_MODES_SWITCH_REQUEST_SUPPLY = "request_supply";
	private static final String MAIN_MODES_SWITCH_GET_GUI = "get_gui_data";

	private static final String PATH_CONFIG = "/configuration/";
	private static final String PATH_OUT_GUI_GOODS = PATH_CONFIG + "out_goods.txt";
	private static final String PATH_OUT_GUI_TIMES = PATH_CONFIG + "out_times.txt";
	private static final String PATH_OUT_SUPPL = PATH_CONFIG + "out_supply.txt";
	private static final String PATH_DB_CONFIG = PATH_CONFIG + "dbconfig.txt";

	private static final String PROTOCOL = "jerp://"; // java enterprise resource planning

	/**
	 * decodes arguments and chooses what to do
	 */
	public static void main(String[] args) throws Exception
	{
		String url = args[0];
		String urldecoded = java.net.URLDecoder.decode(url, "UTF-8"); // remove %20 and put spaces

		String basePath = decodePath(urldecoded);
		String[] cmds = detectCommand(urldecoded);

		clearPreviousResults(basePath);

		String p = basePath + PATH_DB_CONFIG;
		List<String> dbconfig = IO.readFileLines(p);

		switch (cmds[0])
		{
		case MAIN_MODES_SWITCH_GET_GUI:

			getGUIInfos(new DBConfig(dbconfig), basePath);

			break;

		case MAIN_MODES_SWITCH_REQUEST_SUPPLY:

			OrderRequest r = OrderRequest.parse(cmds[1]);
			getSupply(new DBConfig(dbconfig), r, basePath);
			break;

		default:
			throw new IllegalArgumentException("Unexpected value: " + cmds[0]);
		}

	}

	/**
	 * deletes the files used for the previous answer, to avoid problems
	 */
	private static void clearPreviousResults(String basePath)
	{
		IO.deleteFile(basePath + PATH_OUT_GUI_GOODS);
		IO.deleteFile(basePath + PATH_OUT_GUI_TIMES);
		IO.deleteFile(basePath + PATH_OUT_SUPPL);
	}

	/**
	 * Given an URL, splits the command and the argument
	 */
	private static String[] detectCommand(String url) throws IOException
	{
		int begin = url.indexOf(URL_COMMAND) + URL_COMMAND.length();
		int end = url.indexOf(URL_AND, begin + 1) >= 0 ? url.indexOf(URL_AND, begin + 1) : url.length();
		// ^ if there's no more &, go to till the end of the string

		String command = url.substring(begin, end);

		String argument = url.substring(end);
		String[] ret = new String[2];

		/*
		 * for(int i=0; i<command.size();i++) { ret[i]=command.get(i); }
		 */

		ret[0] = command;
		ret[1] = argument;

		return ret;
	}

	/**
	 * Given the protocol URL, returns the path that contains all the files
	 */
	private static String decodePath(String url)
	{
		int index = url.indexOf(URL_PATH);

		if (index >= 0)
		{

			int indexNextpiece = url.indexOf("&", index + 1);

			int indexPath = index + URL_PATH.length();

			return url.substring(indexPath, indexNextpiece);
		} else
		{
			throw new IllegalArgumentException(ARGUMENT_EXCEPTION_MESSAGE);
		}
		
	}

	/**
	 * given the database, determines the best supply, writing the result to a file.
	 * 
	 */
	private static void getSupply(DBConfig db, OrderRequest req, String basePath) throws Exception
	{

		ProblemDomain.getSupplyForGUI(db.serv, db.db, db.user, db.pass, basePath + PATH_OUT_SUPPL, req);
	}

	/**
	 * given the database, pulls the goods' names needed by the web GUI, writing
	 * them to a text file
	 * 
	 */
	private static void getGUIInfos(DBConfig db, String basePath) throws SQLException, FileNotFoundException
	{
		ProblemDomain.getInfosForGUI(db.serv, db.db, db.user, db.pass, basePath + PATH_OUT_GUI_GOODS,
				basePath + PATH_OUT_GUI_TIMES);

	}

}
