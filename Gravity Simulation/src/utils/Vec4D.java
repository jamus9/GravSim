package utils;

public class Vec4D {

	private double x1, x2, x3, x4;
	
	public Vec4D(double x1, double x2, double x3, double x4) {
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
		this.x4 = x4;
	}

	public Vec4D() {
		this(0, 0, 0, 0);
	}

	// Getter
	
	public double x1() {
		return x1;
	}

	public double x2() {
		return x2;
	}

	public double x3() {
		return x3;
	}

	public double x4() {
		return x4;
	}
	
}
