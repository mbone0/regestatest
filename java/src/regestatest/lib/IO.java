package regestatest.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IO
{

	/**
	 * waits until return is pressed
	 */
	public static void pause()
	{
		new java.util.Scanner(System.in).nextLine();
	}

	public static boolean deleteFile(String path)
	{
		File myObj = new File(path);
		return myObj.delete();
		
	}

	public static List<String> readFileLines(String path) throws IOException
	{
		Path pathObj = Paths.get(path);

		return Files.readAllLines(pathObj);

	}

	public static void writeToFile(String text, String path) throws FileNotFoundException
	{
		PrintWriter out = new PrintWriter(path);

		out.print(text);

		out.close();
	}

	/**
	 * @param autoNewline whether to put a newline after every element of the list
	 */
	public static void writeToFile(ArrayList<String> text, boolean autoNewline, String path)
			throws FileNotFoundException
	{
		String concat = "";
		for (String i : text)
		{
			concat += i;
			if (autoNewline)
			{
				concat += "\n";
			}
		}

		writeToFile(concat, path);
	}
}
