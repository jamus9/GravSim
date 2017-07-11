package simulation;

import java.util.ArrayList;
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
import utils.Vec2d;

/**
 * Simulates the movement of the planets and handles the current planets and
 * system.
 * 
 * @author Jan Muskalla
 * 
 */
public class Simulation {

	/** a copy of the Constellation for this simulation */
	private final Constellation constellation;

	/** time step per simulation */
	private double time;

	/** all planets of the current simulation are saved here */
	private ArrayList<Planet> planetList;

	/** all particles of the current simulation */
	private ArrayList<Particle> particleList;

	/** the time line in which all calculations happen */
	public Timeline timeline;

	/** time counter */
	private int spsCounter;
	private double secondsCounter;

	/**
	 * Creates a new simulation with a given system.
	 * 
	 * @param constellation
	 */
	public Simulation(Constellation constellation) {
		spsCounter = 0;
		secondsCounter = 0;

		// save a copy for later restarts
		this.constellation = constellation.clone();
		
		// the variable time step per simulation
		time = constellation.getTime();

		// copy the planets of the new system in the local array and save the
		// first position for the trails
		planetList = constellation.getPlanetList();
		for (Planet p : planetList)
			p.getTrail().savePosition();
//			p.savePosition();

		// copy the particles of the new system in the local array
		particleList = constellation.getParticleList();

		timeline = new Timeline();
	}

	/**
	 * Moves the planets SPS times per second and checks for collisions.
	 */
	public void run() {
		KeyFrame kf = new KeyFrame(Duration.seconds(1.0 / getSps()), new EventHandler<ActionEvent>() {
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
		for (Planet p1 : planetList)
			for (Planet p2 : planetList)
				if (p1 != p2)
					p1.addAcc(getAccVec(p1, p2));

		// update all planets
		for (Planet planet : planetList)
			updateBodyProps(planet);

		// add all acceleration vectors in one time step
		for (Particle particle : particleList) {
			for (Planet planet : planetList)
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
	private Vec2d getAccVec(Body body, Planet planet) {

		// direction of the acceleration vector
		Vec2d r = (planet.getPos()).sub(body.getPos());

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
		Vec2d v = body.getVel();
		Vec2d a = body.getAcc();

		// v + a*t
		body.setVel(v.add(a.mult(time)));

		// r + v*t + 1/2*a*t^2
		body.setPos(body.getPos().add(v.mult(time)).add(a.mult(time * time * 0.5)));

		// reset acceleration
		body.resetAcc();
	}

	/**
	 * Checks all bodies for collisions and removes all collided ones
	 */
	private void checkForCollisions() {
		ArrayList<Body> toRemove = new ArrayList<Body>();

		// planets (collide with each other)
		Planet p1, p2, bigP, smallP;
		for (int i = 0; i < planetList.size(); i++) {
			p1 = planetList.get(i);

			for (int j = i + 1; j < planetList.size(); j++) {
				p2 = planetList.get(j);

				if (p1.getPos().sub(p2.getPos()).norm() < p1.getRadius() + p2.getRadius()) {
					bigP = Utils.getBiggest(p1, p2);
					smallP = Utils.getSmallest(p1, p2);
					collidePlanets(bigP, smallP);
					toRemove.add(smallP);
				}
			}

			// particles (don't collide with each other)
			for (Particle particle : particleList) {
				if (particle.getPos().sub(p1.getPos()).norm() < p1.getRadius()) {
					toRemove.add(particle);
				}
			}
		}

		for (Body body : toRemove) {
			if (body instanceof Planet)
				planetList.remove(body);
			else if (body instanceof Particle)
				particleList.remove(body);
			body.delete();
		}
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
	 * Calculates the new velocity of a planet after a collision with momentum
	 * conservation
	 * 
	 * @param p1
	 * @param p2
	 * @return the new velocity
	 */
	private static Vec2d getCollisionVel(Planet p1, Planet p2) {
		double m1 = p1.getMass();
		double m2 = p2.getMass();
		Vec2d v1 = p1.getVel();
		Vec2d v2 = p2.getVel();
		double x = (m1 * v1.getX() + m2 * v2.getX()) / (m1 + m2);
		double y = (m1 * v1.getY() + m2 * v2.getY()) / (m1 + m2);
		return new Vec2d(x, y);
	}

	/**
	 * Adds a new planet to the simulation.
	 * 
	 * @param planet
	 *            the new planet
	 */
	public void addNewPlanet(Planet planet) {
		planet.getTrail().savePosition();
//		planet.savePosition();
		planetList.add(planet);
	}

	/**
	 * stops the simulation
	 */
	public void stop() {
		timeline.stop();
	}

	/**
	 * pauses the simulation
	 */
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

	public ArrayList<Planet> getPlanetList() {
		return planetList;
	}

	public ArrayList<Particle> getParticleList() {
		return particleList;
	}

	public double getScale() {
		return constellation.getScale();
	}

	public int getNumberOfObjects() {
		return planetList.size() + particleList.size();
	}

	public double getSps() {
		return constellation.getSps();
	}

	public String getName() {
		return constellation.getName();
	}
}
