package simulation;

import constellations.Constellation;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import utils.Utils;
import utils.Vec2D;

/**
 * Simulates the movement of the planets and handles the current planets and
 * constellation.
 * 
 * @author Jan Muskalla
 */
public class Simulation {

	public static final double GRAV_CONST = 6.67408e-11;
	/** simulations per second */
	public static final double SPS = 8000;

	/** the current constellation with start conditions */
	private final Constellation constellation;
	/** scale of the simulation */
	private final double scale;
	/** time step per simulation */
	private double time;

	/** all planets of the current simulation are saved here */
	private Planet[] planets;

	/** the time line in which all calculations happen */
	private Timeline timeline;

	private int spsCounter;
	private int secondsCounter;
	private boolean pause;

	/**
	 * Creates a new simulation with a given constellation.
	 * 
	 * @param constellation
	 */
	public Simulation(Constellation constellation) {
		pause = false;
		spsCounter = 0;
		secondsCounter = 0;

		this.constellation = constellation;
		time = constellation.getTime();
		scale = constellation.getScale();

		/*
		 * copies the planets of the new constellation in the local array
		 * planets
		 */
		planets = new Planet[constellation.numberOfPlanets()];
		for (int i = 0; i < planets.length; i++)
			planets[i] = constellation.getPlanet(i).clone();

		run();
	}

	/**
	 * Moves the planets SPS times per second and checks for collisions.
	 */
	private void run() {
		KeyFrame timeStep = new KeyFrame(Duration.seconds(1.0 / SPS), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (!pause) {
					movePlanets();
					checkForCollisions();
				}
			}
		});
		timeline = new Timeline(timeStep);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	/**
	 * Calculate the sum of all acceleration vectors for each planet at the same
	 * simulation time and then moves all current planets one time step ahead.
	 */
	private void movePlanets() {
		spsCounter++;
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
	 * Checks all planets for collisions.
	 */
	private void checkForCollisions() {
		Planet p1, p2;
		for (int i = 0; i < planets.length - 1; i++) {
			for (int j = i + 1; j < planets.length; j++) {
				p1 = planets[i];
				p2 = planets[j];
				if (p1.getPos().sub(p2.getPos()).norm() < p1.getRadius() + p2.getRadius())
					collide(p1, p2);
			}
		}
	}

	/**
	 * Collides two planets and creates a new array without the smaller collided
	 * planet. The bigger collided planet gets changed after the collision.
	 * 
	 * @param index1
	 *            the index of the collided planet 1
	 * @param index2
	 *            the index of the collided planet 2
	 */
	private void collide(Planet p1, Planet p2) {
		// get the bigger planet
		Planet bigP = Utils.getBiggest(p1, p2);
		Planet smallP = Utils.getSmallest(p1, p2);

		// velocity, mass and radius of the new planet
		bigP.setVel(getCollisionVel(bigP.getMass(), bigP.getVel(), smallP.getMass(), smallP.getVel()));
		bigP.setRadius(getCollisionRadius(bigP.getMass(), bigP.getRadius(), smallP.getMass()));
		bigP.setMass(bigP.getMass() + smallP.getMass());

		// copy all planets in the new planets array except the smaller one
		Planet[] newPlanets = new Planet[planets.length - 1];
		for (int i = 0, j = 0; i < newPlanets.length; i++) {
			if (smallP.equals(planets[j]))
				j++;
			newPlanets[i] = planets[j++];
		}

		// check selected planet
		Planet selPl = Main.window.getSelectedPlanet();
		if (selPl != null && selPl.equals(smallP))
			Main.window.selectPlanet(bigP);

		// copy the new planets in the simulation planets array
		planets = newPlanets;

		// update window
		Main.window.updatePlanets();
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
	private static Vec2D getCollisionVel(double mass1, Vec2D vel1, double mass2, Vec2D vel2) {
		double x = (mass1 * vel1.x() + mass2 * vel2.x()) / (mass1 + mass2);
		double y = (mass1 * vel1.y() + mass2 * vel2.y()) / (mass1 + mass2);
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
	private static double getCollisionRadius(double bigMass, double bigRadius, double smallMass) {
		return bigRadius * Math.pow(1 + smallMass / bigMass, 1.0 / 3.0);
	}

	/**
	 * Adds a new planet to the simulation.
	 * 
	 * @param planet
	 *            the new planet
	 */
	public void addNewPlanet(Planet planet) {
		planet.deleteOrbit();
		planet.savePosition();

		// the new planets array
		Planet[] newPlanets = new Planet[planets.length + 1];

		// copy all planets in the new planets array
		for (int i = 0; i < planets.length; i++)
			newPlanets[i] = planets[i];

		// the new planet in the last position
		newPlanets[newPlanets.length - 1] = planet;

		// update the planets array
		planets = newPlanets;

		// update the window
		Main.window.updatePlanets();
	}

	public Planet[] getPlanets() {
		return planets;
	}

	public boolean isPaused() {
		return pause;
	}

	public void setPause(boolean b) {
		pause = b;
	}

	public double getTime() {
		return time;
	}

	public void multTime(double x) {
		time *= x;
	}

	public void resetTime() {
		time = constellation.getTime();
	}

	public int getSpsCounter() {
		return spsCounter;
	}

	public void resetSpsCounter() {
		spsCounter = 0;
	}

	public int getSecondsCounter() {
		return secondsCounter;
	}

	public Constellation getConstellation() {
		return constellation;
	}

	public double getScale() {
		return scale;
	}
}
