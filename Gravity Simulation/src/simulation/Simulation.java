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
	public static final double SPS = 8000; // simulations per second

	// the current constellation with start conditions
	private final Constellation constellation;
	// scale of the simulation
	private final double scale;
	// time step per simulation
	private double time;

	// all planets of the current simulation are saved here
	private Planet[] planets;

	// the time line in which all calculations happen
	private Timeline timeline;

	private int spsCounter; // counts the simulations per second
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

		// copies the planets of the new constellation in the local array
		// planets
		planets = new Planet[constellation.numberOfPlanets()];
		for (int i = 0; i < planets.length; i++)
			planets[i] = constellation.getPlanet(i).clone();

		run();
	}

	/**
	 * Moves the planets SPS times per second.
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
				if (p1.getPos().sub(p2.getPos()).norm() < p1.getRadius() + p2.getRadius()) {
					collide(i, j);
				}
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
	private void collide(int index1, int index2) {
		pause = true;

		Planet p1 = planets[index1];
		Planet p2 = planets[index2];
		Planet bigP, smallP;

		// get bigger planet
		if (p1.getMass() >= p2.getMass()) {
			bigP = p1;
			smallP = p2;
		} else {
			bigP = p2;
			smallP = p1;
		}

		// vel, mass and radius of the new planet
		bigP.setVel(Utils.getCollisionVel(bigP.getMass(), bigP.getVel(), smallP.getMass(), smallP.getVel()));
		bigP.setRadius(Utils.getCollisionRadius(bigP.getMass(), bigP.getRadius(), smallP.getMass()));
		bigP.setMass(bigP.getMass() + smallP.getMass());

		// the new planets array
		Planet[] newPlanets = new Planet[planets.length - 1];

		// copy all planets in the new planets array except the smaller one
		int j = 0;
		for (int i = 0; i < newPlanets.length; i++) {
			if (smallP.equals(planets[j]))
				j++;

			newPlanets[i] = planets[j];
			j++;
		}

		// check selected planet
		int planetID = -1;
		Boolean planetSelected = false;
		if (Main.window.getSelectedPlanet() != null) {
			planetSelected = true;
			if (Main.window.getSelectedPlanet().equals(smallP)) {
				planetID = bigP.getID();
			} else
				planetID = Main.window.getSelectedPlanet().getID();
			Main.window.deselectPlanet();
		}

		// copy the new planets in the simulation planets array
		planets = newPlanets;

		// select big planet if one of the colliding planets was selected
		if (planetSelected) {
			for (int i = 0; i < planets.length; i++) {
				if (planets[i].getID() == planetID) {
					Main.window.selectPlanet(planets[i]);
					break;
				}
			}
		}

		Main.window.updatePlanets();

		pause = false;
	}

	/**
	 * Adds a new planet to the simulation.
	 * 
	 * @param planet
	 *            the new planet
	 */
	public void addNewPlanet(Planet planet) {
		pause = true;

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

		pause = false;
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
