package frc.team3256.warriorlib.hardware;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * Offers basic control of RGB (three-color) LEDs, given a PCM ID as well as RGB ports
 */
public class LED {
	private Solenoid red;
	private Solenoid green;
	private Solenoid blue;

	private LED(int pcmID, int rPort, int gPort, int bPort) {
		red = new Solenoid(pcmID, rPort);
		green = new Solenoid(pcmID, gPort);
		blue = new Solenoid(pcmID, bPort);
	}

	public void red() {
		red.set(true);
		green.set(false);
		blue.set(false);
	}

	public void green() {
		red.set(false);
		green.set(true);
		blue.set(false);
	}

	public void blue() {
		red.set(false);
		green.set(false);
		blue.set(true);
	}

	public void turnOff() {
		red.set(false);
		green.set(false);
		blue.set(false);
	}

	public void set(boolean r, boolean g, boolean b) {
		red.set(r);
		green.set(g);
		blue.set(b);
	}
}
