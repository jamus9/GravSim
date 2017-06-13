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
	 * Transforms a vector from planet coordinates to screen coordinates.
	 * 
	 * @param vector
	 * @return the transformed vector
	 */
	public static Vec2D transform(Vec2D vector) {
		double x = Main.window.zoom * (Main.simulation.getScale() * vector.x() + Main.window.dx + Main.window.tempdx) + Main.window.winX / 2.0;
		double y = Main.window.zoom * (Main.simulation.getScale() * -vector.y() + Main.window.dy + Main.window.tempdy) + Main.window.winY / 2.0;
		return new Vec2D(x, y);
	}
	
	/**
	 * Transforms a vector from planet coordinates to screen coordinates.
	 * 
	 * @param vector
	 * @return the transformed vector
	 */
	public static Vec2D transfromBack(Vec2D vector) {
		double x = ((vector.x() - Main.window.winX / 2) / Main.window.zoom - Main.window.dx - Main.window.tempdx) / Main.simulation.getScale();
		double y = -((vector.y() - Main.window.winY / 2) / Main.window.zoom - Main.window.dy - Main.window.tempdy) / Main.simulation.getScale();
		return new Vec2D(x, y);
	}
	
	/**
	 * Calculates the new radius of a planet after a collision with the masses
	 * of both collided planets and the density of the bigger one.
	 * 
	 * @param bigMass
	 * @param bigRadius
	 * @param smallMass
	 * @return the new radius
	 */
	public static double getCollisionRadius(double bigMass, double bigRadius, double smallMass) {
		return bigRadius * Math.pow(1 + smallMass / bigMass, 1.0 / 3.0);
	}

	/**
	 * Calculates the new velocity of a planet after a collision with momentum
	 * conservation
	 * 
	 * @param mass1
	 * @param vel1
	 * @param mass2
	 * @param vel2
	 * @return the new velocity
	 */
	public static Vec2D getCollisionVel(double mass1, Vec2D vel1, double mass2, Vec2D vel2) {
		double x = (mass1 * vel1.x() + mass2 * vel2.x()) / (mass1 + mass2);
		double y = (mass1 * vel1.y() + mass2 * vel2.y()) / (mass1 + mass2);
		return new Vec2D(x, y);
	}

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
