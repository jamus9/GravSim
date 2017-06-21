package utils;

import java.util.LinkedList;
import javafx.scene.paint.Color;
import simulation.Planet;
import window.Window;

/**
 * Some utility methods
 * 
 * @author Jan Muskalla
 *
 */
public class Utils {
	
	public static final double GRAV_CONST = 6.67408e-11;

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
		for (Planet p : planets)
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
		for (Planet p : planets)
			if (p.getMass() <= smallest.getMass())
				smallest = p;
		return smallest;
	}

	/**
	 * returns the biggest planet on the screen
	 * 
	 * @param planets
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return the biggest planet
	 */
	public static Planet getBiggestInView(Window win, Planet... planets) {
		LinkedList<Planet> planetsInView = new LinkedList<Planet>();

		Vec2D topLeft = win.transfromBack(new Vec2D(0, 0));
		Vec2D bottomRight = win.transfromBack(new Vec2D(win.getX(), win.getY()));

		double x1 = topLeft.getX();
		double x2 = bottomRight.getX();
		double y1 = topLeft.getY();
		double y2 = bottomRight.getY();

		Vec2D pos;

		for (Planet planet : planets) {
			pos = planet.getPos();
			if (pos.getX() > x1 && pos.getX() < x2 && pos.getY() < y1 && pos.getY() > y2)
				planetsInView.add(planet);
		}

		return getBiggest(planetsInView.toArray(new Planet[planetsInView.size()]));
	}

	/**
	 * returns the orbital velocity as a vector in a position around a planet
	 * 
	 * @param planet
	 * @param position
	 * @return the orbital velocity as a vector
	 */
	public static Vec2D orbVel(Planet planet, Vec2D position) {
		// verbindungsvektor
		Vec2D r = planet.getPos().sub(position);

		// nach kreuzprodukt senkrechter vektor zu r
		Vec2D velDirection = new Vec2D(r.getY(), -r.getX());

		// normieren
		velDirection = velDirection.mult(1.0 / velDirection.norm());

		// anpassen
		return velDirection.mult(orbSpeed(planet, r.norm())).add(planet.getVel());
	}

	/**
	 * returns the orbital speed of a planet
	 * 
	 * @param planet
	 * @param distance
	 * @return orbital speed as a double
	 */
	public static double orbSpeed(Planet planet, double distance) {
		return Math.sqrt(GRAV_CONST * planet.getMass() / distance);
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
	 * Gives the past seconds in a nice time format.
	 * 
	 * @return the past time as a String
	 */
	public static String getTimeString(double seconds) {
		int secs = (int) seconds;
		int mins, hours, days, years;

		years = secs / 31536000;
		secs -= years * 31536000;
		days = secs / 86400;
		secs -= days * 86400;
		hours = secs / 3600;
		secs -= hours * 3600;
		mins = secs / 60;
		secs -= mins * 60;

		if (years > 0)
			return String.format("Y: %1$d D: %2$d", years, days);
		else if (days > 0)
			return String.format("Y: %1$d D: %2$d H: %3$d", years, days, hours);
		else if (hours > 0)
			return String.format("Y: %1$d D: %2$d H: %3$d M: %4$d", years, days, hours, mins);
		else
			return String.format("Y: %1$d D: %2$d H: %3$d M: %4$d S: %5$d", years, days, hours, mins, secs);
	}

	/**
	 * returns 1 or -1 randomly
	 * 
	 * @return 1 or -1
	 */
	public static double plusMinus() {
		return Math.signum(Math.random() - 0.5);
	}

	/**
	 * returns a random double in an interval
	 * 
	 * @param min
	 * @param max
	 * @return random double
	 */
	public static double getRandomInInervall(double min, double max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	/**
	 * returns a random rgb-color
	 * 
	 * @return a color
	 */
	public static Color getRandomColor() {
		return Color.color(Math.random(), Math.random(), Math.random());
	}

}
