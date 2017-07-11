package utils;

/**
 * A two dimensional vector in polar coordinates with radius and angle.
 * 
 * @author Jan Muskalla
 *
 */
public class PolarVec2d {

	private double radius;
	private double angle;

	public PolarVec2d(double radius, double angle) {
		this.radius = radius;
		this.angle = toRad(angle);
	}

	public PolarVec2d(PolarVec2d pv) {
		this.radius = pv.radius;
		this.angle = pv.angle;
	}

	public PolarVec2d(Vec2d v) {
		this(v.toPolarVec2d());
	}

	public PolarVec2d() {
		this.radius = 0;
		this.angle = 0;
	}

	private double toRad(double a) {
		return (Math.PI / 180.0) * a;
	}

	public Vec2d toVec2d() {
		return new Vec2d(radius * Math.cos(angle), radius * Math.sin(angle));
	}

	public void set(double radius, double angle) {
		this.radius = radius;
		this.angle = toRad(angle);
	}

	public double getRadius() {
		return radius;
	}

	public double getAngle() {
		return angle;
	}

	public PolarVec2d mult(double a) {
		return new PolarVec2d(a * radius, angle);
	}

	@Override
	public String toString() {
		return radius + " " + angle;
	}

	@Override
	public PolarVec2d clone() {
		return new PolarVec2d(this);
	}

}
