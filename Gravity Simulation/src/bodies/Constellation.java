package bodies;

import java.util.ArrayList;

/**
 * Implements a Constellation with an array of planets, a scale, time and name.
 * 
 * @author Jan Muskalla
 * 
 */
public class Constellation {

	private static final int defaultSps = 6000;

	private ArrayList<Planet> planetList;
	private ArrayList<Particle> particleList;
	private double scale;
	private double time;
	private String name;
	private int sps;

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
	public Constellation(String name, ArrayList<Planet> planetArray, ArrayList<Particle> particleArray, double scale,
			double time, int sps) {
		this.name = name;
		this.planetList = planetArray;
		this.particleList = particleArray;
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
	public Constellation(String name, ArrayList<Planet> planetArray, ArrayList<Particle> particleArray, double scale,
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
	public Constellation(String name, ArrayList<Planet> planetArray, double scale, double time, int sps) {
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
	public Constellation(String name, ArrayList<Planet> planetArray, double scale, double time) {
		this(name, planetArray, new ArrayList<Particle>(), scale, time, defaultSps);
	}

	public int numberOfPlanets() {
		return planetList.size();
	}

	public int numberOfParticles() {
		return particleList.size();
	}

	public String getName() {
		return name;
	}

	public ArrayList<Planet> getPlanetList() {
		return planetList;
	}

	public ArrayList<Particle> getParticleList() {
		return particleList;
	}

	public Planet getPlanet(int i) {
		return planetList.get(i);
	}

	public Particle getParticle(int i) {
		return particleList.get(i);
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

	public Constellation clone() {
		ArrayList<Planet> planetArrayNew = new ArrayList<>();
		for (Planet p : planetList)
			planetArrayNew.add(p.clone());

		ArrayList<Particle> particleArrayNew = new ArrayList<>();
		for (Particle p : particleList)
			particleArrayNew.add(p.clone());

		return new Constellation(this.name, planetArrayNew, particleArrayNew, this.scale, this.time, this.sps);
	}

}
