package frc.team3256.warriorlib.trajectory;

public class TrajectoryGenerator {
    private double acc;
    private double maxVel;
    private double dt;

    public double accelDistance, decelDistance, cruiseDistance;

    public TrajectoryGenerator(double acc, double maxVel, double dt){
        this.acc = acc;
        this.maxVel = maxVel;
        this.dt = dt;
    }

    public Trajectory generateTrajectory(double startVel, double endVel, double distance){
        double cruiseVel = Math.min(Math.sqrt((distance * acc) + ((Math.pow(startVel, 2) + Math.pow(endVel, 2))/2)), maxVel);
        double accelTime = (cruiseVel - startVel)/acc;
        double decelTime = Math.abs(cruiseVel - endVel)/acc;
        accelDistance = startVel * accelTime + (0.5 * acc * Math.pow(accelTime, 2));
        decelDistance = cruiseVel * decelTime - (0.5 * acc * Math.pow(decelTime, 2));
        cruiseDistance = distance - accelDistance - decelDistance;
        double cruiseTime = cruiseDistance/ cruiseVel;
        double totalTime = accelTime + decelTime + cruiseTime;
        int size = (int)(totalTime/dt);
        Trajectory trajectory = new Trajectory(size);
        double currTime = 0;
        //System.out.println("Cruise Vel: " + cruiseVel);
        for (int i = 0; i < size; i++){
            double currPos, currVel, currAccel;
            if (currTime <= accelTime){
                currAccel = acc;
                currPos = startVel * currTime + (0.5 * acc * Math.pow(currTime, 2));
                currVel = startVel + (currAccel * currTime);
            }
            else if (currTime > accelTime && currTime < (accelTime + cruiseTime)){
                currPos = accelDistance + (cruiseVel * (currTime - accelTime));
                currVel = cruiseVel;
                currAccel = 0;
            }
            else {
                double tempCurrTime = currTime - (accelTime + cruiseTime);
                double adjustedCurrTime = totalTime - accelTime - cruiseTime - tempCurrTime;
                double adjustedCurrPos = endVel * adjustedCurrTime + 0.5 * acc * Math.pow(adjustedCurrTime, 2);
                currAccel = -acc;
                currPos = distance - adjustedCurrPos;
                currVel = cruiseVel + (currAccel * tempCurrTime);
            }
            currTime += dt;
            Trajectory.Point point = new Trajectory.Point(currPos, currVel, currAccel, currTime);
            trajectory.addPoint(i, point);
        }
        return trajectory;
    }

    public Trajectory generateScaledTrajectory(Trajectory leadTrajectory, double scale){
        Trajectory followTrajectory = new Trajectory(leadTrajectory.points.size());
        for (int i = 0; i < leadTrajectory.points.size(); i++){
            Trajectory.Point leadPoint = leadTrajectory.points.get(i);
            double followPosition = scale * leadPoint.getPos();
            double followVel = scale * leadPoint.getVel();
            Trajectory.Point followPoint = new Trajectory.Point(followPosition, followVel, leadPoint.getAcc(), leadPoint.getTime());
            followTrajectory.addPoint(i, followPoint);
        }
        return followTrajectory;
    }
}
