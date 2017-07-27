package utils;

/**
 * A two dimensional vector in polar coordinates with radius and angle.
 * 
 * @author Jan Muskalla
 *
 */
public class PolarVec implements Vec2d {

	private double radius;
	private double angle;

	public PolarVec(double radius, double angle) {
		this.radius = radius;
		this.angle = Vec2d.toRad(angle);
	}

	public PolarVec(Vec2d v) {
		this(v.getRadius(), v.getAngle());
	}

	public PolarVec() {
		this(0, 0);
	}
	
	@Override
	public void set(double radius, double angle) {
		this.radius = radius;
		this.angle = angle;
	}

	@Override
	public double getX() {
		return radius * Math.cos(angle);
	}

	@Override
	public double getY() {
		return radius * Math.sin(angle);
	}

	@Override
	public double getRadius() {
		return radius;
	}

	@Override
	public double getAngle() {
		return Vec2d.toDegree(angle);
	}

	@Override
	public PolarVec mult(double a) {
		return new PolarVec(a * radius, angle);
	}

	@Override
	public PolarVec add(Vec2d v) {
		return new PolarVec((new Vec(this)).add(v));
	}

	@Override
	public PolarVec sub(Vec2d v) {
		return new PolarVec((new Vec(this)).sub(v));
	}

	@Override
	public Vec2d normalize() {
		return new PolarVec(1, angle);
	}

	@Override
	public String toString() {
		return radius + " " + angle;
	}

	@Override
	public PolarVec clone() {
		return new PolarVec(this);
	}

}
