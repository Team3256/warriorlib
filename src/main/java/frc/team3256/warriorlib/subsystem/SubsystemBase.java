package frc.team3256.warriorlib.subsystem;

import frc.team3256.warriorlib.loop.Loop;

/**
 * Subsystem base class, for implementation of dashboard output and zeroing of sensors
 */
public abstract class SubsystemBase implements Loop {
	public abstract void outputToDashboard();

	public abstract void zeroSensors();
}
