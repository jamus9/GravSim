package utils;

/**
 * 
 * @author Jan Muskalla
 *
 */
public class Orbit {

	private double sma;
	
	private double periapsis;
	
	private double argumentOfPeriapsis;

	public Orbit(double sma, double periapsis, double argumentOfPeriapsis) {
		this.sma = sma;
		this.periapsis = periapsis;
		this.argumentOfPeriapsis = argumentOfPeriapsis;
	}

	public Orbit(double periapsis, double apoapsis) {
		this.sma = (periapsis + apoapsis) / 2.0;
		this.periapsis = periapsis;
		this.argumentOfPeriapsis = 0;
	}
	
	public Orbit(double sma) {
		this.sma = this.periapsis = sma;
		argumentOfPeriapsis = 0;
	}

	public double getSma() {
		return sma;
	}

	public double getPeriapsis() {
		return periapsis;
	}

	public double getApoapsis() {
		return 2.0 * sma - periapsis;
	}

	public double getArgumentOfPeriapsis() {
		return argumentOfPeriapsis;
	}

	public void setArgumentOfPeriapsis(double argumentOfPeriapsis) {
		this.argumentOfPeriapsis = argumentOfPeriapsis;
	}

}
