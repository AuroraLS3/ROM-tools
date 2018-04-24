package com.djrapitops.rom.backend;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for logging messages.
 *
 * @author Rsl1122
 */
public class Log {

    private Log() {
        /* Hide Constructor */
    }

    public static void debug(String msg) {
        Logger.getGlobal().log(Level.INFO, msg);
    }

    public static void log(String msg) {
        Backend.getInstance()
                .getFrontend()
                .getState()
                .performStateChange(state -> state.setLoadingInformation(msg));
    }

}
