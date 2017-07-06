
package utils;

/**
 * A 2D Vector in polar coordinates with distance r and angle.
 * 
 * @author Jan Muskalla
 *
 */
public class PolarVec2D {

	private double r;
	private double angle;

	public PolarVec2D(double r, double angle) {
		this.r = r;
		this.angle = angle;
	}
	
	public PolarVec2D(PolarVec2D pv) {
		this.r = pv.r;
		this.angle = pv.angle;
	}
	
	public PolarVec2D() {
		this.r = 0;
		this.angle = 0;
	}

	public Vec2D toVec2D() {
		return new Vec2D(r * Math.cos(angle), r * Math.sin(angle));
	}

	public void set(double r, double angle) {
		this.r = r;
		this.angle = angle;
	}

	public double getR() {
		return r;
	}

	public double getAngle() {
		return angle;
	}

}
