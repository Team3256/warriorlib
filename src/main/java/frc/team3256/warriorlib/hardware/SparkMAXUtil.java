package frc.team3256.warriorlib.hardware;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

/**
 * Utility class to generate SPARK MAX objects
 */
public class SparkMAXUtil {
    /**
     * How often the Spark's are updated
     */
    private static double controlLoopPeriod = 1.0/200.0;

    public static CANSparkMax generateGenericSparkMAX (int id, CANSparkMaxLowLevel.MotorType type){
        CANSparkMax sparkMax = new CANSparkMax(id, type);
        sparkMax.setInverted(false);
        return sparkMax;
    }

    public static CANSparkMax generateSlaveSparkMAX (int id, CANSparkMaxLowLevel.MotorType type, CANSparkMax masterSpark){
        CANSparkMax sparkMax = new CANSparkMax(id, type);
        sparkMax.follow(masterSpark);
        sparkMax.setInverted(false);
        return sparkMax;
    }

    public static void setCurrentLimit (int limit, CANSparkMax... sparks){
        for (CANSparkMax sparkMax: sparks) {
            sparkMax.setSmartCurrentLimit(limit);
        }
    }

    public static void setCurrentLimit (int stallLimit, int freeLimit, CANSparkMax... sparks){
        for (CANSparkMax sparkMax: sparks) {
            sparkMax.setSmartCurrentLimit(stallLimit, freeLimit);
        }
    }

    public static void setCurrentLimit (int stallLimit, int freeLimit, int rpmLimit, CANSparkMax... sparks){
        for (CANSparkMax sparkMax: sparks) {
            sparkMax.setSmartCurrentLimit(stallLimit, freeLimit, rpmLimit);
        }
    }

    public static void setBrakeMode(CANSparkMax ... sparks) {
        for (CANSparkMax sparkMax: sparks) {
            sparkMax.setIdleMode(CANSparkMax.IdleMode.kBrake);
        }
    }

    public static void setCoastMode(CANSparkMax ... sparks) {
        for (CANSparkMax sparkMax: sparks) {
            sparkMax.setIdleMode(CANSparkMax.IdleMode.kCoast);
        }
    }

    public static void setRampRate (double seconds, CANSparkMax ... sparks) {
        for (CANSparkMax sparkMax: sparks) {
            sparkMax.setRampRate(seconds);
        }
    }
}
