package bodies;

/**
 * Implements a Constellation with an array of planets, a scale, time and name.
 * 
 * @author Jan Muskalla
 * 
 */
public class Constellation {

	private Planet[] planetArray;
	private Particle[] particleArray;
	private double scale;
	private double time;
	private String name;
	private int sps;

	private static final int defaultSps = 6000;

	/**
	 * Creates a new constellation with name, planets, particles, scale and time
	 * scale
	 * 
	 * @param name
	 * @param planetArray
	 * @param particleArray
	 * @param scale
	 * @param time
	 */
	public Constellation(String name, Planet[] planetArray, Particle[] particleArray, double scale, double time,
			int sps) {
		this.name = name;
		this.planetArray = planetArray;
		this.particleArray = particleArray;
		this.scale = scale;
		this.time = time;
		this.sps = sps;
	}

	/**
	 * Creates a new constellation with name, planets, particles, scale and time
	 * scale
	 * 
	 * @param name
	 * @param planetArray
	 * @param particleArray
	 * @param scale
	 * @param time
	 */
	public Constellation(String name, Planet[] planetArray, Particle[] particleArray, double scale, double time) {
		this(name, planetArray, particleArray, scale, time, defaultSps);
	}

	/**
	 * Creates a new constellation with name, planets, scale and time scale
	 * 
	 * @param name
	 * @param planetArray
	 * @param scale
	 * @param time
	 */
	public Constellation(String name, Planet[] planetArray, double scale, double time, int sps) {
		this(name, planetArray, new Particle[0], scale, time, sps);
	}

	/**
	 * Creates a new constellation with name, planets, scale and time scale
	 * 
	 * @param name
	 * @param planetArray
	 * @param scale
	 * @param time
	 */
	public Constellation(String name, Planet[] planetArray, double scale, double time) {
		this(name, planetArray, new Particle[0], scale, time, defaultSps);
	}

	public int numberOfPlanets() {
		return planetArray.length;
	}

	public int numberOfParticles() {
		return particleArray.length;
	}

	public String getName() {
		return name;
	}

	public Planet[] getPlanetArray() {
		return planetArray;
	}

	public Planet getPlanet(int i) {
		return planetArray[i];
	}

	public Particle[] getParticleArray() {
		return particleArray;
	}

	public Particle getParticle(int i) {
		return particleArray[i];
	}

	public double getScale() {
		return scale;
	}

	public double getTime() {
		return time;
	}

	public double getSps() {
		return sps;
	}

}
