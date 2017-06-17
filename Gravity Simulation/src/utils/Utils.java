package utils;

import java.util.LinkedList;

import simulation.Main;
import simulation.Planet;
import simulation.Simulation;
import window.Window;

/**
 * Some utility methods
 * 
 * @author Jan Muskalla
 *
 */
public class Utils {
	
	/**
	 * returns the most massive planet from a collection of planets
	 * 
	 * if they have the same mass return the first one
	 * 
	 * @param planets
	 * @return the most massive planet
	 */
	public static Planet getBiggest(Planet... planets) {
		if (planets.length == 0)
			return null;
		Planet biggest = planets[0];
		for (Planet p: planets)
			if (p.getMass() > biggest.getMass())
				biggest = p;
		return biggest;
	}
	
	/**
	 * returns the least massive planet from a collection of planets
	 * 
	 * if they have the same mass return the last one
	 * 
	 * @param planets
	 * @return the least massive planet
	 */
	public static Planet getSmallest(Planet... planets) {
		if (planets.length == 0)
			return null;
		Planet smallest = planets[0];
		for (Planet p: planets)
			if (p.getMass() <= smallest.getMass())
				smallest = p;
		return smallest;
	}

	/**
	 * Returns the biggest planet in view. (does not work!)
	 * 
	 * @param planets
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return the biggest planet
	 */
	public static Planet getBiggestInView(Planet[] planets, Window win) {
		if (planets.length == 0)
			return null;

		Vec2D topLeft = win.transfromBack(new Vec2D(0, 0));
		Vec2D bottomRight = win.transfromBack(new Vec2D(win.getWidth(), win.getHeight()));

		double x1 = topLeft.x();
		double x2 = bottomRight.x();
		double y1 = topLeft.y();
		double y2 = bottomRight.y();

		LinkedList<Planet> planetsInView = new LinkedList<Planet>();

		for (Planet planet : planets) {
			Vec2D pos = planet.getPos();
			if (pos.x() > x1 && pos.x() < x2 && pos.y() > y1 && pos.y() < y2)
				planetsInView.add(planet);
		}
		
		if (planetsInView.isEmpty())
			return null;

		Planet biggest = planetsInView.getFirst();

		for (Planet p : planetsInView)
			if (p.getMass() > biggest.getMass())
				biggest = p;
		
		return biggest;
	}

	/**
	 * returns the orbital velocity as a vector in a position around a planet
	 * 
	 * @param planet
	 * @param position
	 * @return the orbital velocity
	 */
	public static Vec2D orbVel(Planet planet, Vec2D position) {
		// verbindungsvektor
		Vec2D r = planet.getPos().sub(position);

		// nach kreuzprodukt senkrechter vektor zu r
		Vec2D velDirection = new Vec2D(r.y(), -r.x());

		// normieren
		velDirection = velDirection.mult(1.0 / velDirection.norm());

		// anpassen
		return velDirection.mult(orbSpeed(planet, r.norm()));
	}

	/**
	 * returns the orbital speed of a planet
	 * 
	 * @param planet
	 * @param distance
	 * @return the orbital speed as a double
	 */
	public static double orbSpeed(Planet planet, double distance) {
		return Math.sqrt(Simulation.GRAV_CONST * planet.getMass() / distance);
	}

	/**
	 * momentum compensation
	 * 
	 * @param ownMass
	 * @param otherMass
	 * @param otherSpeed
	 * @return speed
	 */
	public static double momComp(double ownMass, double otherMass, double otherSpeed) {
		return -otherMass * otherSpeed / ownMass;
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

	/**
	 * @return randomly 1 or -1
	 */
	public static double plusMinus() {
		return Math.signum(Math.random() - 0.5);
	}
}
