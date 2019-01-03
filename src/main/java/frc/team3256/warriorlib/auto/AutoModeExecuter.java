package frc.team3256.warriorlib.auto;

public class AutoModeExecuter {
    private AutoModeBase autoMode;
    private Thread thread = null;

    public void setAutoMode(AutoModeBase autoMode) {
        this.autoMode = autoMode;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (autoMode != null)
                        autoMode.run();
                }
            });
            thread.start();
        }
    }

    public void stop() {
        if (autoMode != null)
            autoMode.stop();
        autoMode = null;
    }
}
