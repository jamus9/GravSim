package systems;

import java.util.ArrayList;
import java.util.Arrays;

import bodies.System;
import bodies.Particle;
import bodies.Planet;
import javafx.scene.paint.Color;
import utils.PolarVec2D;
import utils.Utils;
import utils.Vec2D;

/**
 * A class that creates systems for the simulation.
 * 
 * @author Jan Muskalla
 * 
 */
public class Systems {

	/**
	 * an empty system
	 */
	public static final System empty = new System("Empty", new ArrayList<Planet>(), 5e-7, 0.5);

	/**
	 * The Solar System with the sun, 9 planets and the main asteroid belt
	 */
	public static System solarSystem() {
		String name = "Solar System";
		double scale = 7e-10;
		double time = 100;
		// int sps = default;

		Planet sun = Planets.getSun();
		Planet mercury = Planets.getMercury();
		Planet venus = Planets.getVenus();
		Planet earth = Planets.getEarth();
		Planet mars = Planets.getMars();
		Planet jupiter = Planets.getJupiter();
		Planet saturn = Planets.getSaturn();
		Planet uranus = Planets.getUranus();
		Planet neptun = Planets.getNeptune();

		// periapsis 46001.2e6
		// Argument of perihelion 29.124
		mercury.setPos(new PolarVec2D(46001.2e6, 29.124));
		mercury.setOrbitalVel(sun, Planets.mercurySma);

		// periapsis 107477e6
		// Argument of perihelion 54.884
		venus.setPos(new PolarVec2D(107477e6, 54.884));
		venus.setOrbitalVel(sun, Planets.venusSma);

		// periapsis 147095e6
		// Argument of perihelion 114.20783
		earth.setPos(new PolarVec2D(147095e6, 114.20783));
		earth.setOrbitalVel(sun, Planets.earthSma);

		// periapsis 1.3814 AU
		// Argument of perihelion 286.502
		mars.setPos(new PolarVec2D(1.3814 * Planets.AU, 286.502));
		mars.setOrbitalVel(sun, Planets.marsSma);

		// periapsis 740.55e9
		// Argument of perihelion 273.867
		jupiter.setPos(new PolarVec2D(740.55e9, 273.867));
		jupiter.setOrbitalVel(sun, Planets.jupiterSma);

		// periapsis 1.35e12
		// Argument of perihelion 339.392
		saturn.setPos(new PolarVec2D(1.35e12, 339.392));
		saturn.setOrbitalVel(sun, Planets.saturnSma);

		// periapsis
		// Argument of perihelion
		uranus.setPos(0, Planets.uranusSma);
		uranus.setOrbitalVel(sun);

		// periapsis
		// Argument of perihelion
		neptun.setPos(0, -Planets.neptuneSma);
		neptun.setOrbitalVel(sun);

		ArrayList<Planet> planets = new ArrayList<Planet>(
				Arrays.asList(sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptun));

		// asteroid belt between mars and Jupiter
		double rMin = Planets.AU * 2.3;
		double rMax = Planets.AU * 3.3;
		ArrayList<Particle> particles = ParticleArrays.getRing(sun, 100, rMin, rMax);

		return new System(name, planets, particles, scale, time);
	}

	/**
	 * The Earth Moon System
	 */
	public static System earthSystem() {
		Planet earth = Planets.getEarth();
		Planet moon = Planets.getMoon();

		// periapsis 362.6e6
		moon.setPos(-362.6e6, 0);
		moon.setOrbitalVel(earth, Planets.moonSma);

		// momentum compensation
		earth.setVel(0, Utils.momComp(Planets.earthMass, Planets.moonMass, -moon.getVel().norm()));

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(earth, moon));

		return new System("Earth System", planets, 0.8e-6, 20);
	}

	/**
	 * The Mars system with the two moons Phobos and Deimos
	 */
	public static System marsSystem() {
		Planet mars = Planets.getMars();
		Planet phobos = Planets.getPhobos();
		Planet deimos = Planets.getDeimos();

		double phobosDistance = 9376e3;
		phobos.setPos(phobosDistance, 0);
		phobos.setOrbitalVel(mars);

		double deimosDistance = 23463.2e3;
		deimos.setPos(-deimosDistance, 0);
		deimos.setOrbitalVel(mars);

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(mars, phobos, deimos));

		return new System("Mars System", planets, 1.2e-5, 0.5);
	}

	/**
	 * The Jupiter system with the four Galilean moons
	 */
	public static System jupiterSystem() {
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

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(jupiter, io, europa, ganymede, callisto));

		double rMin = 92e6;
		double rMax = 122e6;
		ArrayList<Particle> particles = ParticleArrays.getRing(jupiter, 100, rMin, rMax);

		return new System("Jupiter System", planets, particles, 1.7e-7, 1);
	}

	/**
	 * Saturn system
	 */
	public static System saturnSystem() {
		String name = "Saturn System";
		double scale = 6e-7;
		double time = 3;
		int sps = 2000;
		int ringParticle = 500;

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

		ArrayList<Planet> planets = new ArrayList<Planet>(
				Arrays.asList(saturn, enceladus, tethys, dione, rhea, titan, iapetus));

		double rMin = 135e6;
		double rMax = 200e6;
		ArrayList<Particle> particles = ParticleArrays.getRing(saturn, ringParticle, rMin, rMax);

		return new System(name, planets, particles, scale, time, sps);
	}

	/**
	 * A fly-by of Jupiter by two asteroids
	 */
	public static System jupiterFlyby() {
		Planet ast1 = new Planet(-133708e3 * 17, -133708e3 * 4, 10000, 0, 100, 1, Color.GRAY, "Asteroid");
		Planet ast2 = new Planet(-133708e3 * 17, -133708e3 * 8, 10000, 0, 100, 1, Color.GRAY, "Asteroid");

		Planet jupiter = Planets.getJupiter();

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(ast1, ast2, jupiter));

		return new System("Jupiter flyby", planets, 3e-7, 3);
	}

	/**
	 * 8 planets in a symmetrical constellation
	 */
	public static System sym8() {
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

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8));

		return new System("Symmetrical 8", planets, 0.05, 10);
	}

	/**
	 * Earth on collision curse with two mars
	 */
	public static System earthMarsCollision() {
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

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(mars, mars2, earth));

		return new System("Earth-Mars Collisions", planets, 8e-7, 5);
	}

	/**
	 * A number of random moons in orbit around the earth
	 */
	public static System randomMoons() {
		int number = 60;
		int sps = 2000;

		Planet earth = Planets.getEarth();

		ArrayList<Planet> planets = new ArrayList<Planet>();
		planets.add(earth);

		for (int i = 1; i < number; i++) {
			Planet randomMoon = Planets.getRandomMoon();

			randomMoon.setPos(Utils.getRandomOrbitPosition(earth, Planets.moonSma * 0.1, Planets.moonSma));
			randomMoon.setVel(Utils.getOrbitalVelocityCircular(earth, randomMoon));
			randomMoon.setColor(Utils.getRandomColor());
			randomMoon.setName(Integer.toString(i));

			planets.add(randomMoon);
		}

		return new System("Random moons", planets, 8e-7, 4, sps);
	}

	/**
	 * Random planets
	 */
	public static System randomPlanets() {
		int number = 60;
		ArrayList<Planet> planets = new ArrayList<Planet>();

		for (int i = 0; i < number; i++) {
			Planet p = Planets.getRandomPlanet();

			double x = Utils.plusMinus() * Math.random() * 5e7 * 12;
			double y = Utils.plusMinus() * Math.random() * 5e7 * 7;

			p.setPos(x, y);
			p.setVel(Utils.plusMinus() * Math.random() * 5000, Utils.plusMinus() * Math.random() * 5000);
			p.setColor(Utils.getRandomColor());
			p.setName("P" + Integer.toString(i));

			planets.add(p);
		}

		return new System("Random planets", planets, 7e-7, 1, 2000);
	}

	/**
	 * Saturn with rings encounters Uranus
	 */
	public static System saturnUranusEncounter() {
		int sps = 2000;
		Planet saturn = Planets.getSaturn();
		Planet uranus = Planets.getUranus();

		double rMin = 135e6;
		double rMax = 200e6;
		ArrayList<Particle> particles = ParticleArrays.getRing(saturn, 1500, rMin, rMax);

		uranus.setPos(-rMax * 4, -rMin * 3);
		uranus.setVel(15000, 0);

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(saturn, uranus));

		return new System("Saturn Uranus encounter", planets, particles, 0.7e-6, 4, sps);
	}

	/**
	 * Earth with a line of particles
	 */
	public static System getParLine() {
		Planet earth = Planets.getEarth();

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(earth));

		ArrayList<Particle> particles = ParticleArrays.getVerticalLine(new Vec2D(-Planets.moonSma / 2.0, 0),
				new Vec2D(1000, 0), Planets.moonSma * 2, 1000);

		return new System("Particle line", planets, particles, 0.7e-6, 20);
	}

	/**
	 * A binary Star System with two jupiter sized planets
	 */
	public static System binaryStar() {
		Planet sun1 = Planets.getSun();
		sun1.setName("A");

		Planet sun2 = Planets.getSun();
		sun2.setName("B");

		sun1.setPos(-sun1.getRadius() * 3, 0);
		sun2.setPos(sun2.getRadius() * 3, 0);

		sun1.setVel(0, -125000);
		sun2.setVel(0, 125000);

		Planet p1 = Planets.getMercury();
		p1.setName("Planet 1");

		Planet p2 = Planets.getMars();
		p2.setName("Planet 2");

		double merkurDis = 57.909e9;

		p1.setPos(-merkurDis / 3, 0);
		p1.setOrbitalVel(new Planet(0, 0, 0, 0, sun1.getMass() + sun2.getMass(), 20000));

		p2.setPos(merkurDis / 2, 0);
		p2.setOrbitalVel(new Planet(0, 0, 0, 0, sun1.getMass() + sun2.getMass(), 20000));

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(sun1, sun2, p1, p2));

		return new System("Binary star", planets, 1e-8, 10);
	}

	/**
	 * Two Saturn with rings in orbit around each other
	 */
	public static System binaryWithRings() {
		int sps = 2000;

		Planet p1 = Planets.getSaturn();
		Planet p2 = Planets.getSaturn();

		double rad = Planets.moonSma * 2;
		p1.setPos(-rad, -rad / 2);
		p2.setPos(rad, rad / 2);

		double v = 5000;
		p1.setVel(v, 0);
		p2.setVel(-v, 0);

		ArrayList<Particle> particlesAll = new ArrayList<Particle>();
		double rMin = 135e6;
		double rMax = 200e6;
		particlesAll.addAll(ParticleArrays.getRing(p1, 500, rMin, rMax));
		particlesAll.addAll(ParticleArrays.getRing(p2, 500, rMin, rMax));

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(p1, p2));

		return new System("Binary with rings", planets, particlesAll, 5e-7, 10, sps);
	}

	/**
	 * Earth flying through 2500 particles
	 */
	public static System flyThroughParticleField() {
		int sps = 1000;

		Planet earth = Planets.getEarth();
		earth.setPos(-Planets.moonSma * 3, 0);
		earth.setVel(1000, 0);

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(earth));

		ArrayList<Particle> particles = ParticleArrays.getField(Planets.moonSma * 2, Planets.moonSma * 2,
				new Vec2D(-Planets.moonSma, 0));

		return new System("Earth flying through particles", planets, particles, 5e-7, 50, sps);
	}

	/**
	 * Earth in the center of 2500 particles
	 */
	public static System particleField() {
		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(Planets.getEarth()));
		ArrayList<Particle> particles = ParticleArrays.getField(Planets.moonSma, Planets.moonSma, new Vec2D());
		return new System("Earth in particle field", planets, particles, 1.5e-6, 5, 1000);
	}

}
