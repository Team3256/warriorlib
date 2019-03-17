package frc.team3256.warriorlib.auto.purepursuit;

import frc.team3256.warriorlib.auto.action.Action;
import frc.team3256.warriorlib.control.DrivePower;
import frc.team3256.warriorlib.subsystem.DriveTrainBase;

public class PurePursuitAction implements Action {
    private PurePursuitTracker purePursuitTracker = PurePursuitTracker.getInstance();
    private DriveTrainBase driveTrain = DriveTrainBase.getDriveTrain();
    private PoseEstimator poseEstimator = PoseEstimator.getInstance();
    private int pathIndex;

    public PurePursuitAction(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    @Override
    public boolean isFinished() {
        return purePursuitTracker.isDone();
    }

    @Override
    public void update() {
        DrivePower drivePower = purePursuitTracker.update(poseEstimator.getPose(), driveTrain.getLeftVelocity(), driveTrain.getRightVelocity(), driveTrain.getRotationAngle().radians());
        driveTrain.setVelocityClosedLoop(drivePower.getLeftY(), drivePower.getRightX());
    }

    @Override
    public void done() {
        driveTrain.setVelocityClosedLoop(0, 0);
        purePursuitTracker.reset();
    }

    @Override
    public void start() {
        purePursuitTracker.reset();
        purePursuitTracker.setPath(pathIndex);
    }

    public void setPathIndex(Integer pathIndex) {
        this.pathIndex = pathIndex;
    }
}
