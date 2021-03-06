package systems;

import bodies.Planet;
import javafx.scene.paint.Color;
import utils.Orbit;
import utils.Utils;

/**
 * Real and random planets and data.
 * 
 * @author Jan Muskalla
 *
 */
public class PlanetData {

	/**
	 * real planet data
	 */
	public static final double sunMass = 1.988e30;
	public static final double sunRad = 696342e3;

	public static final double mercuryMass = 3.301e23;
	public static final double mercuryRad = 4879.4e3 / 2;
	public static final double mercurySma = 57909050e3;

	public static final double venusMass = 4.869e24;
	public static final double venusRad = 6052e3;
	public static final double venusSma = 108.208e9;

	public static final double earthMass = 5.974e24;
	public static final double earthRad = 12756.32e3 / 2;
	public static final double earthSma = 147.598023e9;
	public static final double AU = 149597870700d;

	public static final double moonMass = 7.349e22;
	public static final double moonRad = 3476e3 / 2;
	public static final double moonSma = 384.399e6;

	public static final double marsMass = 6.419e23;
	public static final double marsRad = 6792.4e3 / 2;
	public static final double marsSma = 227.9392e9;

	public static final double ceresMass = 9.393e20;
	public static final double ceresRad = 891e3 / 2;
	public static final double ceresSma = 413.94e9;

	public static final double jupiterMass = 1.899e27;
	public static final double jupiterRad = 133708e3 / 2;
	public static final double jupiterSma = 778.299e9;

	public static final double saturnMass = 5.683e26;
	public static final double saturnRad = 58232e3;
	public static final double saturnSma = 1429e9;

	public static final double uranusMass = 8.683e25;
	public static final double uranusRad = 51118e3 / 2;
	public static final double uranusSma = 2875.04e9;

	public static final double neptuneMass = 1.024e26;
	public static final double neptuneRad = 49528e3 / 2;
	public static final double neptuneSma = 4504.45e9;

	public static final double plutoMass = 1.303e22;
	public static final double plutoRad = 1189.9e3;
	public static final double plutoSma = 5906.38e9;

	/**
	 * planets
	 */
	
	// Solar System
	private static final Planet sun = new Planet(sunMass, sunRad, Color.YELLOW, "Sun");

	private static final Planet mercury = new Planet(mercuryMass, mercuryRad, Color.rgb(212, 201, 156), "Mercury");

	private static final Planet venus = new Planet(venusMass, venusRad, Color.rgb(205, 165, 69), "Venus");

	private static final Planet earth = new Planet(earthMass, earthRad, Color.rgb(114, 111, 191), "Earth");
	private static final Planet moon = new Planet(moonMass, moonRad, Color.GRAY, "Moon");

	private static final Planet mars = new Planet(marsMass, marsRad, Color.rgb(199, 86, 38), "Mars");
	private static final Planet phobos = new Planet(1.072e16, 22.5e3, Color.GRAY, "Phobos");
	private static final Planet deimos = new Planet(1.8e15, 13e3, Color.GRAY, "Deimos");

	private static final Planet ceres = new Planet(ceresMass, ceresRad, Color.GRAY, "Ceres");

	private static final Planet jupiter = new Planet(jupiterMass, jupiterRad, Color.rgb(201, 144, 57), "Jupiter");
	private static final Planet io = new Planet(8.94e22, 1821.6e3, Color.YELLOW, "Io");
	private static final Planet europa = new Planet(4.799e22, 1560.8e3, Color.WHITE, "Europa");
	private static final Planet ganymede = new Planet(1.4819e23, 2634.1e3, Color.GREY, "Ganymede");
	private static final Planet callisto = new Planet(1.075e23, 2410e3, Color.DARKSLATEGRAY, "Callisto");

	private static final Planet saturn = new Planet(saturnMass, saturnRad, Color.rgb(216, 202, 157), "Saturn");
	private static final Planet enceladus = new Planet(1.08e20, 504.2e3 / 2, Color.WHITE, "Enceladus");
	private static final Planet tethys = new Planet(6.18e20, 1066e3 / 2, Color.GRAY, "Tethys");
	private static final Planet dione = new Planet(1.096e21, 1123.4e3 / 2, Color.GRAY, "Dione");
	private static final Planet rhea = new Planet(2.3e21, 1529e3 / 2, Color.GRAY, "Rhea");
	private static final Planet titan = new Planet(1.3e23, 5150e3 / 2, Color.BEIGE, "Titan");
	private static final Planet iapetus = new Planet(2e21, 1436e3 / 2, Color.GRAY, "Iapetus");

	private static final Planet uranus = new Planet(uranusMass, uranusRad, Color.rgb(225, 238, 238), "Uranus");

	private static final Planet neptune = new Planet(neptuneMass, neptuneRad, Color.rgb(63, 84, 186), "Neptun");

	private static final Planet pluto = new Planet(plutoMass, plutoRad, Color.rgb(205, 165, 69), "Pluto");

	// Other
	private static final Planet sagittariusA = new Planet(sunMass * 4e6, 22.5e9 / 2, Color.BLACK, "Sagittarius A*");

	private static final Planet betelgeuse = new Planet(11.6 * sunMass, 887 * sunRad, Color.ORANGE, "Betelgeuse");

	/**
	 * orbits
	 */
	public static final Orbit mercuryOrbit = new Orbit(mercurySma, 46001.2e6, 29.124);
	public static final Orbit venusOrbit = new Orbit(venusSma, 107477e6, 54.884);
	public static final Orbit earthOrbit = new Orbit(earthSma, 147095e6, 114.20783);
	public static final Orbit moonOrbit = new Orbit(moonSma, 362.6e6, 0);
	public static final Orbit marsOrbit = new Orbit(marsSma, 1.3814 * AU, 286.502);
	public static final Orbit ceresOrbit = new Orbit(ceresSma, 2.5577 * AU, 72.522);
	public static final Orbit jupiterOrbit = new Orbit(jupiterSma, 740.55e9, 273.867);
	public static final Orbit saturnOrbit = new Orbit(saturnSma, 1.35e12, 339.392);
	public static final Orbit uranusOrbit = new Orbit(uranusSma, 2.742e12, 98.99);
	public static final Orbit neptuneOrbit = new Orbit(neptuneSma, 4.46e12, 276.336);
	public static final Orbit plutoOrbit = new Orbit(plutoSma, 4436.82e9, 113.8);

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
	 * returns a random planet with a mass between Mercury and 2 earth masses
	 * 
	 * @return the random Planet
	 */
	public static Planet getRandomPlanet() {
		double density = earth.getDensity();
		double mass = Utils.getRandomInInervall(mercuryMass, earthMass * 2);
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

	public static Planet getMercury() {
		return mercury.clone();
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

	public static Planet getNeptune() {
		return neptune.clone();
	}

	public static Planet getSagittariusA() {
		return sagittariusA.clone();
	}

	public static Planet getBetelgeuse() {
		return betelgeuse.clone();
	}

	public static Planet getEnceladus() {
		return enceladus.clone();
	}

	public static Planet getTethys() {
		return tethys.clone();
	}

	public static Planet getDione() {
		return dione.clone();
	}

	public static Planet getRhea() {
		return rhea.clone();
	}

	public static Planet getTitan() {
		return titan.clone();
	}

	public static Planet getIapetus() {
		return iapetus.clone();
	}

	public static Planet getPhobos() {
		return phobos.clone();
	}

	public static Planet getDeimos() {
		return deimos.clone();
	}

	public static Planet getCeres() {
		return ceres.clone();
	}

	public static Planet getPluto() {
		return pluto.clone();
	}

//	public static Orbit getMercuryorbit() {
//		return mercuryOrbit;
//	}
//
//	public static Orbit getVenusorbit() {
//		return venusOrbit;
//	}
//
//	public static Orbit getJupiterorbit() {
//		return jupiterOrbit;
//	}
//
//	public static Orbit getEarthorbit() {
//		return earthOrbit;
//	}
//
//	public static Orbit getMarsorbit() {
//		return marsOrbit;
//	}
//
//	public static Orbit getCeresorbit() {
//		return ceresOrbit;
//	}
//
//	public static Orbit getSaturnorbit() {
//		return saturnOrbit;
//	}
//
//	public static Orbit getPlutoorbit() {
//		return plutoOrbit;
//	}

}
