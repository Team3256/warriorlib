package frc.team3256.warriorlib.subsystem;

import frc.team3256.warriorlib.loop.Loop;
import frc.team3256.warriorlib.math.Rotation;

public abstract class DriveTrainBase extends SubsystemBase implements Loop {
	public abstract double getLeftDistance();

	public abstract double getRightDistance();

	public abstract Rotation getRotationAngle();
}
