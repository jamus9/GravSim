package utils;

/**
 * An orbit for a body with semi-major axis, periapsis and argument of
 * periapsis.
 * 
 * @author Jan Muskalla
 *
 */
public class Orbit {

	private double sma;
	private double periapsis;
	private double argOfPeriapsis;

	public Orbit(double sma, double periapsis, double argOfPeriapsis) {
		this.sma = sma;
		this.periapsis = periapsis;
		this.argOfPeriapsis = argOfPeriapsis;
	}

	public Orbit(double periapsis, double apoapsis) {
		this((periapsis + apoapsis) / 2.0, periapsis, 0);
	}

	public Orbit(double sma) {
		this(sma, sma, 0);
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

	public double getArgOfPeriapsis() {
		return argOfPeriapsis;
	}

	public void setArgOfPeriapsis(double argOfPeriapsis) {
		this.argOfPeriapsis = argOfPeriapsis;
	}

	public double getArgOfApoapsis() {
		return argOfPeriapsis + 90;
	}

}
