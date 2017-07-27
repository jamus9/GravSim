package utils;

/**
 * A two dimensional vector with x and y coordinate
 * 
 * @author Jan Muskalla
 *
 */
public class Vec implements Vec2d {

	private double x;
	private double y;

	public Vec(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vec(Vec2d v) {
		this(v.getX(), v.getY());
	}

	public Vec() {
		this(0, 0);
	}

	@Override
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getRadius() {
		return Math.sqrt(x * x + y * y);
	}

	@Override
	public double getAngle() {
		return Vec2d.toDegree(Math.tan(y / x));
	}

	@Override
	public Vec mult(double a) {
		return new Vec(a * x, a * y);
	}

	@Override
	public Vec add(Vec2d v) {
		return new Vec(x + v.getX(), y + v.getY());
	}

	@Override
	public Vec sub(Vec2d v) {
		return new Vec(x - v.getX(), y - v.getY());
	}

	@Override
	public Vec normalize() {
		return this.mult(1.0 / this.getRadius());
	}

	@Override
	public String toString() {
		return x + " " + y;
	}

	@Override
	public Vec clone() {
		return new Vec(this);
	}

}
