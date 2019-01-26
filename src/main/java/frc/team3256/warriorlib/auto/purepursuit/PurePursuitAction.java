package frc.team3256.warriorlib.auto.purepursuit;

import frc.team3256.warriorlib.auto.action.Action;
import frc.team3256.warriorlib.control.DrivePower;
import frc.team3256.warriorlib.subsystem.DriveTrainBase;

public class PurePursuitAction implements Action {
    private PurePursuitTracker purePursuitTracker = PurePursuitTracker.getInstance();
    private static DriveTrainBase driveTrainBase;
    private PoseEstimator poseEstimator = PoseEstimator.getInstance();

    public static void setDriveTrainBase(DriveTrainBase driveTrainBase) {
        PurePursuitAction.driveTrainBase = driveTrainBase;
    }

    @Override
    public boolean isFinished() {
        return purePursuitTracker.isDone();
    }

    @Override
    public void update() {
        DrivePower drivePower = purePursuitTracker.update(poseEstimator.getPose(), driveTrainBase.getVelocity(), driveTrainBase.getRotationAngle().radians());
        driveTrainBase.setVelocityClosedLoop(drivePower.getLeft(), drivePower.getRight());
    }

    @Override
    public void done() {
        driveTrainBase.setVelocityClosedLoop(0, 0);
        driveTrainBase.resetEncoders();
        driveTrainBase.resetGyro();
        poseEstimator.reset();
        purePursuitTracker.reset();
    }

    @Override
    public void start() {
        driveTrainBase.resetEncoders();
        driveTrainBase.resetGyro();
        poseEstimator.reset();
        purePursuitTracker.reset();
    }
}
