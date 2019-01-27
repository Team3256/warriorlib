package frc.team3256.warriorlib.control;

public class TeleopDriveController {
	private static final double controllerDeadband = 0.05;
	private static final double kQuickStopThreshold = 0.2;
	private static final double kQuickStopAlpha = 0.1;
	private static final double kQuickStopScalar = 2.0;
	private static final double kQuickTurnDeltaLimit = 2.0 / 1000.0 / 12.0 * 20.0;
	private static double quickStopAccumulator = 0.0;
	private static double prevTurn = 0.0;

	//Tank Drive
	public static DrivePower tankDrive(double leftPower, double rightPower) {
		leftPower = handleDeadband(leftPower, controllerDeadband);
		rightPower = handleDeadband(rightPower, controllerDeadband);
		return new DrivePower(leftPower, rightPower);
	}

	//Arcade Drive
	public static DrivePower arcadeDrive(double throttle, double turn) {
		if (Math.abs(throttle) <= controllerDeadband) {
			throttle = 0;
		}
		if (Math.abs(turn) <= controllerDeadband) {
			turn = 0;
		}
		double left = throttle + turn;
		double right = throttle - turn;

		left = clamp(left);
		right = clamp(right);

		return new DrivePower(left, right);
	}

	//Curvature or Cheesy Drive
	public static DrivePower curvatureDrive(double throttle, double turn, boolean quickTurn, boolean highGear) {
		turn = handleDeadband(turn, controllerDeadband);

		double angularPower, overPower;

		if (quickTurn) {
			highGear = false;
			if (Math.abs(throttle) < kQuickStopThreshold) {
				quickStopAccumulator = (1 - kQuickStopAlpha) * quickStopAccumulator + kQuickStopAlpha * clamp(turn) * kQuickStopScalar;
			}
			overPower = 1.0;
			angularPower = turn;
			if (Math.abs(turn - prevTurn) > kQuickTurnDeltaLimit) {
				//System.out.println("TURN: " + turn);
				//System.out.println("PREVIOUS TURN: " + prevTurn);
				if (turn > 0) {
					angularPower = prevTurn + kQuickTurnDeltaLimit;
					//System.out.println("ANGULAR POWER: " + angularPower);
				} else
					angularPower = prevTurn - kQuickTurnDeltaLimit;
			}
		} else {
			overPower = 0.0;
			angularPower = Math.abs(throttle) * turn - quickStopAccumulator;
			if (quickStopAccumulator > 1) {
				quickStopAccumulator -= 1;
			} else if (quickStopAccumulator < -1) {
				quickStopAccumulator += 1;
			} else {
				quickStopAccumulator = 0.0;
			}
		}
		prevTurn = turn;
		double left = throttle + angularPower;
		double right = throttle - angularPower;
		if (left > 1.0) {
			right -= overPower * (left - 1.0);
			left = 1.0;
		} else if (right > 1.0) {
			left -= overPower * (right - 1.0);
			right = 1.0;
		} else if (left < -1.0) {
			right += overPower * (-1.0 - left);
			left = -1.0;
		} else if (right < -1.0) {
			left += overPower * (-1.0 - right);
			right = -1.0;
		}
		return new DrivePower(left, right, highGear);
	}

	//Clamps the input value to [-1.0, 1.0]
	public static double clamp(double val) {
		return Math.max(Math.min(val, 1.0), -1.0);
	}

	//All values under a deadband value are set to 0, and the values above it are re-scaled accordingly
	public static double handleDeadband(double val, double deadband) {
		if (Math.abs(val) > deadband) {
			if (val > 0) {
				return (val - deadband) / (1.0 - deadband);
			} else
				return (val + deadband) / (1.0 - deadband);
		}
		return 0.0;
	}
}
