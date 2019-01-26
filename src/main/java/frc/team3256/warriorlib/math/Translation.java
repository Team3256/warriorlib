package frc.team3256.warriorlib.math;

import java.text.DecimalFormat;

public class Translation {
	private static final double kEpsilon = 1E-9;
	private double x;
	private double y;

	/**
	 * Create a translation vector from an x and y value.
	 *
	 * @param x x value
	 * @param y y value
	 */
	public Translation(double x, double y) {
		this.x = Math.abs(x) < kEpsilon ? 0.0 : x;
		this.y = Math.abs(y) < kEpsilon ? 0.0 : y;
	}

	/**
	 * Default constructor: Zero vector
	 */
	public Translation() {
		this(0.0, 0.0);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	/**
	 * Translate a translation vector.
	 *
	 * @param other vector to translate by
	 * @return the translated vector
	 */
	public Translation translate(Translation other) {
		return new Translation(x + other.x, y + other.y);
	}

	/**
	 * Rotate a translation vector.
	 *
	 * @param rotation rotation matrix to multiply the translation vector by
	 * @return the rotated translation vector
	 */
	public Translation rotate(Rotation rotation) {
		return new Translation(rotation.cos() * x - rotation.sin() * y, rotation.sin() * x + rotation.cos() * y);
	}

	@Override
	public String toString() {
		final DecimalFormat format = new DecimalFormat("#0.000");
		return "(" + format.format(x) + "," + format.format(y) + ")";
	}

	public boolean equals(Translation t) {
		return (this.x == t.x) && (this.y == t.y);
	}
}
