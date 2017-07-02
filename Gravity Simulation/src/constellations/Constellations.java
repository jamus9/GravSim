package constellations;

import bodies.Constellation;
import bodies.Particle;
import bodies.Planet;
import javafx.scene.paint.Color;
import utils.Utils;
import utils.Vec2D;

/**
 * A class that creates constellations for the simulation.
 * 
 * @author Jan Muskalla
 * 
 */
public class Constellations {

	/**
	 * an empty constellation, have fun
	 */
	public static final Constellation empty = new Constellation("Empty", new Planet[] {}, 5e-7, 0.5);

	/**
	 * The Solar System with the sun and the 9 planets
	 */
	public static Constellation solarSystem() {
		Planet sun = Planets.getSun();
		Planet merkur = Planets.getMerkur();
		Planet venus = Planets.getVenus();
		Planet earth = Planets.getEarth();
		Planet mars = Planets.getMars();
		Planet jupiter = Planets.getJupiter();
		Planet saturn = Planets.getSaturn();
		Planet uranus = Planets.getUranus();
		Planet neptun = Planets.getNeptun();

		merkur.setPos(Planets.merkurDis, 0);
		merkur.setOrbitalVel(sun);

		venus.setPos(-Planets.venusDis, 0);
		venus.setOrbitalVel(sun);

		earth.setPos(0, Planets.earthDis);
		earth.setOrbitalVel(sun);

		mars.setPos(Planets.marsDis, 0);
		mars.setOrbitalVel(sun);

		jupiter.setPos(-Planets.jupiterDis, 0);
		jupiter.setOrbitalVel(sun);

		saturn.setPos(Planets.saturnDis, 0);
		saturn.setOrbitalVel(sun);

		uranus.setPos(0, Planets.uranusDis);
		uranus.setOrbitalVel(sun);

		neptun.setPos(0, -Planets.neptunDis);
		neptun.setOrbitalVel(sun);

		Planet[] planets = { sun, merkur, venus, earth, mars, jupiter, saturn, uranus, neptun };

		// asteroid belt between mars and jupiter
		double rMin = Planets.AU * 2.3;
		double rMax = Planets.AU * 3.3;
		Particle[] particles = Particles.getRing(sun, 100, rMin, rMax);

		return new Constellation("Solar System", planets, particles, 4e-10, 300);
	}

	/**
	 * The Earth-Moon System
	 */
	public static Constellation earthSystem() {
		Planet earth = Planets.getEarth();
		Planet moon = Planets.getMoon();

		earth.setPos(0, 0);

		moon.setPos(-Planets.moonDis, 0);
		moon.setOrbitalVel(earth);

		earth.setVel(0, Utils.momComp(Planets.earthMass, Planets.moonMass, -moon.getVel().norm()));

		Planet[] planets = { earth, moon };

		return new Constellation("Earth System", planets, 0.8e-6, 20);
	}

	/**
	 * The Mars system with the two moons Phobos and Deimos
	 */
	public static Constellation marsSystem() {
		double phobosDistance = 9378e3;
		double deimosDistance = 23459e3;

		Planet mars = Planets.getMars();
		Planet phobos = new Planet(1.072e16, 22.5e3, Color.GRAY, "Phobos");
		Planet deimos = new Planet(1.8e15, 13e3, Color.GRAY, "Deimos");

		phobos.setPos(phobosDistance, 0);
		phobos.setOrbitalVel(mars);

		deimos.setPos(-deimosDistance, 0);
		deimos.setOrbitalVel(mars);

		Planet[] planets = { mars, phobos, deimos };

		return new Constellation("Mars System", planets, 1.2e-5, 0.5);
	}

	/**
	 * The Jupiter system with the four Galilean moons
	 */
	public static Constellation jupiterSystem() {
		double ioDis = 421.7e6;
		double europaDis = 670.9e6;
		double ganymadeDis = 1070.4e6;
		double callistoDis = 1882.7e6;

		Planet jupiter = Planets.getJupiter();
		Planet io = Planets.getIo();
		Planet europa = Planets.getEuropa();
		Planet ganymede = Planets.getGanymede();
		Planet callisto = Planets.getCallisto();

		io.setPos(0, ioDis);
		io.setOrbitalVel(jupiter);

		europa.setPos(0, -europaDis);
		europa.setOrbitalVel(jupiter);

		ganymede.setPos(0, ganymadeDis);
		ganymede.setOrbitalVel(jupiter);

		callisto.setPos(0, -callistoDis);
		callisto.setOrbitalVel(jupiter);

		Planet[] planets = { jupiter, io, europa, ganymede, callisto };

		double rMin = 92e6;
		double rMax = 122e6;
		Particle[] particles = Particles.getRing(jupiter, 100, rMin, rMax);

		return new Constellation("Jupiter System", planets, particles, 1.7e-7, 1);
	}

	/**
	 * Saturn system
	 */
	public static Constellation saturnSystem() {
		int sps = 2000;

		double encDis = 238100e3;
		double tethysDis = 294700e3;
		double dioneDis = 377400e3;
		double rheaDis = 527100e3;
		double titanDis = 1221900e3;
		double iaDis = 3560800e3;

		Planet saturn = Planets.getSaturn();
		Planet enceladus = Planets.getEnceladus();
		Planet tethys = Planets.getTethys();
		Planet dione = Planets.getDione();
		Planet rhea = Planets.getRhea();
		Planet titan = Planets.getTitan();
		Planet iapetus = Planets.getIapetus();

		enceladus.setPos(encDis, 0);
		enceladus.setOrbitalVel(saturn);

		tethys.setPos(-tethysDis, 0);
		tethys.setOrbitalVel(saturn);

		dione.setPos(0, dioneDis);
		dione.setOrbitalVel(saturn);

		rhea.setPos(0, -rheaDis);
		rhea.setOrbitalVel(saturn);

		titan.setPos(titanDis, 0);
		titan.setOrbitalVel(saturn);

		iapetus.setPos(-iaDis, 0);
		iapetus.setOrbitalVel(saturn);

		Planet[] planets = new Planet[] { saturn, enceladus, tethys, dione, rhea, titan, iapetus };

		double rMin = 135e6;
		double rMax = 200e6;
		Particle[] particles = Particles.getRing(saturn, 500, rMin, rMax);

		return new Constellation("Saturn System", planets, particles, 4e-7, 3, sps);
	}

	/**
	 * A flyby of Jupiter by two asteroids
	 */
	public static Constellation jupiterFlyby() {
		Planet ast1 = new Planet(-133708e3 * 17, -133708e3 * 4, 10000, 0, 100, 1, Color.GRAY, "Asteroid");
		Planet ast2 = new Planet(-133708e3 * 17, -133708e3 * 8, 10000, 0, 100, 1, Color.GRAY, "Asteroid");

		Planet jupiter = Planets.getJupiter();

		return new Constellation("Jupiter Flyby", new Planet[] { ast1, ast2, jupiter }, 3e-7, 3);
	}

	/**
	 * 8 planets in a symmetrical constellation
	 */
	public static Constellation sym8() {
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
	public static Constellation earthMarsCollision() {
		double distance = Planets.getMars().getRadius() * 100;
		double vel = 1000;

		Planet mars = Planets.getMars();
		Planet mars2 = Planets.getMars();
		Planet earth = Planets.getEarth();

		mars.setPos(0, -distance);
		mars.setVel(0, vel);

		mars2.setPos(distance, -distance);
		mars2.setVel(0, vel / 2);

		earth.setPos(-distance, 0);
		earth.setVel(vel, 0);

		return new Constellation("Earth-Mars Collisions", new Planet[] { mars, mars2, earth }, 8e-7, 5);
	}

	/**
	 * A number of random moons in orbit around the earth
	 */
	public static Constellation randomMoons() {
		int number = 60;
		int sps = 2000;

		Planet earth = Planets.getEarth();

		Planet[] planets = new Planet[number];
		planets[0] = earth;

		for (int i = 1; i < number; i++) {
			Planet randomMoon = Planets.getRandomMoon();

			randomMoon.setPos(Utils.getRandomOrbitPosition(earth, Planets.moonDis * 0.1, Planets.moonDis));
			randomMoon.setVel(Utils.getOrbitalVelocity(earth, randomMoon));
			randomMoon.setColor(Utils.getRandomColor());

			planets[i] = randomMoon;
		}

		return new Constellation("Random Moons", planets, 8e-7, 4, sps);
	}

	/**
	 * Random planets
	 */
	public static Constellation randomPlanets() {
		int number = 40;
		Planet[] planets = new Planet[number];

		for (int i = 0; i < number; i++) {
			Planet p = Planets.getRandomPlanet();

			double x = Utils.plusMinus() * Math.random() * 5e7 * 12;
			double y = Utils.plusMinus() * Math.random() * 5e7 * 7;

			p.setPos(x, y);
			p.setVel(Utils.plusMinus() * Math.random() * 5000, Utils.plusMinus() * Math.random() * 5000);
			p.setColor(Utils.getRandomColor());

			planets[i] = p;
		}

		return new Constellation("Random Planets", planets, 7e-7, 1);
	}

	/**
	 * Saturn with rings encounters uranus
	 */
	public static Constellation saturnUranusEncounter() {
		int sps = 2000;
		Planet saturn = Planets.getSaturn();
		Planet uranus = Planets.getUranus();

		double rMin = 135e6;
		double rMax = 200e6;
		Particle[] particles = Particles.getRing(saturn, 1500, rMin, rMax);

		uranus.setPos(-rMax * 4, -rMin * 3);
		uranus.setVel(15000, 0);

		Planet[] planets = new Planet[] { saturn, uranus };

		return new Constellation("Saturn Uranus Encounter", planets, particles, 0.7e-6, 4, sps);
	}

	/**
	 * Earth with a line of particles
	 */
	public static Constellation getParLine() {
		Planet earth = Planets.getEarth();

		Planet[] planets = new Planet[] { earth };

		Particle[] particles = Particles.getVerticalLine(new Vec2D(-Planets.moonDis / 2.0, 0), new Vec2D(1000, 0),
				Planets.moonDis * 2, 1000);

		return new Constellation("Particle Line", planets, particles, 0.7e-6, 20);
	}

	/**
	 * A binary Star System with two jupiter sized planets
	 */
	public static Constellation binaryStar() {
		Planet sun1 = Planets.getSun();
		sun1.setName("A");

		Planet sun2 = Planets.getSun();
		sun2.setName("B");

		sun1.setPos(-sun1.getRadius() * 3, 0);
		sun2.setPos(sun2.getRadius() * 3, 0);

		sun1.setVel(0, -125000);
		sun2.setVel(0, 125000);

		Planet p1 = Planets.getJupiter();
		p1.setName("Planet 1");

		Planet p2 = Planets.getJupiter();
		p2.setName("Planet 2");

		double merkurDis = 57.909e9;

		p1.setPos(-merkurDis / 2, 0);
		p1.setOrbitalVel(new Planet(0, 0, 0, 0, sun1.getMass() + sun2.getMass(), 20000));

		p2.setPos(merkurDis / 4, 0);
		p2.setOrbitalVel(new Planet(0, 0, 0, 0, sun1.getMass() + sun2.getMass(), 20000));

		Planet[] planets = new Planet[] { sun1, sun2, p1, p2 };

		return new Constellation("Binary Star", planets, 1e-8, 10);
	}

	/**
	 * Two Saturn with rings in orbit around each other
	 */
	public static Constellation binaryWithRings() {
		int sps = 2000;
		double rad = Planets.moonDis * 2;
		Planet p1 = Planets.getSaturn();
		Planet p2 = Planets.getSaturn();

		p1.setPos(-rad, 0);
		p2.setPos(rad, 0);

		double v = 2400;

		p1.setVel(0, -v);
		p2.setVel(0, v);

		double rMin = 135e6;
		double rMax = 200e6;
		Particle[] particles1 = Particles.getRing(p1, 500, rMin, rMax);
		Particle[] particles2 = Particles.getRing(p2, 500, rMin, rMax);

		Particle[] particlesAll = (Particle[]) Utils.concat(particles1, particles2);

		Planet[] planets = new Planet[] { p1, p2 };

		return new Constellation("Binary with rings", planets, particlesAll, 5e-7, 10, sps);
	}

	/**
	 * Earth flying through 2500 particles
	 */
	public static Constellation flyThroughParticleField() {
		int sps = 1000;

		Planet earth = Planets.getEarth();
		earth.setPos(-Planets.moonDis * 3, 0);
		earth.setVel(1000, 0);

		Planet[] planets = new Planet[] { earth };

		Particle[] particles = Particles.getField(Planets.moonDis * 2, Planets.moonDis * 2,
				new Vec2D(-Planets.moonDis, 0));

		return new Constellation("Particle Field", planets, particles, 5e-7, 50, sps);
	}

	/**
	 * Earth in the center of 2500 particles
	 */
	public static Constellation particleField() {
		Planet[] planets = new Planet[] { Planets.getEarth() };
		Particle[] particles = Particles.getField(Planets.moonDis, Planets.moonDis, new Vec2D());

		return new Constellation("Particle Field", planets, particles, 1.5e-6, 5, 1000);
	}

}
