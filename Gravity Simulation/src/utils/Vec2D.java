package utils;

public class Vec2D {

	private double x, y;

	public Vec2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vec2D() {
		this(0, 0);
	}

	// Getter
	
	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	// Operations
	
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

	public void print() {
		System.out.println(x + " " + y);
	}

	public boolean isEqual(Vec2D vec) {
		if (vec.x() == this.x && vec.y() == this.y)
			return true;
		else
			return false;
	}
}

