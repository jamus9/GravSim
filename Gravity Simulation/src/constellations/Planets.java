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

	public static final double sunMass = 1.988e30;
	public static final double sunRad = 696342e3;

	public static final double merkurMass = 3.301e23;
	public static final double merkurRad = 4879.4e3 / 2.0;
	public static final double merkurDis = 57.909e9;

	public static final double venusMass = 4.869e24;
	public static final double venusRad = 6052e3;
	public static final double venusDis = 108.16e9;

	public static final double earthMass = 5.974e24;
	public static final double earthRad = 12756320 / 2.0;
	public static final double earthDis = 149.6e9;
	public static final double AU = earthDis;

	public static final double moonMass = 7.349e22;
	public static final double moonRad = 3476e3 / 2.0;
	public static final double moonDis = 384.4e6;

	public static final double marsMass = 6.419e23;
	public static final double marsRad = 6792.4e3 / 2.0;
	public static final double marsDis = 227.99e9;

	public static final double jupiterMass = 1.899e27;
	public static final double jupiterRad = 133708e3 / 2.0;
	public static final double jupiterDis = 778.23e9;

	public static final double saturnMass = 5.683e26;
	public static final double saturnRad = 58232e3;
	public static final double saturnDis = 1433.5e9;

	public static final double uranusMass = 8.683e25;
	public static final double uranusRad = 51118e3 / 2.0;
	public static final double uranusDis = 2872.4e9;

	public static final double neptunMass = 1.024e26;
	public static final double neptunRad = 49528e3 / 2.0;
	public static final double neptunDis = 4498.4e9;

	public static final double sagittariusAMass = sunMass * 4e6;
	public static final double sagittariusARad = 22.5e9 / 2.0;

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
			callisto = new Planet(1.075e23, 2410e3, Color.DARKSLATEGRAY, "Callisto");

	private static final Planet saturn = new Planet(saturnMass, saturnRad, Color.rgb(216, 202, 157), "Saturn");
	private static final Planet enceladus = new Planet(1.08e20, 504.2e3, Color.WHITE, "Enceladus");
	private static final Planet tethys = new Planet(6.18e20, 1066e3, Color.GRAY, "Tethys");
	private static final Planet dione = new Planet(1.096e21, 1123.4e3, Color.GRAY, "Dione");
	private static final Planet rhea = new Planet(2.3e21, 1529e3, Color.GRAY, "Rhea");
	private static final Planet titan = new Planet(1.3e23, 5150e3, Color.BEIGE, "Titan");
	private static final Planet iapetus = new Planet(2e21, 1436e3, Color.GRAY, "Iapetus");

	private static final Planet uranus = new Planet(uranusMass, uranusRad, Color.rgb(225, 238, 238), "Uranus"),
			neptun = new Planet(neptunMass, neptunRad, Color.rgb(63, 84, 186), "Neptun"),
			sagittariusA = new Planet(sagittariusAMass, sagittariusARad, Color.BLACK, "Sagittarius A*");

	private static final Planet betelgeuse = new Planet(11.6 * sunMass, 887 * sunRad, Color.ORANGE, "Betelgeuse");

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

	public static Planet getSagittariusA() {
		return sagittariusA.clone();
	}

	public static Planet getBetelgeuse() {
		return betelgeuse;
	}

	public static Planet getEnceladus() {
		return enceladus;
	}

	public static Planet getTethys() {
		return tethys;
	}

	public static Planet getDione() {
		return dione;
	}

	public static Planet getRhea() {
		return rhea;
	}

	public static Planet getTitan() {
		return titan;
	}

	public static Planet getIapetus() {
		return iapetus;
	}

}
