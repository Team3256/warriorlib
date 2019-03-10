package frc.team3256.warriorlib.auto.purepursuit;

import frc.team3256.warriorlib.control.DrivePower;
import frc.team3256.warriorlib.math.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Given a path and a couple parameters, this class will handle much of the processing of the Pure Pursuit algorithm
 */
public class PurePursuitTracker {
	private static PurePursuitTracker instance;
	private int lastClosestPoint;
    private List<Path> paths = new ArrayList<>();
	private Path path;
	private double lookaheadDistance;
	private double robotTrack = 0;
	private double feedbackMultiplier = 0;

	private PurePursuitTracker() {
		reset();
	}

	public static PurePursuitTracker getInstance() {
		return instance == null ? instance = new PurePursuitTracker() : instance;
	}

	/**
	 * Sets the path to be tracked
	 *
	 * @param lookaheadDistance lookahead distance (ideally between 15-24 inches)
	 */
    public void setPaths(List<Path> paths, double lookaheadDistance) {
		reset();
		this.paths = paths;
		this.lookaheadDistance = lookaheadDistance;
	}

	/**
	 * Sets robot track
	 * @param robotTrack width of robot measured from center of each side of drivetrain
	 */
	public void setRobotTrack(double robotTrack) {
		this.robotTrack = robotTrack;
	}

	/**
	 * Sets the feedback multiplier (proportional feedback constant) for velocities
	 *
	 * @param feedbackMultiplier feedback multiplier
	 */
	public void setFeedbackMultiplier(double feedbackMultiplier) {
		this.feedbackMultiplier = feedbackMultiplier;
	}

	public void reset() {
		this.lastClosestPoint = 0;
	}

	/**
	 * Updates the tracker and returns left and right velocities
	 *
	 * @param currPose current position of robot
	 * @param currLeftVel current left velocity of robot
	 * @param currRightVel current right velocity of robot
	 * @param heading current angle of robot
	 * @return left and right velocities to be sent to drivetrain
	 */
	public DrivePower update(Vector currPose, double currLeftVel, double currRightVel, double heading) {
		boolean onLastSegment = false;
		int closestPointIndex = getClosestPointIndex(currPose);
		Vector lookaheadPoint = new Vector(0, 0);
		ArrayList<Vector> robotPath = path.getRobotPath();
		for (int i = closestPointIndex + 1; i < robotPath.size(); i++) {
			Vector startPoint = robotPath.get(i - 1);
			Vector endPoint = robotPath.get(i);
			if (i == robotPath.size() - 1)
				onLastSegment = true;
			Optional<Vector> lookaheadPtOptional = calculateLookAheadPoint(startPoint, endPoint, currPose, lookaheadDistance, onLastSegment);
			if (lookaheadPtOptional.isPresent()) {
				lookaheadPoint = lookaheadPtOptional.get();
				break;
			}
		}

		double curvature = path.calculateCurvatureLookAheadArc(currPose, heading, lookaheadPoint, lookaheadDistance);
		double leftTargetVel = calculateLeftTargetVelocity(robotPath.get(getClosestPointIndex(currPose)).getVelocity(), curvature);
		double rightTargetVel = calculateRightTargetVelocity(robotPath.get(getClosestPointIndex(currPose)).getVelocity(), curvature);

		if (!path.isForward()) {
            leftTargetVel = -leftTargetVel;
            rightTargetVel = -rightTargetVel;
        }

		double leftFeedback = feedbackMultiplier * (leftTargetVel - currLeftVel);
		double rightFeedback = feedbackMultiplier * (rightTargetVel - currRightVel);
        /*
        System.out.println("leftTargetVel: " + leftTargetVel);
        System.out.println("rightTargetVel: " + rightTargetVel);

        double rightFF = calculateFeedForward(rightTargetVel, currVel, true);
        double leftFF = calculateFeedForward(leftTargetVel, currVel, false);
        double rightFB = calculateFeedback(rightTargetVel, currVel);
        double leftFB = calculateFeedback(leftTargetVel, currVel);
        */
        double leftPower = leftTargetVel + leftFeedback;
        double rightPower = rightTargetVel + rightFeedback;

        return new DrivePower(leftPower, rightPower, true);
	}

    /*
    //calculates the feedForward and the feedBack that will get passed through to the motors

    private double calculateFeedForward(double targetVel, double currVel, boolean right) {
        double targetAcc = (targetVel - currVel)/(Constants.loopTime);
		double maxAccel = Constants.maxAccel;
		targetAcc = Range.clip(targetAcc, -maxAccel, maxAccel);
        double rateLimitedVel = rateLimiter(targetVel, maxAccel, right);
        return (Constants.kV * rateLimitedVel) + (Constants.kA * targetAcc);
    }

    private double calculateFeedback(double targetVel, double currVel) {
        return Constants.kP * (targetVel - currVel);
    }
    */

	//calculates the left and right target velocities given the targetRobotVelocity

	/**
	 * Calculates the left target velocity given target overall velocity
	 *
	 * @param targetRobotVelocity target overall robot velocity
	 * @param curvature           curvature of path at current point
	 * @return left target velocity
	 */
	private double calculateLeftTargetVelocity(double targetRobotVelocity, double curvature) {
		return targetRobotVelocity * ((2 + (robotTrack * curvature))) / 2;
	}

	/**
	 * Calculates the right target velocity given target overall velocity
	 * @param targetRobotVelocity target overall robot velocity
	 * @param curvature curvature of path at current point
	 * @return right target velocity
	 */
	private double calculateRightTargetVelocity(double targetRobotVelocity, double curvature) {
		//System.out.println("target robot velocity: " + targetRobotVelocity);
		//System.out.println("curvature " + curvature);
		return targetRobotVelocity * ((2 - (robotTrack * curvature))) / 2;
	}

    /*
    //limits the rate of change of a value given a maxRate parameter

    private double rateLimiter(double input, double maxRate, boolean right) {
        double maxChange = Constants.loopTime * maxRate;
        if (right) {
            rightOutput += Range.clip(input - prevRightOutput, -maxChange, maxChange);
            prevRightOutput = rightOutput;
            return rightOutput;
        }
        else {
            leftOutput += Range.clip(input - prevLeftOutput, -maxChange, maxChange);
            prevLeftOutput = leftOutput;
            return leftOutput;
        }
    }
    */

	//calculates the intersection point between a point and a circle

	/**
	 * Calculates the intersection t-value between a line and a circle, using quadratic formula
	 *
	 * @param startPoint        start of line
	 * @param endPoint          end of line
	 * @param currPos           current robot position (or center of the circle)
	 * @param lookaheadDistance lookahead distance along the path (or radius of the circle)
	 * @return intersection t-value (scaled from 0-1, representing proportionally how far along the segment the intersection is)
	 */
	private Optional<Double> calcIntersectionTVal(Vector startPoint, Vector endPoint, Vector currPos, double lookaheadDistance) {

		Vector d = Vector.sub(endPoint, startPoint);
		Vector f = Vector.sub(startPoint, currPos);

		double a = d.dot(d);
		double b = 2 * f.dot(d);
		double c = f.dot(f) - Math.pow(lookaheadDistance, 2);
		double discriminant = Math.pow(b, 2) - (4 * a * c);

		if (discriminant < 0) {
			return Optional.empty();
		} else {
			discriminant = Math.sqrt(discriminant);
			double t1 = (-b - discriminant) / (2 * a);
			double t2 = (-b + discriminant) / (2 * a);

			if (t1 >= 0 && t1 <= 1) {
				return Optional.of(t1);
			}
			if (t2 >= 0 && t2 <= 1) {
				return Optional.of(t2);
			}

		}

		return Optional.empty();
	}

	//uses the calculated intersection point to get a Vector value on the path that is the lookahead point

	/**
	 * Uses the calculated intersection t-value to get a point on the path of where to look ahead
	 *
	 * @param startPoint        starting point
	 * @param endPoint          ending point
	 * @param currPos           current robot position
	 * @param lookaheadDistance lookahead distance along the path
	 * @param onLastSegment     whether or not we are on the last path segment
	 * @return lookahead point
	 */
	private Optional<Vector> calculateLookAheadPoint(Vector startPoint, Vector endPoint, Vector currPos, double lookaheadDistance, boolean onLastSegment) {
		Optional<Double> tIntersect = calcIntersectionTVal(startPoint, endPoint, currPos, lookaheadDistance);
		if (!tIntersect.isPresent() && onLastSegment) {
			return Optional.of(path.getRobotPath().get(path.getRobotPath().size() - 1));
		} else if (!tIntersect.isPresent()) {
			return Optional.empty();
		} else {
			Vector intersectVector = Vector.sub(endPoint, startPoint, null);
			Vector vectorSegment = Vector.mult(intersectVector, tIntersect.get());
			Vector point = Vector.add(startPoint, vectorSegment);
			return Optional.of(point);
		}
	}

	/**
	 * Calculates the index of the point on the path that is closest to the robot. Also avoids going backwards
	 * @param currPos current robot position
	 * @return index of closest point on path
	 */
	private int getClosestPointIndex(Vector currPos) {
		double shortestDistance = Double.MAX_VALUE;
		int closestPoint = 0;
		ArrayList<Vector> robotPath = path.getRobotPath();
		for (int i = lastClosestPoint; i < robotPath.size(); i++) {
			if (Vector.dist(robotPath.get(i), currPos) < shortestDistance) {
				closestPoint = i;
				shortestDistance = Vector.dist(robotPath.get(i), currPos);
			}
		}
		lastClosestPoint = closestPoint;
		return closestPoint;
	}

	/**
	 * Tells PurePursuitAction when we are done, indicated by the closest point being the last point of the path
	 * @return whether or not we should finish
	 */
	public boolean isDone() {
		return getClosestPointIndex(PoseEstimator.getInstance().getPose()) == path.getRobotPath().size() - 1;
	}

    public void setPath(Integer pathIndex) {
		this.path = paths.get(pathIndex);
	}

	public Path getPath() {
		return path;
	}
}
