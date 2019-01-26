package frc.team3256.warriorlib.auto.action;

/**
 * Action that runs once (i.e. setting a variable)
 */
public abstract class RunOnceAction implements Action {
	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void update() {

	}

	@Override
	public void done() {

	}

	@Override
	public void start() {
		runOnce();
	}

	public abstract void runOnce();
}
