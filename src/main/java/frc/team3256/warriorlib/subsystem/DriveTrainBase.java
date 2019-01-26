package frc.team3256.warriorlib.subsystem;

import frc.team3256.warriorlib.math.Rotation;

public abstract class DriveTrainBase extends SubsystemBase {
	public abstract double getLeftDistance();

	public abstract double getRightDistance();

	public abstract Rotation getRotationAngle();

	public abstract double getVelocity();

	public abstract void setVelocityClosedLoop(double left, double right);

	public abstract void resetEncoders();

	public abstract void resetGyro();

	public abstract double getAngle();
}
