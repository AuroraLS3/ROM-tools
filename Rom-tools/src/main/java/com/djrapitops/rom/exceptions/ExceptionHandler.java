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

    /**
     * Static method for handling any exceptions anywhere.
     *
     * @param level     Level of severity.
     * @param throwable Throwable.
     */
    static void handle(Level level, Throwable throwable) {
        Backend.getInstance().getExceptionHandler().handleThrowable(level, throwable);
    }

    /**
     * Method for returning a functional interface to be used with CompletableFuture API.
     * <p>
     * Uses Level.WARNING for any exceptions.
     *
     * @return BiFunction.
     */
    static BiFunction<Void, Throwable, Void> handle() {
        return handle(Level.WARNING);
    }

    /**
     * Method for returning a functional interface to be used with CompletableFuture API.
     *
     * @param level Level used for the exceptions that occur.
     * @return BiFunction.
     */
    static BiFunction<Void, Throwable, Void> handle(Level level) {
        return (result, ex) -> {
            if (ex != null) {
                ExceptionHandler.handle(level, ex);
            }
            return result;
        };
    }

    /**
     * Handles what should be done with a Throwable that occurred.
     *
     * @param level     Level of severity.
     * @param throwable Throwable.
     */
    void handleThrowable(Level level, Throwable throwable);
}
