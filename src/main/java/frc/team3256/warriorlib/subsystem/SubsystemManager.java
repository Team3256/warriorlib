package frc.team3256.warriorlib.subsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages subsystems, handling zeroing of sensors and dashboard output
 */
public class SubsystemManager {
	private List<SubsystemBase> subsystems;

	public SubsystemManager() {
		subsystems = new ArrayList<>();
	}

	public void addSubsystems(SubsystemBase... subsystems) {
		this.subsystems.addAll(Arrays.asList(subsystems));
	}

	public void zeroAllSensors() {
		for (SubsystemBase s : subsystems) {
			s.zeroSensors();
		}
	}

	public void outputToDashboard() {
		for (SubsystemBase s : subsystems) {
			s.outputToDashboard();
		}
	}
}