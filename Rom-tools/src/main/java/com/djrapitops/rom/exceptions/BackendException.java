package com.djrapitops.rom.exceptions;

/**
 * Exception thrown when Backend encounters an error.
 *
 * @author Rsl1122
 */
public class BackendException extends RuntimeException {

    public BackendException(String s) {
        super(s);
    }

    public BackendException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
