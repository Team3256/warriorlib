package frc.team3256.warriorlib.auto;

import frc.team3256.warriorlib.auto.action.Action;

/**
 * Base class for autonomous modes
 */
public abstract class AutoModeBase {
	protected double updateRate = 1.0 / 50.0;
	protected boolean active;

    private AutoModeExecuter autoModeExecuter;

	/**
	 * To be overriden; contains actual code to be executed when action is run
	 *
	 * @throws AutoModeEndedException
	 */
	protected abstract void routine() throws AutoModeEndedException;

    public void setAutoModeExecuter(AutoModeExecuter autoModeExecuter) {
        this.autoModeExecuter = autoModeExecuter;
    }

	/**
	 * To not be overriden; runs the code in {@link #routine()}
	 */
	public void run() {
		active = true;
		try {
			routine();
		} catch (AutoModeEndedException e) {
			System.out.println("Auto mode ended early");
		}
		done();
        if (autoModeExecuter != null)
            autoModeExecuter.setFinished(true);
		System.out.println("Auto mode done");
	}

	public void done() {

	}

	public void stop() {
		active = false;
        if (autoModeExecuter != null)
            autoModeExecuter.setFinished(true);
	}

	public boolean isActive() {
		return active;
	}

	public boolean isActiveWithThrow() throws AutoModeEndedException {
		if (!isActive())
			throw new AutoModeEndedException();
		return isActive();
	}

	public void runAction(Action action) throws AutoModeEndedException {
		isActiveWithThrow();
		action.start();

		while (isActiveWithThrow() && !action.isFinished()) {
			action.update();
			long waitTime = (long) (updateRate * 1000L);

			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		action.done();
	}
}
