package constellations;

import javafx.scene.paint.Color;
import simulation.Planet;
import simulation.Simulation;
import window.Window;

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

			neptunMass = 1.0243e26, neptunRad = 49528e3 / 2.0;

	private static Planet sun = new Planet(sunMass, sunRad, Color.YELLOW, "Sonne"),

			merkur = new Planet(merkurMass, merkurRad, Color.GREY, "Merkur"),

			venus = new Planet(venusMass, venusRad, Color.BEIGE, "Venus"),

			earth = new Planet(earthMass, earthRad, Color.BLUE, "Erde"),
			moon = new Planet(moonMass, moonRad, Color.GRAY, "Mond"),

			mars = new Planet(marsMass, marsRad, Color.RED, "Mars"),
			phobos = new Planet(1.072e16, 22.5e3, Color.GRAY, "Phobos"),
			deimos = new Planet(1.8e15, 13e3, Color.GRAY, "Deimos"),

			jupiter = new Planet(jupiterMass, jupiterRad, Color.PERU, "Jupiter"),

			saturn = new Planet(saturnMass, saturnRad, Color.BEIGE, "Saturn"),

			uranus = new Planet(uranusMass, uranusRad, Color.LIGHTBLUE, "Uranus"),

			neptun = new Planet(neptunMass, neptunRad, Color.BLUE, "Neptun");

	public static final Constellation solarSystem = getSolarSystem();

	// Solar System
	private static Constellation getSolarSystem() {
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
		merkur.setVel(0, orbVel(sunMass, merkurDis));

		venus.setPos(-venusDis, 0);
		venus.setVel(0, -orbVel(sunMass, venusDis));

		earth.setPos(0, earthDis);
		earth.setVel(-orbVel(sunMass, earthDis), 0);

		mars.setPos(marsDis, 0);
		mars.setVel(0, orbVel(sunMass, marsDis));

		jupiter.setPos(-jupiterDis, 0);
		jupiter.setVel(0, -orbVel(sunMass, jupiterDis));

		saturn.setPos(saturnDis, 0);
		saturn.setVel(0, orbVel(sunMass, saturnDis));

		uranus.setPos(0, uranusDis);
		uranus.setVel(-orbVel(sunMass, uranusDis), 0);

		neptun.setPos(0, -neptunDis);
		neptun.setVel(orbVel(sunMass, neptunDis), 0);

		Planet[] planets = { sun.clone(), merkur.clone(), venus.clone(), earth.clone(), mars.clone(), jupiter.clone(),
				saturn.clone(), uranus.clone(), neptun.clone() };

		return new Constellation("Sonnen System", planets, 2e-9, 100);
	}

	public static final Constellation earthSystem = getEarthSystem();

	// Earth - Moon System
	private static Constellation getEarthSystem() {
		double moonDistance = 384.4e6;

		earth.setPos(0, 0);
		earth.setVel(0, momComp(earthMass, moonMass, -orbVel(earthMass, moonDistance)));

		moon.setPos(-moonDistance, 0);
		moon.setVel(0, -orbVel(earthMass, moonDistance));

		Planet[] planets = { earth.clone(), moon.clone() };
		return new Constellation("Erde - Mond System", planets, 0.8e-6, 20);
	}

	public static final Constellation marsSystem = getMarsSystem();

	// Mars System
	private static Constellation getMarsSystem() {
		double phobosDistance = 9378e3;
		double deimosDistance = 23459e3;

		mars.setPos(0, 0);
		mars.setVel(0, 0);

		phobos.setPos(phobosDistance, 0);
		phobos.setVel(0, orbVel(marsMass, phobosDistance));

		deimos.setPos(-deimosDistance, 0);
		deimos.setVel(0, -orbVel(marsMass, deimosDistance));

		Planet[] planets = { mars.clone(), phobos.clone(), deimos.clone() };

		return new Constellation("Mars System", planets, 1.2e-5, 0.5);
	}

	public static final Constellation jupiterFlyby = getJupiterFlyby();

	// Jupiter Flyby
	private static Constellation getJupiterFlyby() {
		Planet ast1 = new Planet(-133708e3 * 17, -133708e3 * 4, 10000, 0, 0, 0, Color.GRAY, "Asteroid");
		Planet ast2 = new Planet(-133708e3 * 17, -133708e3 * 8, 10000, 0, 0, 0, Color.GRAY, "Asteroid");

		jupiter.setPos(0, 0);
		jupiter.setVel(0, 0);

		return new Constellation("Jupiter Flyby", new Planet[] { ast1, ast2, jupiter.clone() }, 3e-7, 3);
	}

	public static final Constellation symmetrical = getSymmetrical();

	// 8 symmetrical planets
	private static Constellation getSymmetrical() {
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

		return new Constellation("Symmetrische Konstellation", planets, 0.05, 10);
	}

	// Earth in low orbit around the sun
	public static final Constellation earthSunLow = getEarthSunLow();

	private static Constellation getEarthSunLow() {
		double earthDis = sunRad * 1.1;

		sun.setPos(0, 0);
		sun.setVel(0, 0);

		earth.setPos(-earthDis, 0);
		earth.setVel(0, -orbVel(sunMass, earthDis));

		return new Constellation("Sonne - Erde", new Planet[] { sun.clone(), earth.clone() }, 4e-7, 0.05);
	}

	// Earth in low orbit around the sun
	public static final Constellation collision = getCollision();

	private static Constellation getCollision() {
		double distance = mars.getRadius() * 100;
		double vel = 1000;

		mars.setPos(0, -distance);
		mars.setVel(0, vel);

		Planet mars2 = mars.clone();

		mars2.setPos(distance, -distance);
		mars2.setVel(0, vel / 2);

		earth.setPos(-distance, 0);
		earth.setVel(vel, 0);

		return new Constellation("Kollision", new Planet[] { mars.clone(), mars2, earth.clone() }, 8e-7, 5);
	}

	// momentum compensation
	private static double momComp(double ownMass, double otherMass, double otherSpeed) {
		return -otherMass * otherSpeed / ownMass;
	}

	// orbital velocity
	private static double orbVel(double otherMass, double distance) {
		return Math.sqrt(Simulation.GRAV_CONST * otherMass / distance);
	}

	// Random Planets
	public static final Constellation random = getRandom(30, 1);
	
	public static Constellation getRandom(int number, double scale) {
		return new Constellation("Random", random(number, scale), scale, 2);
	}

	public static Planet[] random(int number, double scale) {
		
		Planet[] planets = new Planet[number];

		for (int i = 0; i < number; i++)
			planets[i] = new Planet(
					plusMinus() * Math.random() * Window.winX / 3.0 * scale, 
					plusMinus() * Math.random() * Window.winY / 3.0 *scale,
					plusMinus() * Math.random() *0.001* scale, 
					plusMinus() * Math.random() *0.001* scale, 
					2.5);

		return planets;
	}

	// returns randomly 1 or -1
	public static int plusMinus() {
		if (Math.random() < 0.5)
			return -1;
		else
			return 1;
	}
}
