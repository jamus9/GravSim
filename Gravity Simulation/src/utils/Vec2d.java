package utils;

/**
 * A 2D vector with x and y coordinate
 * 
 * @author Jan Muskalla
 *
 */
public class Vec2d {

	private double x, y;

	public Vec2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2d(Vec2d v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vec2d(PolarVec2d pv) {
		this(pv.toVec2d());
	}

	public Vec2d() {
		this(0, 0);
	}
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Vec2d add(Vec2d v) {
		return new Vec2d(x + v.x, y + v.y);
	}

	public Vec2d sub(Vec2d v) {
		return new Vec2d(x - v.x, y - v.y);
	}

	public Vec2d mult(double a) {
		return new Vec2d(a * x, a * y);
	}

	public double norm() {
		return Math.sqrt(x * x + y * y);
	}
	
	public Vec2d getDir() {
		return this.mult(1.0 / this.norm());
	}
	
}
