package frc.team3256.warriorlib.auto.action;

/**
 * Actions each represent a single task done, such as moving forward, raising an elevator, etc.
 */
public interface Action {
	/**
	 * Returns whether or not the code has finished execution. When implementing this interface, this method is used by
	 * the runAction method every cycle to know when to stop running the action
	 *
	 * @return boolean
	 */
	boolean isFinished();

	/**
	 * Called by runAction in AutoModeBase iteratively until isFinished returns true. Iterative logic lives in this
	 * method
	 */
	void update();

	/**
	 * Run code once when the action finishes, usually for clean up
	 */
	void done();

	/**
	 * Run code once when the action is started, for set up
	 */
	void start();
}
