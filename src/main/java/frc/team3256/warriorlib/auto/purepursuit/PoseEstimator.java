package frc.team3256.warriorlib.auto.purepursuit;

import frc.team3256.warriorlib.loop.Loop;
import frc.team3256.warriorlib.math.*;
import frc.team3256.warriorlib.subsystem.DriveTrainBase;

public class PoseEstimator implements Loop {
	private Twist velocity;
	private RigidTransform pose;
	private RigidTransform prevPose;
	private double prevLeftDist = 0;
	private double prevRightDist = 0;
	private DriveTrainBase driveTrainBase;

	public PoseEstimator(DriveTrainBase driveTrainBase) {
		this.driveTrainBase = driveTrainBase;
		reset(new RigidTransform());
	}

	public Vector getPose() {
		return new Vector(pose.getTranslation().getX(), pose.getTranslation().getY());
	}

	public Twist getVelocity() {
		return velocity;
	}

	public void reset(RigidTransform startingPose) {
		velocity = new Twist();
		pose = startingPose;
		prevPose = new RigidTransform();
	}

	@Override
	public void init(double timestamp) {
		prevLeftDist = driveTrainBase.getLeftDistance();
		prevRightDist = driveTrainBase.getRightDistance();
		reset(new RigidTransform());
	}

	@Override
	public void update(double timestamp) {
		double leftDist = driveTrainBase.getLeftDistance();
		double rightDist = driveTrainBase.getRightDistance();
		double deltaLeftDist = leftDist - prevLeftDist;
		double deltaRightDist = rightDist - prevRightDist;
		Rotation deltaHeading = prevPose.getRotation().inverse().rotate(driveTrainBase.getRotationAngle());
		//Use encoders + gyro to determine our velocity
		velocity = Kinematics.forwardKinematics(deltaLeftDist, deltaRightDist, deltaHeading.radians());
		//use velocity to determine our pose
		pose = Kinematics.integrateForwardKinematics(prevPose, velocity);
		//update for next iteration
		prevLeftDist = leftDist;
		prevRightDist = rightDist;
		prevPose = pose;
	}

	@Override
	public void end(double timestamp) {

	}
}
