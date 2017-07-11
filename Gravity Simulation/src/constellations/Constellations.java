package constellations;

import java.util.ArrayList;
import java.util.Arrays;
import bodies.Constellation;
import bodies.Particle;
import bodies.Planet;
import javafx.scene.paint.Color;
import utils.Orbit;
import utils.Utils;
import utils.Vec2d;

/**
 * A class that creates Constellations for the simulation.
 * 
 * @author Jan Muskalla
 * 
 */
public class Constellations {

	/**
	 * an empty system
	 */
	public static Constellation empty() {
		return new Constellation("Empty", new ArrayList<Planet>(), 5e-7, 1);
	}

	/**
	 * The Solar System with the sun, 9 planets and the main asteroid belt
	 */
	public static Constellation solarSystem() {
		String name = "Solar System";
		double scale = 2e-9;
		double time = 100;
		// int sps = default;

		Planet sun = PlanetData.getSun();
		Planet mercury = PlanetData.getMercury();
		Planet venus = PlanetData.getVenus();
		Planet earth = PlanetData.getEarth();
		Planet mars = PlanetData.getMars();
		Planet ceres = PlanetData.getCeres();
		Planet jupiter = PlanetData.getJupiter();
		Planet saturn = PlanetData.getSaturn();
		Planet uranus = PlanetData.getUranus();
		Planet neptun = PlanetData.getNeptune();

		mercury.setOrbit(sun, PlanetData.mercuryOrbit);
		venus.setOrbit(sun, PlanetData.venusOrbit);
		earth.setOrbit(sun, PlanetData.earthOrbit);
		mars.setOrbit(sun, PlanetData.marsOrbit);
		ceres.setOrbit(sun, PlanetData.ceresOrbit);
		jupiter.setOrbit(sun, PlanetData.jupiterOrbit);
		saturn.setOrbit(sun, PlanetData.saturnOrbit);
		uranus.setOrbit(sun, PlanetData.uranusOrbit);
		neptun.setOrbit(sun, PlanetData.neptuneOrbit);

		// all planets
		ArrayList<Planet> planets = new ArrayList<Planet>(
				Arrays.asList(sun, mercury, venus, earth, mars, ceres, jupiter, saturn, uranus, neptun));

		// asteroid belt between mars and Jupiter
		ArrayList<Particle> particles = ParticleArrays.getRing(sun, 100, PlanetData.AU * 2.3, PlanetData.AU * 3.3);

		return new Constellation(name, planets, particles, scale, time);
	}

	/**
	 * The outer Solar System
	 */
	public static Constellation outerSolarSystem() {
		String name = "Solar System";
		double scale = 6e-11;
		double time = 8000;
		// int sps = default;

		Planet sun = PlanetData.getSun();
		Planet jupiter = PlanetData.getJupiter();
		Planet saturn = PlanetData.getSaturn();
		Planet uranus = PlanetData.getUranus();
		Planet neptun = PlanetData.getNeptune();
		Planet pluto = PlanetData.getPluto();

		jupiter.setOrbit(sun, PlanetData.jupiterOrbit);
		saturn.setOrbit(sun, PlanetData.saturnOrbit);
		uranus.setOrbit(sun, PlanetData.uranusOrbit);
		neptun.setOrbit(sun, PlanetData.neptuneOrbit);
		pluto.setOrbit(sun, PlanetData.plutoOrbit);

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(sun, jupiter, saturn, uranus, neptun, pluto));

		return new Constellation(name, planets, scale, time);
	}

	/**
	 * The Earth Moon System
	 */
	public static Constellation earthSystem() {
		Planet earth = PlanetData.getEarth();
		Planet moon = PlanetData.getMoon();

		moon.setOrbit(earth, PlanetData.moonOrbit);
		earth.setVel(Utils.momComp(earth, moon));

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(earth, moon));

		return new Constellation("Earth System", planets, 0.8e-6, 20);
	}

	/**
	 * The Mars system with the two moons Phobos and Deimos
	 */
	public static Constellation marsSystem() {
		Planet mars = PlanetData.getMars();
		Planet phobos = PlanetData.getPhobos();
		Planet deimos = PlanetData.getDeimos();

		phobos.setPos(9376e3, 0);
		phobos.setOrbitalVel(mars);

		deimos.setPos(-23463.2e3, 0);
		deimos.setOrbitalVel(mars);

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(mars, phobos, deimos));

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

		Planet jupiter = PlanetData.getJupiter();
		Planet io = PlanetData.getIo();
		Planet europa = PlanetData.getEuropa();
		Planet ganymede = PlanetData.getGanymede();
		Planet callisto = PlanetData.getCallisto();

		io.setPos(0, ioDis);
		io.setOrbitalVel(jupiter);

		europa.setPos(0, -europaDis);
		europa.setOrbitalVel(jupiter);

		ganymede.setPos(0, ganymadeDis);
		ganymede.setOrbitalVel(jupiter);

		callisto.setPos(0, -callistoDis);
		callisto.setOrbitalVel(jupiter);

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(jupiter, io, europa, ganymede, callisto));

		ArrayList<Particle> particles = ParticleArrays.getRing(jupiter, 100, 92e6, 122e6);

		return new Constellation("Jupiter System", planets, particles, 1.7e-7, 1);
	}

	/**
	 * Saturn system
	 */
	public static Constellation saturnSystem() {
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

		Planet saturn = PlanetData.getSaturn();
		Planet enceladus = PlanetData.getEnceladus();
		Planet tethys = PlanetData.getTethys();
		Planet dione = PlanetData.getDione();
		Planet rhea = PlanetData.getRhea();
		Planet titan = PlanetData.getTitan();
		Planet iapetus = PlanetData.getIapetus();

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

		ArrayList<Particle> particles = ParticleArrays.getRing(saturn, ringParticle, 135e6, 200e6);

		return new Constellation(name, planets, particles, scale, time, sps);
	}

	/**
	 * A fly-by of Jupiter by two asteroids
	 */
	public static Constellation jupiterFlyby() {
		Planet ast1 = new Planet(-133708e3 * 17, -133708e3 * 4, 10000, 0, 100, 1, Color.GRAY, "Asteroid");
		Planet ast2 = new Planet(-133708e3 * 17, -133708e3 * 8, 10000, 0, 100, 1, Color.GRAY, "Asteroid");

		Planet jupiter = PlanetData.getJupiter();

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(ast1, ast2, jupiter));

		return new Constellation("Jupiter flyby", planets, 3e-7, 3);
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

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8));

		return new Constellation("Symmetrical 8", planets, 0.05, 10);
	}

	/**
	 * Earth on collision curse with two mars
	 */
	public static Constellation earthMarsCollision() {
		double distance = PlanetData.getMars().getRadius() * 100;
		double vel = 1000;

		Planet mars = PlanetData.getMars();
		Planet mars2 = PlanetData.getMars();
		Planet earth = PlanetData.getEarth();

		mars.setPos(0, -distance);
		mars.setVel(0, vel);

		mars2.setPos(distance, -distance);
		mars2.setVel(0, vel / 2);

		earth.setPos(-distance, 0);
		earth.setVel(vel, 0);

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(mars, mars2, earth));

		return new Constellation("Earth-Mars Collisions", planets, 8e-7, 5);
	}

	/**
	 * A number of random moons in orbit around the earth
	 */
	public static Constellation randomMoons() {
		int number = 60;
		int sps = 2000;

		Planet earth = PlanetData.getEarth();

		ArrayList<Planet> planets = new ArrayList<Planet>();
		planets.add(earth);

		for (int i = 1; i < number; i++) {
			Planet randomMoon = PlanetData.getRandomMoon();

			randomMoon.setPos(Utils.getRandomOrbitPosition(earth, PlanetData.moonSma * 0.1, PlanetData.moonSma));
			randomMoon.setOrbitalVel(earth);
			randomMoon.setColor(Utils.getRandomColor());
//			randomMoon.setName(Integer.toString(i));

			planets.add(randomMoon);
		}

		return new Constellation("Random moons", planets, 1e-6, 4, sps);
	}

	/**
	 * Random planets
	 */
	public static Constellation randomPlanets() {
		int number = 60;
		ArrayList<Planet> planets = new ArrayList<Planet>();

		for (int i = 0; i < number; i++) {
			Planet p = PlanetData.getRandomPlanet();

			double x = Utils.plusMinus() * Math.random() * 5e7 * 12;
			double y = Utils.plusMinus() * Math.random() * 5e7 * 7;

			p.setPos(x, y);
			p.setVel(Utils.plusMinus() * Math.random() * 5000, Utils.plusMinus() * Math.random() * 5000);
			p.setColor(Utils.getRandomColor());
//			p.setName(Integer.toString(i));

			planets.add(p);
		}

		return new Constellation("Random planets", planets, 7e-7, 1, 2000);
	}

	/**
	 * Saturn with rings encounters Uranus
	 */
	public static Constellation saturnUranusEncounter() {
		int sps = 2000;
		Planet saturn = PlanetData.getSaturn();
		Planet uranus = PlanetData.getUranus();

		double rMin = 135e6;
		double rMax = 200e6;
		ArrayList<Particle> particles = ParticleArrays.getRing(saturn, 1500, rMin, rMax);

		uranus.setPos(-rMax * 4, -rMin * 3);
		uranus.setVel(15000, 0);

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(saturn, uranus));

		return new Constellation("Saturn Uranus encounter", planets, particles, 0.7e-6, 4, sps);
	}

	/**
	 * Earth with a line of particles
	 */
	public static Constellation getParLine() {
		Planet earth = PlanetData.getEarth();

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(earth));

		ArrayList<Particle> particles = ParticleArrays.getVerticalLine(new Vec2d(-PlanetData.moonSma / 2.0, 0),
				new Vec2d(1000, 0), PlanetData.moonSma * 2, 1000);

		return new Constellation("Particle line", planets, particles, 0.7e-6, 20);
	}

	/**
	 * A binary Star System with two jupiter sized planets
	 */
	public static Constellation binaryStar() {
		Planet sun1 = PlanetData.getSun();
		sun1.setName("A");

		Planet sun2 = PlanetData.getSun();
		sun2.setName("B");

		sun1.setPos(-sun1.getRadius() * 3, 0);
		sun2.setPos(sun2.getRadius() * 3, 0);

		sun1.setVel(0, -125000);
		sun2.setVel(0, 125000);

		Planet p1 = PlanetData.getMercury();
		p1.setName("Planet 1");

		Planet p2 = PlanetData.getMars();
		p2.setName("Planet 2");

		double merkurDis = 57.909e9;

		p1.setPos(-merkurDis / 3, 0);
		p1.setOrbitalVel(new Planet(0, 0, 0, 0, sun1.getMass() + sun2.getMass(), 20000));

		p2.setPos(merkurDis / 2, 0);
		p2.setOrbitalVel(new Planet(0, 0, 0, 0, sun1.getMass() + sun2.getMass(), 20000));

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(sun1, sun2, p1, p2));

		return new Constellation("Binary star", planets, 1e-8, 10);
	}

	/**
	 * Two Saturn with rings in orbit around each other
	 */
	public static Constellation binaryWithRings() {
		int sps = 2000;

		Planet p1 = PlanetData.getSaturn();
		Planet p2 = PlanetData.getSaturn();

		double rad = PlanetData.moonSma * 2;
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

		return new Constellation("Binary with rings", planets, particlesAll, 5e-7, 10, sps);
	}

	/**
	 * Earth flying through 2500 particles
	 */
	public static Constellation flyThroughParticleField() {
		int sps = 1000;

		Planet earth = PlanetData.getEarth();
		earth.setPos(-PlanetData.moonSma * 3, 0);
		earth.setVel(1000, 0);

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(earth));

		ArrayList<Particle> particles = ParticleArrays.getField(PlanetData.moonSma * 2, PlanetData.moonSma * 2,
				new Vec2d(-PlanetData.moonSma, 0));

		return new Constellation("Earth flying through particles", planets, particles, 5e-7, 50, sps);
	}

	/**
	 * Earth in the center of 2500 particles
	 */
	public static Constellation particleField() {
		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(PlanetData.getEarth()));
		ArrayList<Particle> particles = ParticleArrays.getField(PlanetData.moonSma, PlanetData.moonSma, new Vec2d());
		return new Constellation("Earth in particle field", planets, particles, 1.5e-6, 5, 1000);
	}

	public static Constellation test() {

		Planet earth = PlanetData.getEarth();
		Planet iss = new Planet(100e3, 500, Color.WHITE, "ISS");
		Planet gso = new Planet(100, 5, Color.WHITE, "GSO");

		iss.setOrbit(earth, new Orbit(earth.getRadius() + 400e3));
		gso.setOrbit(earth, new Orbit(42157e3));

		ArrayList<Planet> planets = new ArrayList<Planet>(Arrays.asList(earth, iss, gso));

		return new Constellation("Test System", planets, 20e-6, 0.05);
	}

}
























