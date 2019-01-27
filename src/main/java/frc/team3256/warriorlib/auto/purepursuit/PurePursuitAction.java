package frc.team3256.warriorlib.auto.purepursuit;

import frc.team3256.warriorlib.auto.action.Action;
import frc.team3256.warriorlib.control.DrivePower;
import frc.team3256.warriorlib.subsystem.DriveTrainBase;

public class PurePursuitAction implements Action {
    private PurePursuitTracker purePursuitTracker = PurePursuitTracker.getInstance();
    private DriveTrainBase driveTrain = DriveTrainBase.getDriveTrain();
    private PoseEstimator poseEstimator = PoseEstimator.getInstance();

    @Override
    public boolean isFinished() {
        return purePursuitTracker.isDone();
    }

    @Override
    public void update() {
        DrivePower drivePower = purePursuitTracker.update(poseEstimator.getPose(), driveTrain.getLeftVelocity(), driveTrain.getRightVelocity(), driveTrain.getRotationAngle().radians());
        driveTrain.setVelocityClosedLoop(drivePower.getLeft(), drivePower.getRight());
    }

    @Override
    public void done() {
        driveTrain.setVelocityClosedLoop(0, 0);
        driveTrain.resetEncoders();
        driveTrain.resetGyro();
        poseEstimator.reset();
        purePursuitTracker.reset();
    }

    @Override
    public void start() {
        driveTrain.resetEncoders();
        driveTrain.resetGyro();
        poseEstimator.reset();
        purePursuitTracker.reset();
    }
}
