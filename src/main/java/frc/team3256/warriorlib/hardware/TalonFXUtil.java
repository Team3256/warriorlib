package frc.team3256.warriorlib.hardware;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * Utility class to generate SPARK MAX objects
 */
public class TalonFXUtil {
    /**
     * How often the Talons are updated
     */
    private static double controlLoopPeriod = 1.0 / 200.0;

    /**
     * Generates a basic Talon FX object
     *
     * @param id ID of the Talon FX to be generated
     * @return generated Talon FX object
     */
    public static WPI_TalonFX generateGenericTalon(int id) {
        SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(false, 0, 0, 0);

        WPI_TalonFX talon = new WPI_TalonFX(id);
        talon.set(ControlMode.PercentOutput, 0);
        //disable brake mode on default
        talon.setNeutralMode(NeutralMode.Brake);
        //no default reverse
        talon.setSensorPhase(false);
        talon.setInverted(false);
        //no current limit on default
        talon.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
        //no soft limits on default
        talon.configForwardSoftLimitEnable(false, 10);
        talon.configReverseSoftLimitEnable(false, 10);
        //no hard limits on default
        talon.overrideLimitSwitchesEnable(false);
        //no ramping on default
        talon.configOpenloopRamp(0, 10);
        talon.configNominalOutputReverse(0, 10);
        talon.configNominalOutputForward(0, 10);
        talon.configPeakOutputForward(1.0, 10);
        talon.configPeakOutputReverse(-1.0, 10);
        talon.configClosedloopRamp(0, 10);
        return talon;
    }

    /**
     * Generates a "slave" Talon SRX object that follows after the given "master" Talon SRX ID
     *
     * @param id       ID of the slave Talon SRX to be generated
     * @param masterId ID of the master Talon SRX to follow after
     * @return generated Talon SRX slave object
     */
    public static WPI_TalonFX generateSlaveTalon(int id, int masterId) {
        SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(false, 0, 0, 0);

        WPI_TalonFX talon = new WPI_TalonFX(id);
        talon.set(ControlMode.Follower, masterId);
        //enable brake mode on default
        talon.setNeutralMode(NeutralMode.Brake);
        //no default reverse
        talon.setSensorPhase(false);
        talon.setInverted(false);
        //no current limit on default
        talon.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
        //no soft limits on default
        talon.configForwardSoftLimitEnable(false, 10);
        talon.configReverseSoftLimitEnable(false, 10);
        //no hard limits on default
        talon.overrideLimitSwitchesEnable(false);
        //no ramping on default
        talon.configOpenloopRamp(0, 10);
        talon.configClosedloopRamp(0, 10);
        //set minimum and maximum voltage output for talons
        talon.configNominalOutputForward(0, 10);
        talon.configNominalOutputReverse(0, 10);
        talon.configPeakOutputForward(1.0, 10);
        talon.configPeakOutputReverse(-1.0, 10);
        return talon;
    }

    public static void setPIDGains(WPI_TalonFX talon, int slot, double kP, double kI, double kD, double kF) {
        talon.config_kP(slot, kP, 0);
        talon.config_kI(slot, kI, 0);
        talon.config_kD(slot, kD, 0);
        talon.config_kF(slot, kF, 0);
    }

    public static void configMagEncoder(WPI_TalonFX talon) {
        if (talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0) != ErrorCode.OK) {
            DriverStation.reportError("DID NOT DETECT MAG ENCODER ON TALON " + talon.getDeviceID(), false);
        }
        talon.getStatusFramePeriod(StatusFrame.Status_2_Feedback0, (int) (1000 * controlLoopPeriod));
    }

    public static void setCurrentLimit(WPI_TalonFX talon, int peakAmps, int continuousAmps, int continuousDuration) {
        SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, continuousAmps, peakAmps, continuousDuration);
        talon.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
    }

    public static void setBrakeMode(WPI_TalonFX... talons) {
        for (WPI_TalonFX talon : talons) {
            talon.setNeutralMode(NeutralMode.Brake);
        }
    }

    public static void setCoastMode(WPI_TalonFX... talons) {
        for (WPI_TalonFX talon : talons) {
            talon.setNeutralMode(NeutralMode.Coast);
        }
    }

    public static void setPeakOutput(double peakFwd, double peakRev, WPI_TalonFX... talons) {
        for (WPI_TalonFX talon : talons) {
            talon.configPeakOutputForward(peakFwd, 0);
            talon.configPeakOutputReverse(peakRev, 0);
        }
    }

    public static void setMinOutput(double minFwd, double minRev, WPI_TalonFX... talons) {
        for (WPI_TalonFX talon : talons) {
            talon.configNominalOutputForward(minFwd, 0);
            talon.configNominalOutputReverse(minRev, 0);
        }
    }

    public static void setControlLoopPeriod(double controlLoopPeriod) {
        TalonFXUtil.controlLoopPeriod = controlLoopPeriod;
    }
}
