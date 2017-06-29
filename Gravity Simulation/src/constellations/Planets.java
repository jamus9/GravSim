package constellations;

import bodies.Planet;
import javafx.scene.paint.Color;
import utils.Utils;

/**
 * Premade real and random planets and data.
 * 
 * @author Jan Muskalla
 *
 */
public class Planets {

	public static final double sunMass = 1.988e30, sunRad = 696342e3,

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

	private static final Planet sun = new Planet(sunMass, sunRad, Color.YELLOW, "Sun"),

			merkur = new Planet(merkurMass, merkurRad, Color.rgb(212, 201, 156), "Merkur"),

			venus = new Planet(venusMass, venusRad, Color.BEIGE, "Venus"),

			earth = new Planet(earthMass, earthRad, Color.rgb(79, 76, 176), "Earth"),
			moon = new Planet(moonMass, moonRad, Color.GRAY, "Moon"),

			mars = new Planet(marsMass, marsRad, Color.RED, "Mars"),

			jupiter = new Planet(jupiterMass, jupiterRad, Color.rgb(201, 144, 57), "Jupiter"),
			io = new Planet(8.94e22, 1821.6e3, Color.YELLOW, "Io"),
			europa = new Planet(4.799e22, 1560.8e3, Color.WHITE, "Europa"),
			ganymede = new Planet(1.4819e23, 2634.1e3, Color.GREY, "Ganymede"),
			callisto = new Planet(1.075e23, 2410e3, Color.DARKSLATEGRAY, "Callisto"),

			saturn = new Planet(saturnMass, saturnRad, Color.rgb(216, 202, 157), "Saturn"),

			uranus = new Planet(uranusMass, uranusRad, Color.rgb(225, 238, 238), "Uranus"),

			neptun = new Planet(neptunMass, neptunRad, Color.rgb(63, 84, 186), "Neptun"),

			blackHole = new Planet(blackHoleMass, blackHoleRad, Color.BLACK, "Black Hole");

	/**
	 * returns a random typical asteroid
	 * 
	 * @return the random Planet
	 */
	public static Planet getRandomAsteroid() {
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
	public static Planet getRandomMoon() {
		double density = moon.getDensity();
		double mass = Utils.getRandomInInervall(moonMass / 10, moonMass);
		return new Planet(0, 0, 0, 0, mass, density);
	}

	/**
	 * returns a random planet with a mass between merkur and 2 earth masses
	 * 
	 * @return the random Planet
	 */
	public static Planet getRandomPlanet() {
		double density = earth.getDensity();
		double mass = Utils.getRandomInInervall(merkurMass, earthMass * 2);
		return new Planet(0, 0, 0, 0, mass, density);
	}

	/**
	 * returns a random gas giant with a mass between uranus and jupiter
	 * 
	 * @return the random Planet
	 */
	public static Planet getRandomGasGiant() {
		double density = uranus.getDensity();
		double mass = Utils.getRandomInInervall(uranusMass, jupiterMass);
		return new Planet(0, 0, 0, 0, mass, density);
	}

	public static Planet getSun() {
		return sun.clone();
	}

	public static Planet getMerkur() {
		return merkur.clone();
	}

	public static Planet getVenus() {
		return venus.clone();
	}

	public static Planet getEarth() {
		return earth.clone();
	}

	public static Planet getMoon() {
		return moon.clone();
	}

	public static Planet getMars() {
		return mars.clone();
	}

	public static Planet getJupiter() {
		return jupiter.clone();
	}

	public static Planet getIo() {
		return io.clone();
	}

	public static Planet getEuropa() {
		return europa.clone();
	}

	public static Planet getGanymede() {
		return ganymede.clone();
	}

	public static Planet getCallisto() {
		return callisto.clone();
	}

	public static Planet getSaturn() {
		return saturn.clone();
	}

	public static Planet getUranus() {
		return uranus.clone();
	}

	public static Planet getNeptun() {
		return neptun.clone();
	}

	public static Planet getBlackhole() {
		return blackHole.clone();
	}

}
