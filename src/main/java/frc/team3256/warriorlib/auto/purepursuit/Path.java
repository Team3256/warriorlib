package frc.team3256.warriorlib.auto.purepursuit;

import frc.team3256.warriorlib.math.Vector;

import java.util.ArrayList;

/**
 * Represents a path to be followed by the Pure Pursuit algorithm
 */
public class Path {
	private double spacing;
	private ArrayList<Vector> robotPath = new ArrayList<>();
	private Vector endVector = new Vector(0, 0);
	private boolean forward;

	public Path(double spacing, boolean forward) {
		this.spacing = spacing;
		this.forward = forward;
	}

	public ArrayList<Vector> getRobotPath() {
		return robotPath;
	}

	public Vector getStartPoint() {
		return robotPath.get(0);
	}

	public Vector getEndPoint() {
		return robotPath.get(robotPath.size() - 1);
	}

	public boolean isForward() {
		return forward;
	}

	/**
	 * Initializes the path
	 *
	 * @param maxVel   maximum robot velocity
	 * @param maxAccel maximum robot acceleration
	 * @param maxVelk  maximum turning velocity (between 1-5)
	 */
	public void initializePath(double maxVel, double maxAccel, double maxVelk) {
		setCurvatures();
		setDistances();
		setTargetVelocities(maxVel, maxAccel, maxVelk);
	}

	/**
	 * Adds a path segment to this path
	 * @param start starting point
	 * @param end ending point
	 */
	public void addSegment(Vector start, Vector end) {
		ArrayList<Vector> injectTemp = new ArrayList<>();
		injectPoints(start, end, injectTemp);
		robotPath.addAll(injectTemp);
		endVector = end;
	}

	/**
	 * Injects points into this path
	 * @param startPt starting point
	 * @param endPt ending point
	 * @param temp temporary storage for injected points
	 */
	private void injectPoints(Vector startPt, Vector endPt, ArrayList<Vector> temp) {
		Vector vector = new Vector(Vector.sub(endPt, startPt, null));
		double pointsCount = Math.ceil(vector.norm() / spacing);
		Vector unitVector = vector.normalize(null);
		unitVector.mult(vector.norm() / pointsCount);
		for (int i = 0; i < pointsCount; i++) {
			Vector newVector = Vector.mult(unitVector, i, null);
			temp.add(Vector.add(startPt, newVector, null));
		}
	}

	/**
	 * Smooths the path using gradient descent
	 * @param a 1-b
	 * @param b smoothing factor (higher = more smooth)
	 * @param tolerance convergence tolerance amount (higher = less smoothing)
	 */
	public void smooth(double a, double b, double tolerance) {
		ArrayList<Vector> newPath = new ArrayList<>();
		for (Vector v : robotPath) {
			newPath.add(new Vector(v));
		}
		double change = tolerance;
		while (change >= tolerance) {
			change = 0.0;
			for (int i = 1; i < robotPath.size() - 1; ++i) {
				Vector oldVec = robotPath.get(i);
				Vector currVec = newPath.get(i);
				Vector currVecCopy = new Vector(currVec);
				Vector prevVec = newPath.get(i - 1);
				Vector nextVec = newPath.get(i + 1);
				currVec.x += a * (oldVec.x - currVec.x) + b * (prevVec.x + nextVec.x - 2 * currVec.x);
				currVec.y += a * (oldVec.y - currVec.y) + b * (prevVec.y + nextVec.y - 2 * currVec.y);
				change += Math.abs(currVecCopy.x - currVec.x);
				change += Math.abs(currVecCopy.y - currVec.y);
			}
		}
		ArrayList<Vector> path = new ArrayList<>();
		for (Vector v : newPath)
			path.add(new Vector(v));
		robotPath = path;
	}

	public void addLastPoint() {
		robotPath.add(endVector);
	}

	//calculations for point attributes (curvature and max velocity)

	private double calculatePathCurvature(ArrayList<Vector> path, int pointIndex) {
		Vector point = new Vector(path.get(pointIndex));
		Vector prevPoint = new Vector(path.get(pointIndex - 1));
		Vector nextPoint = new Vector(path.get(pointIndex + 1));

		double distanceOne = Vector.dist(point, prevPoint);
		double distanceTwo = Vector.dist(point, nextPoint);
		double distanceThree = Vector.dist(nextPoint, prevPoint);

		double productOfSides = distanceOne * distanceTwo * distanceThree;
		double semiPerimeter = (distanceOne + distanceTwo + distanceThree) / 2;
		double triangleArea = Math.sqrt(semiPerimeter * (semiPerimeter - distanceOne) * (semiPerimeter - distanceTwo) * (semiPerimeter - distanceThree));

		double radius = (productOfSides) / (4 * triangleArea);
		double curvature = 1 / radius;

		return curvature;
	}

	private double calculateMaxVelocity(ArrayList<Vector> path, int point, double pathMaxVel, double k) {
		if (point > 0) {
			double curvature = calculatePathCurvature(path, point);
			return Math.min(pathMaxVel, k / curvature); //k is a constant (generally between 1-5 based on how quickly you want to make the turn)

		}
		return pathMaxVel;
	}

	public double calculateCurrDistance(int point) {
		return robotPath.get(point).getDistance();
	}

	public double getTotalPathDistance() {
		return calculateCurrDistance(robotPath.size() - 1);
	}

	//setter methods that iterate through and set attributes to robotPath

	public void setCurvatures() {
		getStartPoint().setCurvature(0);
		getEndPoint().setCurvature(0);
		for (int i = 1; i < robotPath.size() - 1; i++) {
			robotPath.get(i).setCurvature(calculatePathCurvature(robotPath, i));
		}
	}

	public void setTargetVelocities(double maxVel, double maxAccel, double k) {
		robotPath.get(robotPath.size() - 1).setVelocity(0);
		for (int i = robotPath.size() - 2; i >= 0; i--) {
			double distance = Vector.dist(robotPath.get(i + 1), robotPath.get(i));
			//System.out.println(robotPath.get(i));
			double maxReachableVel = Math.sqrt(Math.pow(robotPath.get(i + 1).getVelocity(), 2) + (2 * maxAccel * distance));
			robotPath.get(i).setVelocity(Math.min(calculateMaxVelocity(robotPath, i, maxVel, k), maxReachableVel));
		}
	}

	public void setDistances() {
		double distance = 0;
		getStartPoint().setDistance(0);
		for (int i = 1; i < robotPath.size(); i++) {
			distance += Vector.sub(robotPath.get(i), robotPath.get(i - 1)).norm();
			robotPath.get(i).setDistance(distance);
		}
	}

	//calculating the curvature necessary for a lookahead arc

	public double calculateCurvatureLookAheadArc(Vector currPos, double heading, Vector lookahead, double lookaheadDistance) {
		double a = -Math.tan(heading);
		double b = 1;
		double c = (Math.tan(heading) * currPos.x) - currPos.y;
		double x = Math.abs(a * lookahead.x + b * lookahead.y + c) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
		double cross = (Math.sin(heading) * (lookahead.x - currPos.x)) - (Math.cos(heading) * (lookahead.y - currPos.y));
		double side = cross > 0 ? 1 : -1;
		double curvature = (2 * x) / (Math.pow(lookaheadDistance, 2));
		return curvature * side;
	}
}
