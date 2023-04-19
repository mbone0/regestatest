package regestatest.lib;

import java.text.DecimalFormat;

public class Money
{
	public static final String ROUND_CONFIG_TWO_PLACES = "0.00";

	/**
	 * rounds a price according to the decimal configuration
	 */
	public static String roundForPresentation(float priceToRound, String roundConfig)
	{
		DecimalFormat df = new DecimalFormat(roundConfig);
		return df.format(priceToRound);
	}
}
