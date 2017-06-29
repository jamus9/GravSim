package simulation;

import bodies.Body;
import bodies.Constellation;
import bodies.Particle;
import bodies.Planet;
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
 * 
 */
public class Simulation {

	/** simulations per second */
	public static final double SPS = 2000;

	/** the current constellation with start conditions */
	private final Constellation constellation;

	/** scale of the simulation */
	private final double scale;

	/** time step per simulation */
	private double time;

	/** all planets of the current simulation are saved here */
	private Planet[] planets;

	/** all particles of the current simulation */
	private Particle[] particles;

	/** the time line in which all calculations happen */
	public Timeline timeline;

	/** time counter */
	private int spsCounter;
	private double secondsCounter;

	/**
	 * Creates a new simulation with a given constellation.
	 * 
	 * @param constellation
	 */
	public Simulation(Constellation constellation) {
		spsCounter = 0;
		secondsCounter = 0;

		this.constellation = constellation;
		time = constellation.getTime();
		scale = constellation.getScale();

		// copy the planets of the new constellation in the local array
		planets = new Planet[constellation.numberOfPlanets()];
		for (int i = 0; i < planets.length; i++)
			planets[i] = constellation.getPlanet(i).clone();

		// copy the particles of the new constellation in the local array
		particles = new Particle[constellation.numberOfParticles()];
		for (int i = 0; i < particles.length; i++)
			particles[i] = constellation.getParticle(i).clone();

		timeline = new Timeline();
	}

	/**
	 * Moves the planets SPS times per second and checks for collisions.
	 */
	public void run() {
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.0 / SPS), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				moveBodies();
				checkForCollisions();
				spsCounter++;
				secondsCounter += time;
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	/**
	 * Calculate the sum of all acceleration vectors for each body at the same
	 * simulation time and then moves all bodies one time step.
	 */
	private void moveBodies() {
		// add all acceleration vectors in one time step
		for (int i = 0; i < planets.length; i++)
			for (int j = 0; j < planets.length; j++)
				if (i != j)
					planets[i].addAcc(getAccVec(planets[i], planets[j]));

		// update all planets
		for (Planet planet : planets)
			updateBodyProps(planet);

		// add all acceleration vectors in one time step
		for (Particle particle : particles) {
			for (Planet planet : planets)
				particle.addAcc(getAccVec(particle, planet));

			// update all particles
			updateBodyProps(particle);
		}
	}

	/**
	 * returns the acceleration vector for a body in reference to a planet
	 * 
	 * @param body
	 * @param planet
	 * @return the acceleration vector
	 */
	private Vec2D getAccVec(Body body, Planet planet) {

		// direction of the acceleration vector
		Vec2D r = (planet.getPos()).sub(body.getPos());

		// r * G * m / |r|^3
		return r.mult(Utils.GRAV_CONST * planet.getMass() / Math.pow(r.norm(), 3));
	}

	/**
	 * Updates the position and velocity of a body with its calculated
	 * acceleration vector. The acceleration vector gets resetted for the next
	 * calculation
	 * 
	 * @param body
	 */
	private void updateBodyProps(Body body) {
		Vec2D v = body.getVel();
		Vec2D a = body.getAcc();

		// v + a*t
		body.setVel(v.add(a.mult(time)));

		// r + v*t + 1/2*a*t^2
		body.setPos(body.getPos().add(v.mult(time)).add(a.mult(time * time * 0.5)));

		// reset acceleration
		body.resetAcc();
	}

	/** Checks all bodies for collisions */
	private void checkForCollisions() {

		// planets (collide with each other)
		Planet p1, p2;
		for (int i = 0; i < planets.length - 1; i++) {
			for (int j = i + 1; j < planets.length; j++) {
				p1 = planets[i];
				p2 = planets[j];
				if (p1.getPos().sub(p2.getPos()).norm() < p1.getRadius() + p2.getRadius())
					collidePlanets(p1, p2);
			}
		}

		// particles (don't collide with each other)
		for (Particle par : particles)
			for (Planet pla : planets)
				if (par.getPos().sub(pla.getPos()).norm() < pla.getRadius())
					collideParticlePlanet(pla, par);

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
	private void collidePlanets(Planet p1, Planet p2) {

		// get the bigger planet
		Planet bigP = Utils.getBiggest(p1, p2);
		Planet smallP = Utils.getSmallest(p1, p2);

		// velocity, mass and radius of the new planet
		bigP.setVel(getCollisionVel(p1, p2));
		bigP.setMass(bigP.getMass() + smallP.getMass(), bigP.getDensity());

		// check selected planet
		Planet selPl = Main.win.getSelectedPlanet();
		if (selPl != null && selPl.equals(smallP))
			Main.win.selectPlanet(bigP);

		// copy all planets in the new planets array except the smaller one
		Planet[] newPlanets = new Planet[planets.length - 1];

		for (int i = 0, j = 0; i < newPlanets.length; i++) {
			if (smallP.equals(planets[j]))
				j++;
			newPlanets[i] = planets[j++];
		}

		// set the new planets array and update the window
		planets = newPlanets;

		// delete the small planet
		smallP.delete();
	}

	/**
	 * deletes the collided particle from the array
	 * 
	 * @param pla
	 * @param par
	 */
	private void collideParticlePlanet(Planet pla, Particle par) {
		Particle[] newParticles = new Particle[particles.length - 1];

		for (int i = 0, j = 0; i < newParticles.length; i++) {
			if (par.equals(particles[j]))
				j++;
			newParticles[i] = particles[j++];
		}

		particles = newParticles;

		par.delete();
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
		double m1 = p1.getMass();
		double m2 = p2.getMass();
		Vec2D v1 = p1.getVel();
		Vec2D v2 = p2.getVel();
		double x = (m1 * v1.getX() + m2 * v2.getX()) / (m1 + m2);
		double y = (m1 * v1.getY() + m2 * v2.getY()) / (m1 + m2);
		return new Vec2D(x, y);
	}

	/**
	 * Adds a new planet to the simulation.
	 * 
	 * @param planet
	 *            the new planet
	 */
	public void addNewPlanet(Planet planet) {

		// copy all planets in the new planets array
		Planet[] newPlanets = new Planet[planets.length + 1];
		for (int i = 0; i < planets.length; i++)
			newPlanets[i] = planets[i];

		// the new planet in the last position
		newPlanets[newPlanets.length - 1] = planet;

		// update the planets array and window
		planets = newPlanets;
	}

	/** stops the simulation */
	public void stop() {
		timeline.stop();
	}

	public void setPause(boolean b) {
		if (b)
			timeline.pause();
		else
			timeline.play();
	}

	public boolean isPaused() {
		return timeline.getStatus() == Animation.Status.PAUSED;
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

	public Planet[] getPlanets() {
		return planets;
	}

	public Particle[] getParticles() {
		return particles;
	}

	public double getScale() {
		return scale;
	}

	public int getNumberOfObjects() {
		return planets.length + particles.length;
	}
}
