package frc.team3256.warriorlib.math;

import java.text.DecimalFormat;

public class Rotation {
	private static final double kEpsilon = 1E-9;
	private double cos;
	private double sin;

	/**
	 * Create a rotation matrix from the sin and cos of the angle.
	 *
	 * @param cos       cos of the angle of the rotation
	 * @param sin       sin of the angle of the rotation
	 * @param normalize whether or not we should "normalize" this rotation {@link #normalize()}
	 */
	public Rotation(double cos, double sin, boolean normalize) {
		this.cos = Math.abs(cos) < kEpsilon ? 0.0 : cos;
		this.sin = Math.abs(sin) < kEpsilon ? 0.0 : sin;
		if (normalize)
			normalize();
	}

	public Rotation(double cos, double sin) {
		this(cos, sin, false);
	}

	/**
	 * Default constructor: rotation matrix of angle 0
	 */
	public Rotation() {
		this(1.0, 0.0, false);
	}

	/**
	 * Create a rotation matrix from a translation vector. This basically is the angle from the
	 * positive x axis to the translation vector.
	 *
	 * @param directionVector the translation vector to calculate the angle from
	 */
	public Rotation(Translation directionVector) {
		this(directionVector.getX(), directionVector.getY(), true);
	}

	public static Rotation fromRadians(double radians) {
		return new Rotation(Math.cos(radians), Math.sin(radians));
	}

	public static Rotation fromDegrees(double degrees) {
		return fromRadians(Math.toRadians(degrees));
	}

	/**
	 * Normalizes the rotation matrix by forcing the sin and cos values to be in the unit circle.
	 */
	private void normalize() {
		double magnitude = Math.hypot(cos, sin);
		if (magnitude > kEpsilon) {
			cos /= magnitude;
			sin /= magnitude;
		} else {
			cos = 1.0;
			sin = 0.0;
		}
	}

	public double cos() {
		return cos;
	}

	public double sin() {
		return sin;
	}

	public double tan() {
		if (Math.abs(this.cos) < kEpsilon) {
			return this.sin >= 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
		}
		return this.sin / this.cos;
	}

	/**
	 * Returns between -pi and pi
	 *
	 * @return The angle in radians
	 */
	public double radians() {
		return Math.atan2(sin, cos);
	}

	public double degrees() {
		return Math.toDegrees(radians());
	}

	/**
	 * Calculates the Normal or perpendicular rotation matrix to the angle of this rotation matrix
	 * Example: If this represents a rotation matrix of 0 degrees, normal will return a rotation matrix for 90 degrees
	 *
	 * @return The normal rotation matrix to this rotation matrix
	 */
	public Rotation normal() {
		return new Rotation(-this.sin, this.cos);
	}

	/**
	 * Rotates this rotation matrix by a specified angle
	 * <p>
	 * Basically multiplies the specified rotation matrix after this rotation matrix
	 * <p>
	 * [[cos', -sin']    =    [[cos_0, -sin_0]  *  [[cos_1, -sin_1]   =  [[cos_0*cos_1+(-sin_0)*sin_1, cos_0*(-sin_1)+(-sin_0)*cos_1]
	 * [sin',  cos']]         [sin_0,  cos_0]]     [sin_1,  cos_1]]      [sin_0*cos_1+cos_0*sin_1,    sin_0*(-sin_1)+cos_0*cos_1   ]]
	 * So for implementation purposes, basically:
	 * cos' = cos_0*cos_1+(-sin_0)*sin_1
	 * sin' = sin_0*cos_1+cos_0*sin_1
	 *
	 * @param other Angle to rotate this rotation matrix by
	 * @return Rotated rotation matrix
	 */
	public Rotation rotate(Rotation other) {
		return new Rotation(this.cos * other.cos - this.sin * other.sin, this.sin * other.cos + this.cos * other.sin, true);
	}

	/**
	 * Calculates the inverse of this rotation matrix
	 * Basically what when multiplied after this matrix will turn it into the identity matrix
	 * In other words, it's the rotation matrix representing the angle that is the opposite of this angle
	 * <p>
	 * Since rotation matrices are orthagonal matrices, the inverse is the same as the transpose
	 * Calculating the transpose is much cheaper than calculating the inverse
	 * <p>
	 * [[cos, -sin]T = [[cos, sin]
	 * [sin, cos]]     [-sin, cos]]
	 * <p>
	 * For implementation purposes,
	 * cos' = cos
	 * sin' = -sin
	 *
	 * @return Inverse of this rotation matrix
	 */
	public Rotation inverse() {
		return new Rotation(this.cos, -this.sin);
	}

	@Override
	public String toString() {
		final DecimalFormat format = new DecimalFormat("#0.000");
		return "(" + format.format(this.degrees()) + "deg)";
	}
}
