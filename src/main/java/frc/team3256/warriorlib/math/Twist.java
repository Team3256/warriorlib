package frc.team3256.warriorlib.math;

import java.text.DecimalFormat;

public class Twist {
	private double dx;
	private double dy;
	private double dtheta;

	public Twist(double dx, double dy, double dtheta) {
		this.dx = dx;
		this.dy = dy;
		this.dtheta = dtheta;
	}

	public Twist() {
		this(0, 0, 0);
	}

	public double dx() {
		return dx;
	}

	public double dy() {
		return dy;
	}

	public double dtheta() {
		return dtheta;
	}

	@Override
	public String toString() {
		final DecimalFormat format = new DecimalFormat("#0.000");
		return "(" + format.format(dx) + "," + format.format(dy) + "," + format.format(dtheta) + ")";
	}
}
