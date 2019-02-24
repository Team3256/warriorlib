package frc.team3256.warriorlib.trajectory;

import frc.team3256.robot.Constants;
import frc.team3256.robot.PoseEstimator;

import java.util.ArrayList;

public class TrajectoryCurveGenerator {

    private double robotTrack; //inches

    TrajectoryGenerator trajectoryGenerator;
    Trajectory leadPath;
    Trajectory followPath;

    public TrajectoryCurveGenerator(double acc, double maxVel, double dt, double robotTrack){
        trajectoryGenerator = new TrajectoryGenerator(acc, maxVel, dt);
        this.robotTrack = robotTrack;
    }

    public void generateTrajectoryCurve(double startVel, double endVel, double degrees, double turnRadius){
        double arcLeadLength = 2 * (turnRadius + (robotTrack * 0.5)) * Math.PI * (degrees/360);
        double followScale = (turnRadius - (robotTrack * 0.5))/((turnRadius + (robotTrack * 0.5)));
        leadPath = trajectoryGenerator.generateTrajectory(startVel, endVel, arcLeadLength);
        followPath = trajectoryGenerator.generateScaledTrajectory(leadPath, followScale);
    }

    public Trajectory getLeadPath() {
        return leadPath;
    }

    public Trajectory getFollowPath() {
        return followPath;
    }

    public static void main (String [] args){
        TrajectoryCurveGenerator trajectoryCurveGenerator = new TrajectoryCurveGenerator(144, 144, 0.005, Constants.kRobotTrack);
        trajectoryCurveGenerator.generateTrajectoryCurve(0, 0, 90, 48);
        System.out.println(trajectoryCurveGenerator.getFollowPath());
    }

}
