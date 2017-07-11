
package utils;

/**
 * A 2D Vector in polar coordinates with distance r and angle.
 * 
 * @author Jan Muskalla
 *
 */
public class PolarVec2d {

	private double r;
	private double angle;

	/**
	 * creates a new polar vector with radius and angle in degrees
	 * @param r
	 * @param angle
	 */
	public PolarVec2d(double r, double angle) {
		this.r = r;
		this.angle = toRad(angle);
	}
	
	public PolarVec2d(PolarVec2d pv) {
		this.r = pv.r;
		this.angle = pv.angle;
	}
	
	public PolarVec2d() {
		this.r = 0;
		this.angle = 0;
	}

	private double toRad(double a) {
		return (Math.PI / 180.0) * a;
	}

	public Vec2d toVec2d() {
		return new Vec2d(r * Math.cos(angle), r * Math.sin(angle));
	}

	public void set(double r, double angle) {
		this.r = r;
		this.angle = toRad(angle);
	}

	public double getR() {
		return r;
	}

	public double getAngle() {
		return angle;
	}

}
