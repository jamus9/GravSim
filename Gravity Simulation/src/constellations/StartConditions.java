package constellations;

import javafx.scene.paint.Color;
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

	private static final double sunMass = 1.9884e30, sunRad = 696342e3,

			merkurMass = 3.301e23, merkurRad = 4879.4e3 / 2.0,

			venusMass = 4.869e24, venusRad = 6052e3,

			earthMass = 5.974e24, earthRad = 12756320 / 2.0,

			moonMass = 7.349e22, moonRad = 3476e3 / 2.0,

			marsMass = 6.419e23, marsRad = 6792.4e3 / 2.0,

			jupiterMass = 1.899e27, jupiterRad = 133708e3 / 2.0,

			saturnMass = 5.68319e26, saturnRad = 58232e3,

			uranusMass = 8.683e25, uranusRad = 51118e3 / 2.0,

			neptunMass = 1.0243e26, neptunRad = 49528e3 / 2.0,

			blackHoleMass = sunMass * 4e6, blackHoleRad = 22.5e9 / 2.0;

	public static Planet sun = new Planet(sunMass, sunRad, Color.YELLOW, "Sun"),

			merkur = new Planet(merkurMass, merkurRad, Color.GREY, "Merkur"),

			venus = new Planet(venusMass, venusRad, Color.BEIGE, "Venus"),

			earth = new Planet(earthMass, earthRad, Color.BLUE, "Earth"),
			moon = new Planet(moonMass, moonRad, Color.GRAY, "Moon"),

			mars = new Planet(marsMass, marsRad, Color.RED, "Mars"),
			phobos = new Planet(1.072e16, 22.5e3, Color.GRAY, "Phobos"),
			deimos = new Planet(1.8e15, 13e3, Color.GRAY, "Deimos"),

			jupiter = new Planet(jupiterMass, jupiterRad, Color.PERU, "Jupiter"),
			io = new Planet(8.94e22, 1821.6e3, Color.YELLOW, "Io"),
			europa = new Planet(4.799e22, 1560.8e3, Color.WHITE, "Europa"),
			ganymede = new Planet(1.4819e23, 2634.1e3, Color.GREY, "Ganymede"),
			callisto = new Planet(1.075e23, 2410e3, Color.DARKSLATEGRAY, "Callisto"),

			saturn = new Planet(saturnMass, saturnRad, Color.BEIGE, "Saturn"),

			uranus = new Planet(uranusMass, uranusRad, Color.LIGHTBLUE, "Uranus"),

			neptun = new Planet(neptunMass, neptunRad, Color.BLUE, "Neptun"),

			blackHole = new Planet(blackHoleMass, blackHoleRad, Color.BLACK, "Black Hole");

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

		return new Constellation("Solar System", planets, 2e-9, 50);
	}

	/**
	 * The Earth-Moon System
	 */
	public static Constellation getEarthSystem() {
		double moonDistance = 384.4e6;

		earth.setPos(0, 0);
		earth.setVel(0, Utils.momComp(earthMass, moonMass, -Utils.orbSpeed(earth, moonDistance)));

		moon.setPos(-moonDistance, 0);
		moon.setVel(0, -Utils.orbSpeed(earth, moonDistance));

		Planet[] planets = { earth.clone(), moon.clone() };
		return new Constellation("Earth System", planets, 0.8e-6, 10);
	}

	/**
	 * The Mars system with the two moons Phobos and Deimos
	 */
	public static Constellation getMarsSystem() {
		double phobosDistance = 9378e3;
		double deimosDistance = 23459e3;

		mars.setPos(0, 0);
		mars.setVel(0, 0);

		phobos.setPos(phobosDistance, 0);
		phobos.setVel(0, Utils.orbSpeed(mars, phobosDistance));

		deimos.setPos(-deimosDistance, 0);
		deimos.setVel(0, -Utils.orbSpeed(mars, deimosDistance));

		Planet[] planets = { mars.clone(), phobos.clone(), deimos.clone() };

		return new Constellation("Mars System", planets, 1.2e-5, 0.5);
	}

	/**
	 * The Mars system with the two moons Phobos and Deimos
	 */
	public static Constellation getJupiterSystem() {
		double ioDis = 421.7e6;
		double europaDis = 670.9e6;
		double ganymadeDis = 1070.4e6;
		double callistoDis = 1882.7e6;

		jupiter.setPos(0, 0);
		jupiter.setVel(0, 0);

		io.setPos(0, ioDis);
		io.setVel(Utils.orbVel(jupiter, io.getPos()));

		europa.setPos(0, -europaDis);
		europa.setVel(Utils.orbVel(jupiter, europa.getPos()));

		ganymede.setPos(0, ganymadeDis);
		ganymede.setVel(Utils.orbVel(jupiter, ganymede.getPos()));

		callisto.setPos(0, -callistoDis);
		callisto.setVel(Utils.orbVel(jupiter, callisto.getPos()));

		Planet[] planets = { jupiter.clone(), io.clone(), europa.clone(), ganymede.clone(), callisto.clone() };

		return new Constellation("Jupiter System", planets, 2e-7, 3);
	}

	/**
	 * A flyby of Jupiter by two asteroids
	 */
	public static Constellation getJupiterFlyby() {
		Planet ast1 = new Planet(-133708e3 * 17, -133708e3 * 4, 10000, 0, 100, 1, Color.GRAY, "Asteroid");
		Planet ast2 = new Planet(-133708e3 * 17, -133708e3 * 8, 10000, 0, 100, 1, Color.GRAY, "Asteroid");

		jupiter.setPos(0, 0);
		jupiter.setVel(0, 0);

		return new Constellation("Jupiter Flyby", new Planet[] { ast1, ast2, jupiter.clone() }, 3e-7, 3);
	}

	/**
	 * 8 planets in a symmetrical constellation
	 */
	public static Constellation getSym8() {
		int dis = 3000;
		double vel = 0.0135;
		int rad = 100;
		Planet p1 = new Planet(dis, 0, 0, vel, rad);
		Planet p2 = new Planet(-dis, 0, 0, -vel, rad);
		Planet p3 = new Planet(0, dis, -vel, 0, rad);
		Planet p4 = new Planet(0, -dis, vel, 0, rad);

		Planet p5 = new Planet(dis, dis, -vel, vel, rad);
		Planet p6 = new Planet(-dis, -dis, vel, -vel, rad);
		Planet p7 = new Planet(-dis, dis, -vel, -vel, rad);
		Planet p8 = new Planet(dis, -dis, vel, vel, rad);

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
	 * returns a number of random planets
	 * 
	 * @param number
	 * @return a constellation of random planets
	 */
	public static Constellation getRandomPlanets(int number) {
		Planet[] planets = new Planet[number];

		for (int i = 0; i < number; i++) {
			planets[i] = new Planet(Utils.plusMinus() * Math.random() * 1200 * (3.0 / 8.0),
					Utils.plusMinus() * Math.random() * 700 * (3.0 / 8.0), Utils.plusMinus() * Math.random() * 0.001,
					Utils.plusMinus() * Math.random() * 0.001, 2.5);
		}

		return new Constellation("Random Planets", planets, 1, 2);
	}

	/**
	 * a number of random moons in orbit around the earth
	 */
	public static Constellation getRandomMoons(int number) {
		double moonDistance = 384.4e6;

		earth.setPos(0, 0);
		earth.setVel(0, 0);

		Planet[] planets = new Planet[number];
		planets[0] = earth.clone();

		double r, x, y;
		Vec2D pos;
		for (int i = 1; i < number; i++) {
			r = (Math.random() * 2.0 + 0.1) * moonDistance;
			y = Utils.plusMinus() * Math.random() * r;
			x = Utils.plusMinus() * Math.sqrt(r * r - y * y);

			pos = new Vec2D(x, y);

			moon.setPos(pos);
			moon.setVel(Utils.orbVel(earth, pos));

			planets[i] = moon.clone();
		}

		return new Constellation("Random Moons", planets, 5e-7, 5);
	}
}
