package simulation;

import constellations.Constellation;
import constellations.StartConditions;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import utils.Vec2D;

/**
 * Simulates the movement of the planets and handles the current planets and
 * constellation.
 * 
 * @author Jan Muskalla
 */
public class Simulation {

	public static final double GRAV_CONST = 6.67408e-11;
	public static final double SPS = 8000; // simulations per second
	public static int SPScounter = 0; // counts the simulations per second
	public static int secondsCounter = 0;
	public static boolean pause = true;

	// the current constellation with start conditions
	public static Constellation constellation = StartConditions.solarSystem;
	public static double scale; // scale of the simulation
	public static double time; // time step per simulation

	// all planets of the current simulation are saved here
	public static Planet[] planets;

	/**
	 * Loads a new given constellation.
	 * 
	 * @param newConstellation
	 */
	public static void loadConstellation(Constellation newConstellation) {
		pause = true;

		constellation = newConstellation;

		time = constellation.getTime();
		scale = constellation.getScale();

		// copies the planets of the new constellation in the local array
		// "planets"
		planets = new Planet[constellation.numberOfPlanets()];

		for (int i = 0; i < constellation.numberOfPlanets(); i++)
			planets[i] = constellation.getPlanet(i).clone();

		secondsCounter = 0;
		pause = false;
	}

	/**
	 * calls movePlanets() SPS times per second
	 */
	public static void run() {
		KeyFrame timeStep = new KeyFrame(Duration.seconds(1.0 / SPS), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (!pause)
					movePlanets();
			}
		});
		Timeline tl1 = new Timeline(timeStep);
		tl1.setCycleCount(Animation.INDEFINITE);
		tl1.play();
	}

	/**
	 * Calculate the sum of all acceleration vectors for each planet at the same
	 * simulation time and then moves all current planets one time step ahead.
	 */
	private static void movePlanets() {
		SPScounter++;
		secondsCounter += time;

		Vec2D accVec, dirVec;

		// array with an acceleration vector for each planet
		Vec2D[] planetsAcc = new Vec2D[planets.length];

		for (int i = 0; i < planets.length; i++) {
			planetsAcc[i] = new Vec2D();
			for (int j = 0; j < planets.length; j++) {
				if (i != j) {
					// direction of the acceleration vector
					dirVec = (planets[j].getPos()).sub(planets[i].getPos());

					// acceleration vector: accVec-> = r-> * G * m / r^3
					accVec = dirVec.mult(GRAV_CONST * planets[j].getMass() / Math.pow(dirVec.norm(), 3));

					// add all acceleration vectors for one planet
					planetsAcc[i] = planetsAcc[i].add(accVec);
				}
			}
		}

		double t = time;
		Vec2D pos0, vel0, acc0;

		// change the position of all planets with the calculated vectors
		for (int i = 0; i < planets.length; i++) {
			pos0 = planets[i].getPos();
			vel0 = planets[i].getVel();
			acc0 = planetsAcc[i];

			// v0 + a0 * t
			planets[i].setVel(vel0.add(acc0.mult(t)));
			// r0 + v0 * t + 1/2 * a0 * t^2
			planets[i].setPos(pos0.add(vel0.mult(t)).add(acc0.mult(t * t * 0.5)));
		}
	}

	/**
	 * Gives the time in a nice format.
	 * 
	 * @return the past time as a String
	 */
	public static String pastTime() {
		int secs = secondsCounter;
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
