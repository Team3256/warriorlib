package frc.team3256.warriorlib.auto;

/**
 * Executes the chosen autonomous mode
 */
public class AutoModeExecuter {
    private AutoModeBase autoMode;
    private Thread thread = null;

    public void setAutoMode(AutoModeBase autoMode) {
        this.autoMode = autoMode;
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
        if (autoMode != null)
            autoMode.stop();
        autoMode = null;
    }
}
