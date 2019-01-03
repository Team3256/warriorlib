package frc.team3256.warriorlib.subsystem;

import frc.team3256.warriorlib.loop.Loop;

public abstract class SubsystemBase implements Loop {

    public abstract void outputToDashboard();

    public abstract void zeroSensors();

}
