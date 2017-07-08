package simulation;

import java.util.ArrayList;
import bodies.Body;
import bodies.System;
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
 * system.
 * 
 * @author Jan Muskalla
 * 
 */
public class Simulation {

	/** simulations per second */
	private static double sps;

	/** the current system with start conditions */
	private final System system;

	private final String name;

	/** scale of the simulation */
	private final double scale;

	/** time step per simulation */
	private double time;

	/** all planets of the current simulation are saved here */
	private ArrayList<Planet> planets;

	/** all particles of the current simulation */
	private ArrayList<Particle> particles;

	/** the time line in which all calculations happen */
	public Timeline timeline;

	/** time counter */
	private int spsCounter;
	private double secondsCounter;

	/**
	 * Creates a new simulation with a given system.
	 * 
	 * @param system
	 */
	public Simulation(System system) {
		spsCounter = 0;
		secondsCounter = 0;

		// save a copy for later restarts
		this.system = system.clone();

		name = system.getName();
		time = system.getTime();
		scale = system.getScale();
		sps = system.getSps();

		// copy the planets of the new system in the local array and save the
		// first position for the trails
		planets = system.getPlanetArray();
		for (Planet p : planets)
			p.savePosition();

		// copy the particles of the new system in the local array
		particles = system.getParticleArray();

		timeline = new Timeline();
	}

	/**
	 * Moves the planets SPS times per second and checks for collisions.
	 */
	public void run() {
		KeyFrame kf = new KeyFrame(Duration.seconds(1.0 / sps), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				moveBodies();
				checkForCollisions();
				spsCounter++;
				secondsCounter += time;
			}
		});

		timeline.getKeyFrames().add(kf);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	/**
	 * Calculate the sum of all acceleration vectors for each body at the same
	 * simulation time and then moves all bodies one time step.
	 */
	private void moveBodies() {

		// add all acceleration vectors in one time step
		for (Planet p1 : planets)
			for (Planet p2 : planets)
				if (p1 != p2)
					p1.addAcc(getAccVec(p1, p2));

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
	 * Updates the position and velocity of a body with its calculated acceleration
	 * vector. The acceleration vector gets resetted for the next calculation
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

	/** Checks all bodies for collisions and removes all collided ones */
	private void checkForCollisions() {
		ArrayList<Body> toRemove = new ArrayList<Body>();

		// planets (collide with each other)
		Planet p1, p2, bigP, smallP;
		for (int i = 0; i < planets.size(); i++) {
			p1 = planets.get(i);

			for (int j = i + 1; j < planets.size(); j++) {
				p2 = planets.get(j);

				if (p1.getPos().sub(p2.getPos()).norm() < p1.getRadius() + p2.getRadius()) {
					bigP = Utils.getBiggest(p1, p2);
					smallP = Utils.getSmallest(p1, p2);
					collidePlanets(bigP, smallP);
					toRemove.add(smallP);
				}
			}

			// particles (don't collide with each other)
			for (Particle particle : particles)
				if (particle.getPos().sub(p1.getPos()).norm() < p1.getRadius())
					toRemove.add(particle);
		}

		for (Body body : toRemove)
			removeBody(body);
	}

	/**
	 * Collides two planets and changes the bigger one after the collision.
	 * 
	 * @param bigP
	 *            the bigger planet
	 * @param smallP
	 *            the smaller planet
	 */
	private void collidePlanets(Planet bigP, Planet smallP) {

		// velocity, mass and radius of the new planet
		bigP.setVel(getCollisionVel(bigP, smallP));
		bigP.setMass(bigP.getMass() + smallP.getMass(), bigP.getDensity());

		// check selected planet
		Planet sp = Main.win.getSelectedPlanet();
		if (sp != null && sp.equals(smallP))
			Main.win.selectPlanet(bigP);
	}

	/**
	 * deletes a body from the array and window
	 * 
	 * @param body
	 */
	private void removeBody(Body body) {
		if (body instanceof Planet)
			planets.remove(body);
		if (body instanceof Particle)
			particles.remove(body);
		body.delete();
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
		planet.savePosition();
		planets.add(planet);
	}

	/** stops the simulation */
	public void stop() {
		timeline.stop();
	}

	/** pauses the simulation */
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
		time = system.getTime();
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

	public System getSystem() {
		return system;
	}

	public ArrayList<Planet> getPlanets() {
		return planets;
	}

	public ArrayList<Particle> getParticles() {
		return particles;
	}

	public double getScale() {
		return scale;
	}

	public int getNumberOfObjects() {
		return planets.size() + particles.size();
	}

	public static double getSps() {
		return sps;
	}

	public String getName() {
		return name;
	}
}
