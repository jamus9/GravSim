package constellations;

import javafx.scene.paint.Color;
import simulation.Particle;
import simulation.Planet;
import utils.Utils;
import utils.Vec2D;

/**
 * A class that creates start conditions (constellations) for the simulation.
 * 
 * @author Jan Muskalla
 * 
 */
public class StartConditions {

	private static final double sunMass = 1.988e30, sunRad = 696342e3,

			merkurMass = 3.301e23, merkurRad = 4879.4e3 / 2.0,

			venusMass = 4.869e24, venusRad = 6052e3,

			earthMass = 5.974e24, earthRad = 12756320 / 2.0,

			moonMass = 7.349e22, moonRad = 3476e3 / 2.0,

			marsMass = 6.419e23, marsRad = 6792.4e3 / 2.0,

			jupiterMass = 1.899e27, jupiterRad = 133708e3 / 2.0,

			saturnMass = 5.683e26, saturnRad = 58232e3,

			uranusMass = 8.683e25, uranusRad = 51118e3 / 2.0,

			neptunMass = 1.024e26, neptunRad = 49528e3 / 2.0,

			blackHoleMass = sunMass * 4e6, blackHoleRad = 22.5e9 / 2.0;

	public static Planet sun = new Planet(sunMass, sunRad, Color.YELLOW, "Sun"),

			merkur = new Planet(merkurMass, merkurRad, Color.rgb(212, 201, 156), "Merkur"),

			venus = new Planet(venusMass, venusRad, Color.BEIGE, "Venus"),

			earth = new Planet(earthMass, earthRad, Color.rgb(79, 76, 176), "Earth"),
			moon = new Planet(moonMass, moonRad, Color.GRAY, "Moon"),

			mars = new Planet(marsMass, marsRad, Color.RED, "Mars"),
			phobos = new Planet(1.072e16, 22.5e3, Color.GRAY, "Phobos"),
			deimos = new Planet(1.8e15, 13e3, Color.GRAY, "Deimos"),

			jupiter = new Planet(jupiterMass, jupiterRad, Color.rgb(201, 144, 57), "Jupiter"),
			io = new Planet(8.94e22, 1821.6e3, Color.YELLOW, "Io"),
			europa = new Planet(4.799e22, 1560.8e3, Color.WHITE, "Europa"),
			ganymede = new Planet(1.4819e23, 2634.1e3, Color.GREY, "Ganymede"),
			callisto = new Planet(1.075e23, 2410e3, Color.DARKSLATEGRAY, "Callisto"),

			saturn = new Planet(saturnMass, saturnRad, Color.rgb(216, 202, 157), "Saturn"),

			uranus = new Planet(uranusMass, uranusRad, Color.rgb(225, 238, 238), "Uranus"),

			neptun = new Planet(neptunMass, neptunRad, Color.rgb(63, 84, 186), "Neptun"),

			blackHole = new Planet(blackHoleMass, blackHoleRad, Color.BLACK, "Black Hole");

	private static double moonDistance = 384.4e6;

	/**
	 * an empty constellation, have fun
	 */
	public static final Constellation empty = new Constellation("Empty", new Planet[] {}, 5e-7, 0.5);

	/**
	 * The Solar System with the sun and the 9 planets
	 */
	public static Constellation getSolarSystem() {
		double merkurDis = 57.909e9;
		double venusDis = 108.16e9;
		double earthDis = 149.6e9;
		double marsDis = 227.99e9;
		double jupiterDis = 778.23e9;
		double saturnDis = 1433.5e9;
		double uranusDis = 2872.4e9;
		double neptunDis = 4498.4e9;

		sun.setPos(0, 0);
		sun.setVel(0, 0);

		merkur.setPos(merkurDis, 0);
		merkur.setVel(0, Utils.orbSpeed(sun, merkurDis));

		venus.setPos(-venusDis, 0);
		venus.setVel(0, -Utils.orbSpeed(sun, venusDis));

		earth.setPos(0, earthDis);
		earth.setVel(-Utils.orbSpeed(sun, earthDis), 0);

		mars.setPos(marsDis, 0);
		mars.setVel(0, Utils.orbSpeed(sun, marsDis));

		jupiter.setPos(-jupiterDis, 0);
		jupiter.setVel(0, -Utils.orbSpeed(sun, jupiterDis));

		saturn.setPos(saturnDis, 0);
		saturn.setVel(0, Utils.orbSpeed(sun, saturnDis));

		uranus.setPos(0, uranusDis);
		uranus.setVel(-Utils.orbSpeed(sun, uranusDis), 0);

		neptun.setPos(0, -neptunDis);
		neptun.setVel(Utils.orbSpeed(sun, neptunDis), 0);

		Planet[] planets = { sun.clone(), merkur.clone(), venus.clone(), earth.clone(), mars.clone(), jupiter.clone(),
				saturn.clone(), uranus.clone(), neptun.clone() };

		return new Constellation("Solar System", planets, 1.4e-9, 200);
	}

	/**
	 * The Earth-Moon System
	 */
	public static Constellation getEarthSystem() {
		earth.setPos(0, 0);
		earth.setVel(0, Utils.momComp(earthMass, moonMass, -Utils.orbSpeed(earth, moonDistance)));

		moon.setPos(-moonDistance, 0);
		moon.setVel(0, -Utils.orbSpeed(earth, moonDistance));

		Planet[] planets = { earth.clone(), moon.clone() };
		return new Constellation("Earth System", planets, 0.8e-6, 20);
	}

	/**
	 * The Mars system with the two moons Phobos and Deimos
	 */
	public static Constellation getMarsSystem() {
		double phobosDistance = 9378e3;
		double deimosDistance = 23459e3;

		mars.setZero();

		phobos.setPos(phobosDistance, 0);
		phobos.setVel(Utils.getOrbitalVelocity(mars, phobos));

		deimos.setPos(-deimosDistance, 0);
		deimos.setVel(Utils.getOrbitalVelocity(mars, deimos));

		Planet[] planets = { mars.clone(), phobos.clone(), deimos.clone() };

		return new Constellation("Mars System", planets, 1.2e-5, 0.5);
	}

	/**
	 * The Jupiter System with the four Galilean moons
	 */
	public static Constellation getJupiterSystem() {
		double ioDis = 421.7e6;
		double europaDis = 670.9e6;
		double ganymadeDis = 1070.4e6;
		double callistoDis = 1882.7e6;

		jupiter.setZero();

		io.setPos(0, ioDis);
		io.setVel(Utils.getOrbitalVelocity(jupiter, io));

		europa.setPos(0, -europaDis);
		europa.setVel(Utils.getOrbitalVelocity(jupiter, europa));

		ganymede.setPos(0, ganymadeDis);
		ganymede.setVel(Utils.getOrbitalVelocity(jupiter, ganymede));

		callisto.setPos(0, -callistoDis);
		callisto.setVel(Utils.getOrbitalVelocity(jupiter, callisto));

		Planet[] planets = { jupiter.clone(), io.clone(), europa.clone(), ganymede.clone(), callisto.clone() };

		return new Constellation("Jupiter System", planets, 1.5e-7, 3);
	}

	/**
	 * A flyby of Jupiter by two asteroids
	 */
	public static Constellation getJupiterFlyby() {
		Planet ast1 = new Planet(-133708e3 * 17, -133708e3 * 4, 10000, 0, 100, 1, Color.GRAY, "Asteroid");
		Planet ast2 = new Planet(-133708e3 * 17, -133708e3 * 8, 10000, 0, 100, 1, Color.GRAY, "Asteroid");

		jupiter.setZero();

		return new Constellation("Jupiter Flyby", new Planet[] { ast1, ast2, jupiter.clone() }, 3e-7, 3);
	}

	/**
	 * 8 planets in a symmetrical constellation
	 */
	public static Constellation getSym8() {
		int dis = 3000;
		double vel = 0.0135;
		double mass = 5e9;
		int density = 2000;

		Planet p1 = new Planet(dis, 0, 0, vel, mass, density);
		Planet p2 = new Planet(-dis, 0, 0, -vel, mass, density);
		Planet p3 = new Planet(0, dis, -vel, 0, mass, density);
		Planet p4 = new Planet(0, -dis, vel, 0, mass, density);

		Planet p5 = new Planet(dis, dis, -vel, vel, mass, density);
		Planet p6 = new Planet(-dis, -dis, vel, -vel, mass, density);
		Planet p7 = new Planet(-dis, dis, -vel, -vel, mass, density);
		Planet p8 = new Planet(dis, -dis, vel, vel, mass, density);

		Planet[] planets = { p1, p2, p3, p4, p5, p6, p7, p8 };
		return new Constellation("Symmetrical 8", planets, 0.05, 10);
	}

	/**
	 * Earth on collision curse with two mars
	 */
	public static Constellation getEarthMarsCollision() {
		double distance = mars.getRadius() * 100;
		double vel = 1000;

		mars.setPos(0, -distance);
		mars.setVel(0, vel);

		Planet mars2 = mars.clone();

		mars2.setPos(distance, -distance);
		mars2.setVel(0, vel / 2);

		earth.setPos(-distance, 0);
		earth.setVel(vel, 0);

		return new Constellation("Earth-Mars Collisions", new Planet[] { mars.clone(), mars2, earth.clone() }, 8e-7, 5);
	}

	/**
	 * random planets
	 * 
	 * @return a constellation of random planets
	 */
	public static Constellation getRandomPlanets() {
		int number = 40;
		Planet[] planets = new Planet[number];

		for (int i = 0; i < number; i++) {
			Planet p = randomPlanet();

			double x = Utils.plusMinus() * Math.random() * 5e7 * 12;
			double y = Utils.plusMinus() * Math.random() * 5e7 * 7;

			p.setPos(x, y);
			p.setVel(Utils.plusMinus() * Math.random() * 5000, Utils.plusMinus() * Math.random() * 5000);
			p.setColor(Utils.getRandomColor());

			planets[i] = p;
		}

		return new Constellation("Random Planets", planets, 7e-7, 3);
	}

	/**
	 * a number of random moons in orbit around the earth
	 */
	public static Constellation getRandomMoons() {
		int number = 60;

		earth.setZero();

		Planet[] planets = new Planet[number];
		planets[0] = earth.clone();

		for (int i = 1; i < number; i++) {
			Planet randomMoon = randomMoon();

			randomMoon.setPos(Utils.getRandomOrbitPosition(earth, moonDistance * 0.1, moonDistance * 0.5));
			randomMoon.setVel(Utils.getOrbitalVelocity(earth, randomMoon));
			randomMoon.setColor(Utils.getRandomColor());

			planets[i] = randomMoon.clone();
		}

		return new Constellation("Random Moons", planets, 1.9e-6, 1);
	}

	/**
	 * Saturn with rings
	 */
	public static Constellation getSaturnWithRings() {
		saturn.setZero();

		double rMin = 135e6;
		double rMax = 200e6;
		Particle[] particles = getRing(saturn, 1500, rMin, rMax);

		Planet[] planets = new Planet[] { saturn.clone() };

		return new Constellation("Saturn with Rings", planets, particles, 1.2e-6, 2);
	}

	/**
	 * Saturn with rings encounters an uranus
	 */
	public static Constellation getSaturnUranusEncounter() {
		saturn.setZero();

		double rMin = 135e6;
		double rMax = 200e6;
		Particle[] particles = getRing(saturn, 1500, rMin, rMax);

		uranus.setPos(-rMax * 4, -rMin * 3);
		uranus.setVel(15000, 0);

		Planet[] planets = new Planet[] { saturn.clone(), uranus.clone() };

		return new Constellation("Particles Test", planets, particles, 0.7e-6, 4);
	}

	public static Constellation getParLine() {
		earth.setZero();
		Planet[] planets = new Planet[] { earth.clone() };
		Particle[] particles = getVerticalLine(new Vec2D(-moonDistance / 2.0, 0), new Vec2D(1000, 0), moonDistance * 2,
				1000);
		return new Constellation("Particles Test", planets, particles, 0.7e-6, 20);
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
	private static Particle[] getRing(Planet planet, int number, double innerRadius, double outerRadius) {
		Particle[] particles = new Particle[number];
		Particle particle;
		for (int i = 0; i < particles.length; i++) {
			particle = new Particle();

			particle.setPos(Utils.getRandomOrbitPosition(saturn, innerRadius, outerRadius));
			particle.setVel(Utils.getOrbitalVelocity(saturn, particle));

			particles[i] = particle.clone();
		}

		return particles;
	}

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
	private static Particle[] getVerticalLine(Vec2D pos, Vec2D vel, double lenght, int number) {
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
	 * returns a random typical asteroid
	 * 
	 * @return the random Planet
	 */
	@SuppressWarnings("unused")
	private static Planet randomAsteroid() {
		// typical asteroid density in kg/m^3
		double density = 2000;
		double mass = Utils.getRandomInInervall(10e4, 10e20);
		return new Planet(0, 0, 0, 0, mass, density);
	}

	/**
	 * returns a random moon with a mass between 1/3 moon and moon mass
	 * 
	 * @return the random Planet
	 */
	private static Planet randomMoon() {
		double density = moon.getDensity();
		double mass = Utils.getRandomInInervall(moonMass / 10, moonMass);
		return new Planet(0, 0, 0, 0, mass, density);
	}

	/**
	 * returns a random planet with a mass between merkur and 2 earth masses
	 * 
	 * @return the random Planet
	 */
	private static Planet randomPlanet() {
		double density = earth.getDensity();
		double mass = Utils.getRandomInInervall(merkurMass, earthMass * 2);
		return new Planet(0, 0, 0, 0, mass, density);
	}

	/**
	 * returns a random gas giant with a mass between uranus and jupiter
	 * 
	 * @return the random Planet
	 */
	@SuppressWarnings("unused")
	private static Planet randomGasGiant() {
		double density = uranus.getDensity();
		double mass = Utils.getRandomInInervall(uranusMass, jupiterMass);
		return new Planet(0, 0, 0, 0, mass, density);
	}
}
