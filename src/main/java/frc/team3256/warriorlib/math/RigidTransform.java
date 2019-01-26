package frc.team3256.warriorlib.math;

public class RigidTransform {
	private static final double kEpsilon = 1E-9;
	private Translation translation;
	private Rotation rotation;

	public RigidTransform(Translation translation, Rotation rotation) {
		this.translation = translation;
		this.rotation = rotation;
	}

	public RigidTransform() {
		this(new Translation(), new Rotation());
	}

	/**
	 * ethaneade.com/lie_groups.pdf
	 * Exponential map for 2D rigid transformation
	 * Basically this converts a {@link Twist} ---(exponential map)---> transformation
	 * Kind of like an differential position ---(integral)---> position
	 * <p>
	 * Twist: [[dx]
	 * [dy]
	 * [dtheta]]
	 * <p>
	 * Rotation: [[cos(dtheta), -sin(dtheta)]
	 * [sin(dtheta),  cos(dtheta)]]
	 * <p>
	 * Translation: [[sin(dtheta)/dtheta,      -(1-cos(dtheta)/detheta)]  * [[dx]
	 * [(1-cos(dtheta)/dtheta),  sin(dtheta)/dtheta)     ]]    [dy]]
	 *
	 * @param twist Input twist
	 * @return Transformation based on the twist
	 */
	public static RigidTransform exp(Twist twist) {
		double dtheta = twist.dtheta();
		double cos = Math.cos(dtheta);
		double sin = Math.sin(dtheta);
		Rotation rot = new Rotation(cos, sin);
		double sin_theta_over_theta;
		double one_minus_cos_theta_over_theta;
		//if theta is very small, we need to use taylor series to approximate the values
		//as we can't divide by 0
		if (Math.abs(dtheta) < kEpsilon) {
			sin_theta_over_theta = 1.0 - Math.pow(dtheta, 2) / 6.0 + Math.pow(dtheta, 4) / 120.0;
			one_minus_cos_theta_over_theta = 1.0 / 2.0 * dtheta - Math.pow(dtheta, 3) / 24.0 + Math.pow(twist.dtheta(), 5) / 720.0;
		} else {
			sin_theta_over_theta = sin / dtheta;
			one_minus_cos_theta_over_theta = (1.0 - cos) / dtheta;
		}
		Translation translation = new Translation(sin_theta_over_theta * twist.dx(), one_minus_cos_theta_over_theta * twist.dx());
		return new RigidTransform(translation, rot);
	}

	public Translation getTranslation() {
		return translation;
	}

	public Rotation getRotation() {
		return rotation;
	}

	/**
	 * Transforms (rotates + translates) this transformation matrix by another transformation matrix
	 * Basically multiplies two transformation matrices (see above the class declaration) together.
	 * <p>
	 * In other words, we first rotation this translation vector by the other rotation vector (taking into account this rotation vector),
	 * then we translate this translation vector by the other translation vector
	 *
	 * @param other Transformation matrix to transform this matrix by
	 * @return The transformed matrix
	 */
	public RigidTransform transform(RigidTransform other) {
		return new RigidTransform(translation.translate(other.getTranslation().rotate(rotation)), rotation.rotate(other.getRotation()));
	}

	@Override
	public String toString() {
		return "T:" + translation.toString() + ", R:" + rotation.toString();
	}
}
