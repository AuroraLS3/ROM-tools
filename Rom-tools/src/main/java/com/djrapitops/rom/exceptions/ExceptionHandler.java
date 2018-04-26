package com.djrapitops.rom.exceptions;

import com.djrapitops.rom.backend.Backend;

import java.util.function.BiFunction;
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

    static BiFunction<Void, Throwable, Void> handle() {
        return handle(Level.WARNING);
    }

    static BiFunction<Void, Throwable, Void> handle(Level level) {
        return (result, ex) -> {
            if (ex != null) {
                ExceptionHandler.handle(level, ex);
            }
            return result;
        };
    }
}
