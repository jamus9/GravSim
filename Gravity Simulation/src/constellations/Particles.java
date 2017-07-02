package constellations;

import java.util.ArrayList;

import bodies.Particle;
import bodies.Planet;
import utils.Utils;
import utils.Vec2D;

/**
 * Creates different arrays of particles.
 * 
 * @author Jan Muskalla
 *
 */
public class Particles {

	/**
	 * returns a vertical line of a number of particles with a given position,
	 * velocity and length
	 * 
	 * @param pos
	 * @param vel
	 * @param lenght
	 * @param number
	 * @return
	 */
	public static Particle[] getVerticalLine(Vec2D pos, Vec2D vel, double lenght, int number) {
		Particle[] particles = new Particle[number];

		Particle particle;
		for (int i = 0; i < particles.length; i++) {
			particle = new Particle();

			particle.setPos(pos.getX(), i * lenght / number - lenght / 2.0 + pos.getY());
			particle.setVel(1000, 0);

			particles[i] = particle;
		}

		return particles;
	}

	/**
	 * returns a ring around a planet made of a given number of particles
	 * 
	 * @param planet
	 * @param number
	 * @param innerRadius
	 * @param outerRadius
	 * @return
	 */
	public static Particle[] getRing(Planet planet, int number, double innerRadius, double outerRadius) {
		Particle[] particles = new Particle[number];
		Particle particle;
		for (int i = 0; i < particles.length; i++) {
			particle = new Particle();

			particle.setPos(Utils.getRandomOrbitPosition(planet, innerRadius, outerRadius));
			particle.setVel(Utils.getOrbitalVelocity(planet, particle));

			particles[i] = particle.clone();
		}

		return particles;
	}

	/**
	 * 1600 particles
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Particle[] getField(double x, double y, Vec2D center) {
		int n = 50;
		double cx = center.getX();
		double cy = center.getY();

		ArrayList<Particle> particles = new ArrayList<Particle>();

		Particle particle;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				particle = new Particle();

				particle.setPos(-(x / 2) + (i * x / n) + cx, -(y / 2) + (j * y / n) + cy);

				particles.add(particle);
			}
		}

		return particles.toArray(new Particle[particles.size()]);
	}

}
