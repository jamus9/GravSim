package utils;

import java.util.LinkedList;

import bodies.Body;
import bodies.Particle;
import bodies.Planet;
import javafx.scene.paint.Color;
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
	 * @return the most massive planet
	 */
	public static Planet getBiggestInView(Window win, Planet... planets) {
		LinkedList<Planet> planetsInView = new LinkedList<Planet>();

		Vec2d topLeft = win.transfromBack(new Vec2d(0, 0));
		Vec2d bottomRight = win.transfromBack(new Vec2d(win.getWidth(), win.getHeight()));

		double x1 = topLeft.getX();
		double x2 = bottomRight.getX();
		double y1 = topLeft.getY();
		double y2 = bottomRight.getY();

		Vec2d pos;

		for (Planet planet : planets) {
			pos = planet.getPos();
			if (pos.getX() > x1 && pos.getX() < x2 && pos.getY() < y1 && pos.getY() > y2)
				planetsInView.add(planet);
		}

		return getBiggest(planetsInView.toArray(new Planet[planetsInView.size()]));
	}

	/**
	 * return a random position vector inside a disk around a planet
	 * 
	 * @param parent
	 * @param rMin
	 * @param rMax
	 * @return the random position vector
	 */
	public static Vec2d getRandomOrbitPosition(Planet parent, double rMin, double rMax) {
		double angle = getRandomInInervall(0, 360);
		double radius = getRandomInInervall(rMin, rMax);

		return (new Vec2d(new PolarVec2d(radius, angle))).add(parent.getPos());
	}

	/**
	 * returns the orbital velocity as a vector in a position around a planet
	 * 
	 * @param parent
	 * @param position
	 * @return the orbital velocity as a vector
	 */
	public static Vec2d getOrbitalVelocityCircular(Planet parent, Body body) {

		// connection vector
		Vec2d r = parent.getPos().sub(body.getPos());

		// normal vector to r
		Vec2d velDirection = new Vec2d(r.getY(), -r.getX());

		// norm and multiply with orbital speed
		return velDirection.getDir().mult(orbSpeedCircular(parent, r.norm())).add(parent.getVel());
	}

	/**
	 * returns the orbital velocity as a vector in a position around a planet
	 * 
	 * @param parent
	 * @param position
	 * @return the orbital velocity as a vector
	 */
	public static Vec2d getOrbitalVelocityElliptical(Planet parent, Body body, double sma) {
	
		// connection vector
		Vec2d r = parent.getPos().sub(body.getPos());
	
		// normal vector to r
		Vec2d velDirection = new Vec2d(r.getY(), -r.getX());
	
		// norm and multiply with orbital speed
		return velDirection.getDir().mult(orbSpeedElliptical(parent, r.norm(), sma)).add(parent.getVel());
	}

	/**
	 * returns the orbital speed of a planet in a circular orbit
	 * 
	 * @param planet
	 * @param distance
	 * @return orbital speed as a double
	 */
	public static double orbSpeedCircular(Planet planet, double distance) {
		return Math.sqrt(GRAV_CONST * planet.getMass() / distance);
	}

	/**
	 * returns the orbital speed of a planet in an elliptical orbit
	 * 
	 * @param parent
	 * @param sma
	 *            the semi major axis of the orbit
	 * @param distance
	 * @return orbital speed as a double
	 */
	public static double orbSpeedElliptical(Planet parent, double distance, double sma) {
		return Math.sqrt(GRAV_CONST * parent.getMass() * (2 / distance - 1 / sma));
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
	
	public static Vec2d momComp(Planet p1, Planet p2) {
		return p2.getVel().mult(p2.getMass()).mult(-1.0 / p1.getMass());
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
		if (Math.random() - 0.5 < 0)
			return -1;
		else
			return 1;
	}

	/**
	 * returns a random double in an interval
	 * 
	 * @param min
	 * @param max
	 * @return random double
	 */
	public static double getRandomInInervall(double min, double max) {
		return min + (Math.random() * (max - min));
	}

	/**
	 * returns a random RGB-color
	 * 
	 * @return a color
	 */
	public static Color getRandomColor() {
		return Color.color(Math.random(), Math.random(), Math.random());
	}

	/**
	 * concatenates two particle arrays
	 * 
	 * @param array1
	 * @param array2
	 * @return the new array
	 */
	public static Particle[] concatParticles(Particle array1[], Particle array2[]) {
		int l1 = array1.length;
		int l2 = array2.length;
		Particle concat[] = new Particle[l1 + l2];

		// copy the first array in the first half
		for (int i = 0; i < l1; i++)
			concat[i] = array1[i];

		// copy the second array in the second half
		for (int i = 0; i < l2; i++)
			concat[l1 + i] = array2[i];

		return concat;
	}

}
