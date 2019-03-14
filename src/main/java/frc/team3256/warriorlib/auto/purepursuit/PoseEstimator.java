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
	private DriveTrainBase driveTrain = DriveTrainBase.getDriveTrain();

	private static PoseEstimator instance;

	private PoseEstimator() {
		reset();
	}

	public static PoseEstimator getInstance() {
		return instance == null ? instance = new PoseEstimator() : instance;
	}

	public Vector getPose() {
		return new Vector(pose.getTranslation().getX(), pose.getTranslation().getY());
	}

	public Twist getVelocity() {
		return velocity;
	}

	public void resetPosition() {
		velocity = new Twist();
		pose = new RigidTransform(new Translation(), pose.getRotation());
		prevPose = new RigidTransform(new Translation(), pose.getRotation());
		prevLeftDist = 0;
		prevRightDist = 0;
	}

	public void offsetPoseAngle(double angle) {
		pose = new RigidTransform(pose.getTranslation(), pose.getRotation().rotate(Rotation.fromDegrees(angle)));
		prevPose = new RigidTransform(pose.getTranslation(), pose.getRotation().rotate(Rotation.fromDegrees(angle)));
	}

	/**
	 * MUST BE CALLED IMMEDIATELY AFTER RESETTING DRIVETRAIN/GYRO!
	 */
	public void reset() {
		velocity = new Twist();
		pose = new RigidTransform();
		prevPose = new RigidTransform();
		prevLeftDist = 0;
		prevRightDist = 0;
	}

	@Override
	public void init(double timestamp) {
		reset();
	}

	@Override
	public void update(double timestamp) {
		double leftDist = driveTrain.getLeftDistance();
		double rightDist = driveTrain.getRightDistance();
		double deltaLeftDist = leftDist - prevLeftDist;
		double deltaRightDist = rightDist - prevRightDist;
		Rotation deltaHeading = prevPose.getRotation().inverse().rotate(driveTrain.getRotationAngle());
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
