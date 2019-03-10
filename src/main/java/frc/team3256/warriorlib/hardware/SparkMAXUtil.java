package frc.team3256.warriorlib.hardware;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

/**
 * Utility class to generate SPARK MAX objects
 */
public class SparkMAXUtil {
	/**
	 * How often the Sparks are updated
	 */
	private static double controlLoopPeriod = 1.0 / 200.0;

	/**
	 * Creates a basic Spark MAX object
	 *
	 * @param id   ID of the SparkMAX to be generated
	 * @param type Type of motor connected to SparkMAX (brushed or brushless)
	 * @return Generated SparkMAX object
	 */
	public static CANSparkMax generateGenericSparkMAX(int id, CANSparkMaxLowLevel.MotorType type) {
		CANSparkMax sparkMax = new CANSparkMax(id, type);
		sparkMax.setInverted(false);
		return sparkMax;
	}

	/**
	 * Generates a "slave" Spark MAX object that follows after the given "master" Spark MAX ID
	 *
	 * @param id          ID of the SparkMAX to be generated
	 * @param type        Type of motor connected to SparkMAX (brushed or brushless)
	 * @param masterSpark Master SparkMAX to follow
	 * @return Generated SparkMAX object
	 */
	public static CANSparkMax generateSlaveSparkMAX(int id, CANSparkMaxLowLevel.MotorType type, CANSparkMax masterSpark) {
		CANSparkMax sparkMax = new CANSparkMax(id, type);
		sparkMax.follow(masterSpark);
		sparkMax.setInverted(false);
		return sparkMax;
	}

	/**
	 * Sets the PID Gains for the specified PIDController
	 *
	 * @param sparkPIDController SparkMAX PIDController object to set gains for
	 * @param slot               Value from 0 to 3. Each slot has its own set of gain values
	 * @param kP                 Proportional gain value
	 * @param kI                 Integral gain value
	 * @param kD                 Derivative gain value
	 * @param kF                 FeedForward gain value
	 * @param kIz                IZone value
	 */
	public static void setPIDGains(CANPIDController sparkPIDController, int slot, double kP, double kI, double kD, double kF, double kIz) {
		sparkPIDController.setP(kP, slot);
		sparkPIDController.setI(kI, slot);
		sparkPIDController.setD(kD, slot);
		sparkPIDController.setFF(kF, slot);
		sparkPIDController.setIZone(kIz, slot);
	}

    public static void setSmartMotionParams(CANPIDController sparkPIDController, double minVel, double maxVel, double maxAccel, double allowedErr, int slot) {
        sparkPIDController.setSmartMotionAllowedClosedLoopError(allowedErr, slot);
        sparkPIDController.setSmartMotionMinOutputVelocity(minVel, slot);
        sparkPIDController.setSmartMotionMaxVelocity(maxVel, slot);
        sparkPIDController.setSmartMotionMaxAccel(maxAccel, slot);
    }

	/**
	 * Limits the current draw
	 *
	 * @param limit  Max current draw
	 * @param sparks SparkMAX objects
	 */
	public static void setCurrentLimit(int limit, CANSparkMax... sparks) {
		for (CANSparkMax sparkMax : sparks) {
			sparkMax.setSmartCurrentLimit(limit);
		}
	}

	/**
	 * Limits the current draw for when the motor is stalled and free
	 *
	 * @param stallLimit Current limit at maximum torque
	 * @param freeLimit  Current limit when motor runs freely
	 * @param sparks     SparkMAX objects
	 */
	public static void setCurrentLimit(int stallLimit, int freeLimit, CANSparkMax... sparks) {
		for (CANSparkMax sparkMax : sparks) {
			sparkMax.setSmartCurrentLimit(stallLimit, freeLimit);
		}
	}

	/**
	 * Limits the current draw for when the motor is stalled and free
	 * Limits the max rpm of the motor
	 *
	 * @param stallLimit Current limit at maximum torque
	 * @param freeLimit  Current limit when motor runs freely
	 * @param rpmLimit   Max rpm motor is allowed to run at
	 * @param sparks     SparkMAX objects
	 */
	public static void setCurrentLimit(int stallLimit, int freeLimit, int rpmLimit, CANSparkMax... sparks) {
		for (CANSparkMax sparkMax : sparks) {
			sparkMax.setSmartCurrentLimit(stallLimit, freeLimit, rpmLimit);
		}
	}

	/**
	 * Sets the motors to brake
	 *
	 * @param sparks SparkMAX objects
	 */
	public static void setBrakeMode(CANSparkMax... sparks) {
		for (CANSparkMax sparkMax : sparks) {
			sparkMax.setIdleMode(CANSparkMax.IdleMode.kBrake);
		}
	}

	/**
	 * Sets the motors to coast
	 *
	 * @param sparks SparkMAX objects
	 */
	public static void setCoastMode(CANSparkMax... sparks) {
		for (CANSparkMax sparkMax : sparks) {
			sparkMax.setIdleMode(CANSparkMax.IdleMode.kCoast);
		}
	}

	/**
	 * Sets the amount of seconds it takes to go from 0 power to full throttle (closed loop)
	 *
	 * @param seconds Amount of time in seconds SparkMAX takes to ramp to full throttle
	 * @param sparks  SparkMAX objects
	 */
	public static void setClosedLoopRampRate(double seconds, CANSparkMax... sparks) {
		for (CANSparkMax sparkMax : sparks) {
			sparkMax.setClosedLoopRampRate(seconds);
		}
	}

	/**
	 * Sets the amount of seconds it takes to go from 0 power to full throttle (open loop)
	 *
	 * @param seconds Amount of time in seconds SparkMAX takes to ramp to full throttle
	 * @param sparks  SparkMAX objects
	 */
	public static void setOpenLoopRampRate(double seconds, CANSparkMax... sparks) {
		for (CANSparkMax sparkMax : sparks) {
			sparkMax.setOpenLoopRampRate(seconds);
		}
	}
}
