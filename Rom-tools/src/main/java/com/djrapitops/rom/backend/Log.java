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

    /**
     * Logs a message to the global Logger.
     * <p>
     * Does not appear on the program if not run via command prompt.
     *
     * @param msg Message to log.
     * @see Logger
     */
    public static void debug(String msg) {
        Logger.getGlobal().log(Level.INFO, msg);
    }

    /**
     * Log a message to the status bar of the frontend.
     *
     * @param msg Message to log.
     * @see com.djrapitops.rom.frontend.state.State
     */
    public static void log(String msg) {
        Backend.getInstance()
                .getFrontend()
                .getState()
                .performStateChange(state -> state.setStatus(msg));
    }

}
