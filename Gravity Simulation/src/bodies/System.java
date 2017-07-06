package bodies;

import java.util.ArrayList;

/**
 * Implements a System with an array of planets, a scale, time and name.
 * 
 * @author Jan Muskalla
 * 
 */
public class System {

	private ArrayList<Planet> planetArray;
	private ArrayList<Particle> particleArray;
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
	public System(String name, ArrayList<Planet> planetArray, ArrayList<Particle> particleArray, double scale,
			double time, int sps) {
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
	public System(String name, ArrayList<Planet> planetArray, ArrayList<Particle> particleArray, double scale,
			double time) {
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
	public System(String name, ArrayList<Planet> planetArray, double scale, double time, int sps) {
		this(name, planetArray, new ArrayList<Particle>(), scale, time, sps);
	}

	/**
	 * Creates a new constellation with name, planets, scale and time scale
	 * 
	 * @param name
	 * @param planetArray
	 * @param scale
	 * @param time
	 */
	public System(String name, ArrayList<Planet> planetArray, double scale, double time) {
		this(name, planetArray, new ArrayList<Particle>(), scale, time, defaultSps);
	}

	public int numberOfPlanets() {
		return planetArray.size();
	}

	public int numberOfParticles() {
		return particleArray.size();
	}

	public String getName() {
		return name;
	}

	public ArrayList<Planet> getPlanetArray() {
		return planetArray;
	}

	public Planet getPlanet(int i) {
		return planetArray.get(i);
	}

	public ArrayList<Particle> getParticleArray() {
		return particleArray;
	}

	public Particle getParticle(int i) {
		return particleArray.get(i);
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

	public System clone() {
		ArrayList<Planet> planetArrayNew = new ArrayList<>();
		for (Planet p : planetArray)
			planetArrayNew.add(p.clone());

		ArrayList<Particle> particleArrayNew = new ArrayList<>();
		for (Particle p : particleArray)
			particleArrayNew.add(p.clone());

		return new System(this.name, planetArrayNew, particleArrayNew, this.scale, this.time, this.sps);
	}

}
