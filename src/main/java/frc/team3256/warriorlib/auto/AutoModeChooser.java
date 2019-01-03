package frc.team3256.warriorlib.auto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class AutoModeChooser {
    private ArrayList<AutoModeBase> autoModes;

    public AutoModeChooser(AutoModeBase... autoModes) {
        this.autoModes = new ArrayList<>(Arrays.asList(autoModes));
    }

    public void addAutoModes(AutoModeBase... autoModes) {
        this.autoModes.addAll(Arrays.asList(autoModes));
    }

    public String[] getAutoNames() {
        return autoModes.stream().map(autoModeBase -> autoModeBase.getClass().getSimpleName()).toArray(String[]::new);
    }

    public AutoModeBase getChosenAuto(String autoName) {
        Optional<AutoModeBase> autoModeBase = autoModes.stream().filter(autoModeBase1 -> autoModeBase1.getClass().getSimpleName().equals(autoName)).findFirst();
        return autoModeBase.orElse(null);
    }
}
