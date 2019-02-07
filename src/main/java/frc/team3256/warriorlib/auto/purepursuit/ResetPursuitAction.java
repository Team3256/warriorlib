package frc.team3256.warriorlib.auto.purepursuit;

import frc.team3256.warriorlib.auto.action.RunOnceAction;
import frc.team3256.warriorlib.subsystem.DriveTrainBase;

/**
 * Resets drive train and pose estimator
 */
public class ResetPursuitAction extends RunOnceAction {
    @Override
    public void runOnce() {
        DriveTrainBase.getDriveTrain().resetEncoders();
        DriveTrainBase.getDriveTrain().resetGyro();
        PoseEstimator.getInstance().reset();
    }
}
