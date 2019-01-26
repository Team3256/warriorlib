package frc.team3256.warriorlib.auto.purepursuit;

import frc.team3256.warriorlib.control.DrivePower;
import frc.team3256.warriorlib.math.Vector;

import java.util.ArrayList;
import java.util.Optional;

public class PurePursuitTracker {
    private static PurePursuitTracker instance;
    private int lastClosestPt;
    private Path p;
    private double lookaheadDistance;
    private double robotTrack;

    private PurePursuitTracker() {
        reset();
    }

    public static PurePursuitTracker getInstance() {
        return instance == null ? instance = new PurePursuitTracker() : instance;
    }

    public void setPath(Path p, double lookaheadDistance) {
        this.p = p;
        this.lookaheadDistance = lookaheadDistance;
    }

    public void setRobotTrack(double robotTrack) {
        this.robotTrack = robotTrack;
    }

    public void reset() {
        this.lastClosestPt = 0;
    }

    //updates the motor controller values

    public DrivePower update(Vector currPose, double currVel, double heading) {
        boolean onLastSegment = false;
        int closestPointIndex = getClosestPointIndex(currPose);
        Vector lookaheadPt = new Vector(0, 0);
        ArrayList<Vector> robotPath = p.getRobotPath();
        for (int i = closestPointIndex + 1; i < robotPath.size(); i++) {
            Vector startPt = robotPath.get(i - 1);
            Vector endPt = robotPath.get(i);
            if (i == robotPath.size() - 1)
                onLastSegment = true;
            Optional<Vector> lookaheadPtOptional = calculateVectorLookAheadPoint(startPt, endPt, currPose, lookaheadDistance, onLastSegment);
            if (lookaheadPtOptional.isPresent()) {
                lookaheadPt = lookaheadPtOptional.get();
                break;
            }
        }
        double curvature = p.calculateCurvatureLookAheadArc(currPose, heading, lookaheadPt, lookaheadDistance);
        double leftTargetVel = calculateLeftTargetVelocity(robotPath.get(getClosestPointIndex(currPose)).getVelocity(), curvature);
        double rightTargetVel = calculateRightTargetVelocity(robotPath.get(getClosestPointIndex(currPose)).getVelocity(), curvature);
        /*
        System.out.println("leftTargetVel: " + leftTargetVel);
        System.out.println("rightTargetVel: " + rightTargetVel);

        double rightFF = calculateFeedForward(rightTargetVel, currVel, true);
        double leftFF = calculateFeedForward(leftTargetVel, currVel, false);
        double rightFB = calculateFeedback(rightTargetVel, currVel);
        double leftFB = calculateFeedback(leftTargetVel, currVel);
        */

        return new DrivePower(leftTargetVel, rightTargetVel, true);
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

    private double calculateLeftTargetVelocity(double targetRobotVelocity, double curvature) { //target velocity is from closest point on path
        return targetRobotVelocity * ((2 + (robotTrack * curvature))) / 2;
    }

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

    private Optional<Double> calcIntersectionPoint(Vector startPoint, Vector endPoint, Vector currPos, double lookaheadDistance) {

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

    private Optional<Vector> calculateVectorLookAheadPoint(Vector startPoint, Vector endPoint, Vector currPos, double lookaheadDistance, boolean onLastSegment) {
        Optional<Double> tIntersect = calcIntersectionPoint(startPoint, endPoint, currPos, lookaheadDistance);
        if (tIntersect.isEmpty() && onLastSegment) {
            return Optional.of(p.getRobotPath().get(p.getRobotPath().size() - 1));
        } else if (tIntersect.isEmpty()) {
            return Optional.empty();
        } else {
            Vector intersectVector = Vector.sub(endPoint, startPoint, null);
            Vector vectorSegment = Vector.mult(intersectVector, tIntersect.get());
            Vector point = Vector.add(startPoint, vectorSegment);
            return Optional.of(point);
        }
    }

    //gets the index of the closest point

    private int getClosestPointIndex(Vector currPos) {
        double shortestDistance = Double.MAX_VALUE;
        int closestPoint = 0;
        ArrayList<Vector> robotPath = p.getRobotPath();
        for (int i = lastClosestPt; i < robotPath.size(); i++) {
            if (Vector.dist(robotPath.get(i), currPos) < shortestDistance) {
                closestPoint = i;
                shortestDistance = Vector.dist(robotPath.get(i), currPos);
            }
        }
        lastClosestPt = closestPoint;
        return closestPoint;
    }

    /*
    public int getClosestPoint(Vector currentPose) {
        double leastDistance = Double.MAX_VALUE;
        int closestPointIndex = lastClosestPt+1;
        ArrayList<Vector> robotPath = p.getRobotPath();
        for (int startVectorIndex = closestPointIndex; startVectorIndex < robotPath.size()-1; startVectorIndex++) {
            Vector startVector = robotPath.get(startVectorIndex);
            Vector endVector = robotPath.get(startVectorIndex + 1);
            Vector endToStart = Vector.sub(endVector, startVector, null);
            Vector startToEnd = Vector.sub(startVector, endVector, null);
            Vector startToPose = Vector.sub(startVector, currentPose, null);
            Vector endToPose = Vector.sub(endVector, currentPose, null);
            double endAngle = Vector.angleBetween(endToPose, endToStart);
            double startAngle = Vector.angleBetween(startToPose, startToEnd);
            double piOver2 = Math.PI/2;
            if (endAngle < piOver2 && startAngle < piOver2) {
                endToStart.normalize();
                double dot = endToPose.dot(endToStart);
                Vector perpendicular = Vector.mult(endToStart, dot);
                double distance = Vector.sub(currentPose, perpendicular, null).norm();
                if (distance < leastDistance) {
                    leastDistance = distance;
                    closestPointIndex = startVectorIndex+1;
                }
            }
        }
        return closestPointIndex;
    }
    */

    public boolean isDone() {
        return getClosestPointIndex(PoseEstimator.getInstance().getPose()) == p.getRobotPath().size() - 1;
    }
}
