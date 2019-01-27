package frc.team3256.warriorlib.loop;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to run various control loops. {@link Loop}
 */
public class Looper {
	private Notifier notifier;
	private List<Loop> loops;
	private boolean started;
	private double period;
	private double measured_dt = 0.0;
	private double prev_timestamp = 0.0;

	public Looper(double period) {
		this.period = period;
		loops = new ArrayList<>();
		notifier = new Notifier(() -> {
			if (started) {
				double now = Timer.getFPGATimestamp();
				for (Loop loop : loops) {
					loop.update(now);
				}
				measured_dt = now - prev_timestamp;
				prev_timestamp = now;
			}
		});
		started = false;
	}

	/**
	 * Start running the control loop. {@link Loop}
	 */
	public void start() {
		if (started)
			return;
		prev_timestamp = Timer.getFPGATimestamp();
		for (Loop loop : loops) {
			loop.init(prev_timestamp);
		}
		started = true;
		notifier.startPeriodic(period);
	}

	/**
	 * Stop running the control loop. {@link Loop}
	 */
	public void stop() {
		if (!started)
			return;
		notifier.stop();
		started = false;
		double now = Timer.getFPGATimestamp();
		for (Loop loop : loops) {
			loop.end(now);
		}
	}

	/**
	 * Register loop. {@link Loop}
	 *
	 * @param loops Control loop to register
	 */
	public void addLoops(Loop... loops) {
		this.loops.addAll(Arrays.asList(loops));
	}

	public void removeLoop(Loop loop) {
		this.loops.remove(loop);
	}

	/**
	 * @return the measured period of the control loop
	 */
	public double getMeasuredPeriod() {
		return measured_dt;
	}
}