package frc.team3256.warriorlib.auto.action;

import edu.wpi.first.wpilibj.Timer;

public class WaitAction implements Action {
	private double waitTime, startTime;

	public WaitAction(double waitTime) {
		this.waitTime = waitTime;
	}

	@Override
	public boolean isFinished() {
		return Timer.getFPGATimestamp() - startTime >= waitTime;
	}

	@Override
	public void update() {

	}

	@Override
	public void done() {

	}

	@Override
	public void start() {
		startTime = Timer.getFPGATimestamp();
	}
}
