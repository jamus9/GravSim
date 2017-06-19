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
	public static final double SPS = 6000;

	/** the current constellation with start conditions */
	private final Constellation constellation;
	/** scale of the simulation */
	private final double scale;
	/** time step per simulation */
	private double time;

	/** all planets of the current simulation are saved here */
	private Planet[] planets;

	/** the time line in which all calculations happen */
	public Timeline timeline;

	private int spsCounter;
	private double secondsCounter;
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
		Vec2D accVec, dirVec, x, v, a;

		// array with an acceleration vector for each planet
		Vec2D[] planetsAcc = new Vec2D[planets.length];

		for (int i = 0; i < planets.length; i++) {
			// initialize array
			planetsAcc[i] = new Vec2D();
			
			for (int j = 0; j < planets.length; j++) {
				if (i != j) {
					// direction of the acceleration vector
					dirVec = (planets[j].getPos()).sub(planets[i].getPos());

					// acceleration vector: accVec = r * G * m / |r|^3
					accVec = dirVec.mult(GRAV_CONST * planets[j].getMass() / Math.pow(dirVec.norm(), 3));

					// add all acceleration vectors for one planet
					planetsAcc[i] = planetsAcc[i].add(accVec);
				}
			}
		}

		// change the position of all planets with the calculated vectors
		for (int i = 0; i < planets.length; i++) {
			x = planets[i].getPos();
			v = planets[i].getVel();
			a = planetsAcc[i];

			// v + a*t
			planets[i].setVel(v.add(a.mult(time)));
			// r + v*t + 1/2*a*t^2
			planets[i].setPos(x.add(v.mult(time)).add(a.mult(time * time * 0.5)));
		}
		
		spsCounter++;
		secondsCounter += time;
	}

	/**
	 * Checks all planets for collisions and calls collide if necessary
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
		bigP.setVel(getCollisionVel(bigP, smallP));
		bigP.setMass(bigP.getMass() + smallP.getMass(), bigP.getDensity());

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
	 * @param p1
	 * @param p2
	 * @return the new velocity
	 */
	private static Vec2D getCollisionVel(Planet p1, Planet p2) {
		double x = (p1.getMass() * p1.getVel().x() + p2.getMass() * p2.getVel().x()) / (p1.getMass() + p2.getMass());
		double y = (p1.getMass() * p1.getVel().y() + p2.getMass() * p2.getVel().y()) / (p1.getMass() + p2.getMass());
		return new Vec2D(x, y);
	}

	/**
	 * Adds a new planet to the simulation.
	 * 
	 * @param planet
	 *            the new planet
	 */
	public void addNewPlanet(Planet planet) {
		planet.deleteTrail();
		planet.savePosition();

		// copy all planets in the new planets array
		Planet[] newPlanets = new Planet[planets.length + 1];
		for (int i = 0; i < planets.length; i++)
			newPlanets[i] = planets[i];

		// the new planet in the last position
		newPlanets[newPlanets.length - 1] = planet;

		// update the planets array and window
		planets = newPlanets;
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

	public double getSecondsCounter() {
		return secondsCounter;
	}

	public Constellation getConstellation() {
		return constellation;
	}

	public double getScale() {
		return scale;
	}
}
