package frc.team3256.warriorlib.hardware;

import frc.team3256.warriorlib.loop.Loop;

/**
 * This is a class to periodically re-calibrate the gyroscope (when the robot is disabled) using a
 * {@link Loop}.
 */
public class ADXRS453_Calibrator implements Loop {
	private ADXRS453_Gyro gyro;
	private double previousTime = 0.0;

	public ADXRS453_Calibrator(ADXRS453_Gyro gyro) {
		this.gyro = gyro;
	}

	@Override
	public void init(double timestamp) {
	}

	/**
	 * Calibrates the gyro if time since last calibration exceeds calibration sample time
	 *
	 * @param timestamp time of current execution
	 */
	@Override
	public void update(double timestamp) {
		if (timestamp - previousTime > ADXRS453_Gyro.kCalibrationSampleTime) {
			gyro.endCalibrate();
			previousTime = timestamp;
			gyro.startCalibrate();
		}
	}

	@Override
	public void end(double timestamp) {
		gyro.cancelCalibrate();
	}
}
