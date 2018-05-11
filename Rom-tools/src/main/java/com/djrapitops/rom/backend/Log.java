package com.djrapitops.rom.backend;

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
