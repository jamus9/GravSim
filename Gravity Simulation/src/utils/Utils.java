package utils;

import simulation.Main;

/**
 * Some utility methods
 * 
 * @author Jan Muskalla
 *
 */
public class Utils {

	/**
	 * Gives the time in a nice format.
	 * 
	 * @return the past time as a String
	 */
	public static String pastTime() {
		int secs = Main.simulation.getSecondsCounter();
		int mins, hours, days, years;

		years = secs / 31536000;
		secs -= years * 31536000;
		days = secs / 86400;
		secs -= days * 86400;
		hours = secs / 3600;
		secs -= hours * 3600;
		mins = secs / 60;
		secs -= mins * 60;

		String y = " Y: " + Integer.toString(years);
		String d = " D: " + Integer.toString(days);
		String h = " H: " + Integer.toString(hours);
		String m = " M: " + Integer.toString(mins);
		String s = " S: " + Integer.toString(secs);

		if (years > 0)
			return y + d;
		else if (days > 0)
			return d + h;
		else if (hours > 0)
			return h + m;
		else
			return m + s;
	}
}
