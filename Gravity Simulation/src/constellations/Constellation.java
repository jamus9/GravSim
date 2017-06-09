package constellations;

import simulation.Planet;

/**
 * Implements a Constellation with an array of planets, a scale, time and name.
 * 
 * @author Jan Muskalla
 * 
 */
public class Constellation {

	private Planet[] planetArray;
	private double scale;
	private double time;
	private String name;
	
	public Constellation(String name, Planet[] planetArray, double scale, double time) {
		this.name = name;
		this.planetArray = planetArray;
		this.scale = scale;
		this.time = time;
	}
	
	public Planet[] getPlanetArray() {
		return planetArray;
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
	
	public Planet getPlanet(int i) {
		return planetArray[i];
	}
	
	public String getName() {
		return name;
	}
}
