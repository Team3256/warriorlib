package frc.team3256.warriorlib.hardware;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * Utility class to generate Talon SRX objects
 */
public class TalonSRXUtil {
	/**
	 * How often the Talons are updated
	 */
	private static double controlLoopPeriod = 1.0 / 200.0;

	/**
	 * Generates a basic Talon SRX object
	 *
	 * @param id ID of the Talon SRX to be generated
	 * @return generated Talon SRX object
	 */
	public static WPI_TalonSRX generateGenericTalon(int id) {
		WPI_TalonSRX talon = new WPI_TalonSRX(id);
		talon.set(ControlMode.PercentOutput, 0);
		//setup frame periods
        /*
        talon.setControlFramePeriod(ControlFrame.Control_3_General, 5);
        talon.setControlFramePeriod(ControlFrame.Control_4_Advanced, 5);
        talon.setControlFramePeriod(ControlFrame.Control_6_MotProfAddTrajPoint, 1000);
        talon.setStatusFramePeriod(StatusFrame.Status_1_General, 5, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_4_AinTempVbat, 100, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_6_Misc, 100, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_7_CommStatus, 100, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_9_MotProfBuffer, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_10_MotionMagic, 5, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 5, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 5, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_15_FirmwareApiStatus, 1000, 10);
        */
		//disable brake mode on default
		talon.setNeutralMode(NeutralMode.Brake);
		//no default reverse
		talon.setSensorPhase(false);
		talon.setInverted(false);
		//no current limit on default
		talon.enableCurrentLimit(false);
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
	public static WPI_TalonSRX generateSlaveTalon(int id, int masterId) {
		WPI_TalonSRX talon = new WPI_TalonSRX(id);
		talon.set(ControlMode.Follower, masterId);
		//we don't really need slaves to update fast, so update at 1hz
        /*talon.setControlFramePeriod(ControlFrame.Control_3_General, 1000);
        talon.setControlFramePeriod(ControlFrame.Control_4_Advanced, 1000);
        talon.setControlFramePeriod(ControlFrame.Control_6_MotProfAddTrajPoint, 1000);
        talon.setStatusFramePeriod(StatusFrame.Status_1_General, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_4_AinTempVbat, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_6_Misc, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_7_CommStatus, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_9_MotProfBuffer, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_10_MotionMagic, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_15_FirmwareApiStatus, 1000, 10); */
		//enable brake mode on default
		talon.setNeutralMode(NeutralMode.Brake);
		//no default reverse
		talon.setSensorPhase(false);
		talon.setInverted(false);
		//no current limit on default
		talon.enableCurrentLimit(false);
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

	public static void setPIDGains(WPI_TalonSRX talon, int slot, double kP, double kI, double kD, double kF) {
		talon.config_kP(slot, kP, 0);
		talon.config_kI(slot, kI, 0);
		talon.config_kD(slot, kD, 0);
		talon.config_kF(slot, kF, 0);
	}

	public static void configMagEncoder(WPI_TalonSRX talon) {
		if (talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0) != ErrorCode.OK) {
			DriverStation.reportError("DID NOT DETECT MAG ENCODER ON TALON " + talon.getDeviceID(), false);
		}
		talon.getStatusFramePeriod(StatusFrame.Status_2_Feedback0, (int) (1000 * controlLoopPeriod));
	}

	public static void setCurrentLimit(WPI_TalonSRX talon, int peakAmps, int continuousAmps, int continuousDuration) {
		talon.configPeakCurrentLimit(peakAmps, 0);
		talon.configContinuousCurrentLimit(continuousAmps, continuousDuration);
	}

	public static void setBrakeMode(WPI_TalonSRX... talons) {
		for (WPI_TalonSRX talon : talons) {
			talon.setNeutralMode(NeutralMode.Brake);
		}
	}

	public static void setCoastMode(WPI_TalonSRX... talons) {
		for (WPI_TalonSRX talon : talons) {
			talon.setNeutralMode(NeutralMode.Coast);
		}
	}

	public static void setPeakOutput(double peakFwd, double peakRev, WPI_TalonSRX... talons) {
		for (WPI_TalonSRX talon : talons) {
			talon.configPeakOutputForward(peakFwd, 0);
			talon.configPeakOutputReverse(peakRev, 0);
		}
	}

	public static void setMinOutput(double minFwd, double minRev, WPI_TalonSRX... talons) {
		for (WPI_TalonSRX talon : talons) {
			talon.configNominalOutputForward(minFwd, 0);
			talon.configNominalOutputReverse(minRev, 0);
		}
	}

	public static void setControlLoopPeriod(double controlLoopPeriod) {
		TalonSRXUtil.controlLoopPeriod = controlLoopPeriod;
	}
}
