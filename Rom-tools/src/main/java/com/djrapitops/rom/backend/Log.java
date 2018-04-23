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

    public static void debug(String msg) {
        // TODO Use logger.
        System.out.println(msg);
    }

    public static void log(String msg) {
        Backend.getInstance().getFrontend().getState().performStateChange(state -> state.setLoadingInformation(msg));
    }

}
