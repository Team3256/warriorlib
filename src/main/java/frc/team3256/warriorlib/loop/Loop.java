package frc.team3256.warriorlib.loop;

/**
 * Interface representing a control loop
 */
public interface Loop {
	void init(double timestamp);

	void update(double timestamp);

	void end(double timestamp);
}