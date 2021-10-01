package frc.team3256.warriorlib.operations.logging;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RobotLogger {

    //Does not Allow Instances
    private RobotLogger(){}

    // ******* CONFIG ******* //
    public static Level LOG_LEVEL = Level.WARNING;
    public static Level CONSOLE_LEVEL = Level.WARNING;

    //Forces Normal Logging to Internal Storage
    public static boolean FORCE_NORMAL_INTERNAL = false;

    //Max Number of Files
    public static int TXT_LOG_MAX_FILES = 10;
    public static int HTML_LOG_MAX_FILES = 3;

    //Normal Max File Sizes
    public static int TXT_LOG_MAX_SIZE  = 3000000; // In Bytes
    public static int HTML_LOG_MAX_SIZE = 100000000; // In Bytes

    //File Names (%g is for Numbering of Files)
    public static String TXT_FILE_NAME = "TextLog%g.txt";
    public static String HTML_FILE_NAME = "HtmlLog%g.html";


    //Emergency File Settings (NO USB)
    public static int EMERGENCY_TXT_MAX_FILES = 3;
    public static int EMERGENCY_TXT_MAX_SIZE = 3000000; //In Bytes
    // ******************************* //

    static private Logger globalLogger;

    /**
     * Sets Up Logging Format and Output
     * Call AFTER Configuration
     */
    static public void setup() {

        String homePath = Filesystem.getOperatingDirectory().getAbsolutePath();

        // get the global logger to configure it, applies to all loggers
        globalLogger = Logger.getLogger("");

        globalLogger.setLevel(LOG_LEVEL);
        globalLogger.getHandlers()[0].setLevel(CONSOLE_LEVEL);

        //If Running Unit Tests / Simulation, Disable any File Access
        if (!RobotBase.isReal()) {
            return;
        }

        if (Paths.get("u").toFile().exists()) {
            normalLog("u"); // USB Defaults to /u for Mounting
        } else {
            System.err.println("NO USB DRIVE");

            if (FORCE_NORMAL_INTERNAL) {
                System.err.println("Forcing Normal Logging to Internal");
                normalLog(homePath);
            } else {
                emergencyLog();
            }
        }



    }

    /**
     * Logs to USB Drive with HTML + Text
     */
    static private void normalLog(String pathToLogInto){
        String homePath = Filesystem.getOperatingDirectory().getAbsolutePath();

        //Backup Text File, In case USB gets corrupted
        Path txtFilePath = Paths.get(homePath, TXT_FILE_NAME);

        Path htmlFilePath = Paths.get(pathToLogInto, HTML_FILE_NAME);

        try {
            FileHandler fileHTMLHandler = new FileHandler(
                    htmlFilePath.toString(), HTML_LOG_MAX_SIZE, HTML_LOG_MAX_FILES, false);
            FileHandler fileTxtHandler = new FileHandler(
                    txtFilePath.toString(), TXT_LOG_MAX_SIZE, TXT_LOG_MAX_FILES, false);

            fileHTMLHandler.setFormatter(new HtmlFormatter());
            globalLogger.addHandler(fileHTMLHandler);
            fileTxtHandler.setFormatter(new OneLineFormatter());
            globalLogger.addHandler(fileTxtHandler);

        } catch(IOException e){
            System.err.println("Normal Log FAILED - IOException - Going to Emergency Log");
            e.printStackTrace();
            emergencyLog();
        }
    }

    /**
     * When normal Logging Fails, Log minimally to Internal Storage
     */
    static private void emergencyLog() {
        for (int i = 0; i < 3; i++)
            System.err.println("Emergency LOG - Logging to Internal");

        try {
            String homePath = Filesystem.getOperatingDirectory().getAbsolutePath();

            Path emergencyTxtPath = Paths.get(homePath, TXT_FILE_NAME);
            FileHandler fileTxtHandler = new FileHandler(
                    emergencyTxtPath.toString(), EMERGENCY_TXT_MAX_SIZE, EMERGENCY_TXT_MAX_FILES, false);

            fileTxtHandler.setFormatter(new OneLineFormatter());
            globalLogger.addHandler(fileTxtHandler);

        } catch (IOException e){
            System.err.println("EMERGENCY LOG FAILED - CANT CREATE FILE");
            e.printStackTrace();
            System.err.println("LOGGING ONLY TO CONSOLE");
        }
    }

    //*********** Getters ***********//

    /**
     * Level that all Logging Systems should log
     * Should be WARNING at Competition
     */
    public static void setLogLevel(Level logLevel) {
        LOG_LEVEL = logLevel;
    }

    /**
     * Level that the console should log
     * Should be WARNING at most times, too cluttered otherwise.
     */
    public static void setConsoleLevel(Level consoleLevel) {
        CONSOLE_LEVEL = consoleLevel;
    }

    /**
     * Forces System to Log Normally to Internal Storage
     * Make sure to use with Caution, this can take a lot of INTERNAL STORAGE
     */
    public static void setForceNormalInternal(boolean forceNormalInternal) {
        FORCE_NORMAL_INTERNAL = forceNormalInternal;
    }

    public static void setTxtLogMaxFiles(int txtLogMaxFiles) {
        TXT_LOG_MAX_FILES = txtLogMaxFiles;
    }

    public static void setHtmlLogMaxFiles(int htmlLogMaxFiles) {
        HTML_LOG_MAX_FILES = htmlLogMaxFiles;
    }

    /**
     * @param txtLogMaxSize In Bytes
     */
    public static void setTxtLogMaxSize(int txtLogMaxSize) {
        TXT_LOG_MAX_SIZE = txtLogMaxSize;
    }

    /**
     * @param htmlLogMaxSize In Bytes
     */
    public static void setHtmlLogMaxSize(int htmlLogMaxSize) {
        HTML_LOG_MAX_SIZE = htmlLogMaxSize;
    }

    /**
     * Use %g in the String to allow for Multiple Files
     * Ex. LogTxt%g.txt -> LogTxt1.txt, LogTxt2.txt
     */
    public static void setTxtFileName(String txtFileName) {
        TXT_FILE_NAME = txtFileName;
    }

    /**
     * Use %g in the String to allow for Multiple Files
     * Ex. LogTxt%g.txt -> LogTxt1.txt, LogTxt2.txt
     */
    public static void setHtmlFileName(String htmlFileName) {
        HTML_FILE_NAME = htmlFileName;
    }


    /**
     * If Logging with no USB, Max Number of Log Files
     * Be Careful, RoboRIO has only 512MB of Internal Storage
     * And we still need to Fit a Program!
     */
    public static void setEmergencyTxtMaxFiles(int emergencyTxtMaxFiles) {
        EMERGENCY_TXT_MAX_FILES = emergencyTxtMaxFiles;
    }

    /**
     * If Logging with no USB, Max Size of Log Files
     * Be Careful, RoboRIO has only 512MB of Internal Storage
     * And we still need to Fit a Program!
     * @param emergencyTxtMaxSize In Bytes
     */
    public static void setEmergencyTxtMaxSize(int emergencyTxtMaxSize) {
        EMERGENCY_TXT_MAX_SIZE = emergencyTxtMaxSize;
    }

}
