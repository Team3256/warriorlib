package frc.team3256.warriorlib.auto;

public abstract class AutoModeBase {
    protected double updateRate = 1.0/50.0;
    protected boolean active;

    protected abstract void routine() throws AutoModeEndedException;

    public void run() {
        active = true;
        try {
            routine();
        } catch (AutoModeEndedException e) {
            System.out.println("Auto mode ended early");
        }
        done();
        System.out.println("Auto mode done");
    }

    public void done() {

    }

    public void stop() {
        active = false;
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
