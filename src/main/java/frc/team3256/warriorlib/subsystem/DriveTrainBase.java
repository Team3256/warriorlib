package frc.team3256.warriorlib.subsystem;

import frc.team3256.warriorlib.math.Rotation;

/**
 * For use with Pure Pursuit algorithm. Suggested units are inches and seconds
 */
public abstract class DriveTrainBase extends SubsystemBase {
	private static DriveTrainBase driveTrain;

	/**
	 * @return drivetrain used for Pure Pursuit purposes
	 */
	public static DriveTrainBase getDriveTrain() {
		return driveTrain;
	}

	/**
	 * Sets the drivetrain used by Pure Pursuit algorithm, should only be called once in robot code
	 *
	 * @param driveTrain drivetrain used
	 */
	public static void setDriveTrain(DriveTrainBase driveTrain) {
		DriveTrainBase.driveTrain = driveTrain;
	}

	/**
	 * @return left encoder distance value
	 */
	public abstract double getLeftDistance();

	/**
	 * @return right encoder distance value
	 */
	public abstract double getRightDistance();

	/**
	 * NOTE: for typical implementation, angle should start at 90 degrees rather than zero. The idea is to go forward to increase y, and go right to increase x
	 * @return {@link frc.team3256.warriorlib.math.Rotation} that represents current gyro angle
	 */
	public abstract Rotation getRotationAngle();

	/**
	 * @return velocity of left side of robot
	 */
	public abstract double getLeftVelocity();

	/**
	 * @return velocity of right side of robot
	 */
	public abstract double getRightVelocity();

	/**
	 * Sets left and right velocities of the robot
	 * @param left left velocity
	 * @param right right velocity
	 */
	public abstract void setVelocityClosedLoop(double left, double right);

	/**
	 * Resets encoders to zero
	 */
	public abstract void resetEncoders();

	/**
	 * Resets gyro angle to zero
	 */
	public abstract void resetGyro();
}
