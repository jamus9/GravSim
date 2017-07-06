package utils;

/**
 * A 2D vector with x and y coordinate
 * 
 * @author Jan Muskalla
 *
 */
public class Vec2D {

	private double x, y;

	public Vec2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2D(Vec2D v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vec2D(PolarVec2D pv) {
		this(pv.toVec2D());
	}

	public Vec2D() {
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

	public Vec2D add(Vec2D v) {
		return new Vec2D(x + v.x, y + v.y);
	}

	public Vec2D sub(Vec2D v) {
		return new Vec2D(x - v.x, y - v.y);
	}

	public Vec2D mult(double a) {
		return new Vec2D(a * x, a * y);
	}

	public double norm() {
		return Math.sqrt(x * x + y * y);
	}
	
	public Vec2D getDir() {
		return this.mult(1.0 / this.norm());
	}
	
}
