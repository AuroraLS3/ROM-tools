package com.djrapitops.rom.exceptions;

import com.djrapitops.rom.backend.Backend;

import java.util.logging.Level;

/**
 * Interface for handling exceptions in the program.
 * <p>
 * Implementation is UI framework specific.
 *
 * @author Rsl1122
 */
public interface ExceptionHandler {

    static void handle(Level level, Throwable throwable) {
        Backend.getInstance().getExceptionHandler().handleThrowable(level, throwable);
    }

    void handleThrowable(Level level, Throwable throwable);

}
