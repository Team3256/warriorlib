package frc.team3256.warriorlib.auto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * Handles selection of autonomous modes; to be used with custom dashboard
 */
public class AutoModeChooser {
	private ArrayList<AutoModeBase> autoModes;

	public AutoModeChooser(AutoModeBase... autoModes) {
		this.autoModes = new ArrayList<>(Arrays.asList(autoModes));
	}

	public void addAutoModes(AutoModeBase... autoModes) {
		this.autoModes.addAll(Arrays.asList(autoModes));
	}

	/**
	 * Maps each auto mode to its class name for selection purposes
	 *
	 * @return the class names of all auto modes to choose from
	 */
	public String[] getAutoNames() {
		return autoModes.stream().map(autoModeBase -> autoModeBase.getClass().getSimpleName()).toArray(String[]::new);
	}

	/**
	 * Maps a class name to the actual auto mode to execute
	 *
	 * @param autoName the name of the auto mode
	 * @return the corresponding auto mode
	 */
	public AutoModeBase getChosenAuto(String autoName) {
		Optional<AutoModeBase> autoModeBase = autoModes.stream().filter(autoModeBase1 -> autoModeBase1.getClass().getSimpleName().equals(autoName)).findFirst();
		return autoModeBase.orElse(null);
	}
}
