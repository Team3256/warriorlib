package frc.team3256.warriorlib.auto.operations;

public class Util {

    public static double kWheelDiameter;
    public static double kGearRatio;

    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }

    public static double handleDeadband(double a, double deadband) {
        if (Math.abs(a) <= deadband)
            return 0;
        else
            return a;
    }

    public static double clip(double a, double min, double max) {
        if(a < min) return min;
        if(a > max) return max;
        return a;
    }

    public static void setWheelDiameter(double wheelDiameter) {
        Util.kWheelDiameter = wheelDiameter;
    }

    public static void setGearRatio(double gearRatio) {
        Util.kGearRatio = gearRatio;
    }

    public static double rpmToVelocity(double rpm) {         // inches per second
        return rpmToInchesPerSec(rpm) / kGearRatio;
    }

    public static double rotationsToPosition(double rotations) {    // inches
        return rotationsToInches(rotations) / kGearRatio;
    }

    public static double inchesToRotations(double inches) {
        return inches / (kWheelDiameter * Math.PI);
    }

    public static double rotationsToInches(double rotations) {
        return rotations * Math.PI * kWheelDiameter;
    }

    public static double inchesPerSecToRPM(double inchesPerSec) {
        return inchesToRotations(inchesPerSec) * 60D;
    }

    public static double rpmToInchesPerSec(double rpm) {
        return rotationsToInches(rpm) / 60D;
    }


}