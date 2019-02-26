package frc.team3256.warriorlib.auto;

/**
 * Executes the chosen autonomous mode
 */
public class AutoModeExecuter {
	private AutoModeBase autoMode;
	private Thread thread = null;
	private boolean finished = false;

	public void setAutoMode(AutoModeBase autoMode) {
        this.finished = false;
        this.thread = null;

		this.autoMode = autoMode;
		this.autoMode.setAutoModeExecuter(this);
	}

	/**
	 * Uses a thread to run the chosen auto mode in parallel to the rest of robot code
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(() -> {
				if (autoMode != null)
					autoMode.run();
			});
			thread.start();
		}
	}

	/**
	 * Stops execution of the chosen auto mode
	 */
	public void stop() {
        if (finished)
            return;
        autoMode.stop();
		autoMode = null;
		finished = true;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
