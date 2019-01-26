package frc.team3256.warriorlib.math;

public class Kinematics {
	/**
	 * Forward kinematics:
	 * Starting in the pose (x, y, theta) at time t, determine the pose at time t + delta_t (x', y', theta')
	 * given the robot's left and right wheel velocities.
	 * Note: we don't take time into account here because we are assuming the control loop is running at a constant period.
	 *
	 * @param currentPose current pose of the robot
	 * @param deltaPos    the change in (x, y, theta) of the robot over the past iteration
	 * @return the new pose of the robot
	 */
	public static RigidTransform integrateForwardKinematics(RigidTransform currentPose, Twist deltaPos) {
		return currentPose.transform(RigidTransform.exp(deltaPos));
	}

	public static Twist forwardKinematics(double leftDelta, double rightDelta, double rotationDelta) {
		double dx = (leftDelta + rightDelta) / 2.0;
		//dy is 0, because we don't move sideways
		return new Twist(dx, 0.0, rotationDelta);
	}
}
