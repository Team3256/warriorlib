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
    private static double controlLoopPeriod = 1.0/200.0;

    /**
     *
     * @param id ID of the SparkMAX to be generated
     * @param type Type of motor connected to SparkMAX (brushed or brushless)
     * @return Generated SparkMAX object
     */
    public static CANSparkMax generateGenericSparkMAX (int id, CANSparkMaxLowLevel.MotorType type){
        CANSparkMax sparkMax = new CANSparkMax(id, type);
        sparkMax.setInverted(false);
        return sparkMax;
    }

    /**
     *
     * @param id ID of the SparkMAX to be generated
     * @param type Type of motor connected to SparkMAX (brushed or brushless)
     * @param masterSpark Master SparkMAX to follow
     * @return Generated SparkMAX object
     */
    public static CANSparkMax generateSlaveSparkMAX (int id, CANSparkMaxLowLevel.MotorType type, CANSparkMax masterSpark){
        CANSparkMax sparkMax = new CANSparkMax(id, type);
        sparkMax.follow(masterSpark);
        sparkMax.setInverted(false);
        return sparkMax;
    }

    /**
     *
     * @param sparkPIDController SparkMAX PIDController object to set gains for
     * @param slot Value from 0 to 3. Each slot has its own set of gain values
     * @param kP Proportional gain value
     * @param kI Integral gain value
     * @param kD Derivative gain value
     * @param kF FeedForward gain value
     * @param kIz IZone value
     */
    public static void setPIDGains (CANPIDController sparkPIDController, int slot, double kP, double kI, double kD, double kF, double kIz) {
        sparkPIDController.setP(kP, slot);
        sparkPIDController.setI(kI, slot);
        sparkPIDController.setD(kD, slot);
        sparkPIDController.setFF(kF, slot);
        sparkPIDController.setIZone(kIz, slot);
    }

    /**
     *
     * @param limit Max current draw
     * @param sparks SparkMAX objects
     */
    public static void setCurrentLimit (int limit, CANSparkMax... sparks){
        for (CANSparkMax sparkMax: sparks) {
            sparkMax.setSmartCurrentLimit(limit);
        }
    }

    /**
     *
     * @param stallLimit Current limit at maximum torque
     * @param freeLimit Current limit when motor runs freely
     * @param sparks SparkMAX objects
     */
    public static void setCurrentLimit (int stallLimit, int freeLimit, CANSparkMax... sparks){
        for (CANSparkMax sparkMax: sparks) {
            sparkMax.setSmartCurrentLimit(stallLimit, freeLimit);
        }
    }

    /**
     *
     * @param stallLimit Current limit at maximum torque
     * @param freeLimit Current limit when motor runs freely
     * @param rpmLimit Max rpm motor is allowed to run at
     * @param sparks SparkMAX objects
     */
    public static void setCurrentLimit (int stallLimit, int freeLimit, int rpmLimit, CANSparkMax... sparks){
        for (CANSparkMax sparkMax: sparks) {
            sparkMax.setSmartCurrentLimit(stallLimit, freeLimit, rpmLimit);
        }
    }

    /**
     *
     * @param sparks SparkMAX objects
     */
    public static void setBrakeMode(CANSparkMax ... sparks) {
        for (CANSparkMax sparkMax: sparks) {
            sparkMax.setIdleMode(CANSparkMax.IdleMode.kBrake);
        }
    }

    /**
     *
     * @param sparks SparkMAX objects
     */
    public static void setCoastMode(CANSparkMax ... sparks) {
        for (CANSparkMax sparkMax: sparks) {
            sparkMax.setIdleMode(CANSparkMax.IdleMode.kCoast);
        }
    }

    /**
     *
     * @param seconds Amount of time in seconds SparkMAX takes to ramp to full throttle
     * @param sparks SparkMAX objects
     */
    public static void setRampRate (double seconds, CANSparkMax ... sparks) {
        for (CANSparkMax sparkMax: sparks) {
            sparkMax.setRampRate(seconds);
        }
    }
}
