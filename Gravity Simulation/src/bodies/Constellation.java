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
		this.name = name;
		this.planetArray = planetArray;
		this.particleArray = particleArray;
		this.scale = scale;
		this.time = time;
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
		this(name, planetArray, new Particle[0], scale, time);
	}

	public Planet[] getPlanetArray() {
		return planetArray;
	}

	public Particle[] getParticleArray() {
		return particleArray;
	}

	public double getScale() {
		return scale;
	}

	public double getTime() {
		return time;
	}

	public int numberOfPlanets() {
		return planetArray.length;
	}

	public int numberOfParticles() {
		return particleArray.length;
	}

	public Planet getPlanet(int i) {
		return planetArray[i];
	}

	public Particle getParticle(int i) {
		return particleArray[i];
	}

	public String getName() {
		return name;
	}
	
}
