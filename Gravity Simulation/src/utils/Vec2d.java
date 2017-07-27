package utils;

/**
 * Interface for a two dimensional vector.
 * 
 * @author Jan Muskalla
 *
 */
public interface Vec2d {
	
	public void set(double x, double y);

	public double getX();

	public double getY();

	public double getRadius();

	public double getAngle();

	public Vec2d mult(double a);
	
	public Vec2d add(Vec2d v);
	
	public Vec2d sub(Vec2d v);

	public Vec2d normalize();

	public Vec2d clone();

	public String toString();

	public static double toRad(double a) {
		return (Math.PI / 180.0) * a;
	}

	public static double toDegree(double a) {
		return (180.0 / Math.PI) * a;
	}

}
