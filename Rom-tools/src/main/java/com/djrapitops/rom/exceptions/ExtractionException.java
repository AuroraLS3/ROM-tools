package com.djrapitops.rom.exceptions;

/**
 * Exception thrown when an archive file could not be extracted.
 *
 * @author Rsl1122
 */
public class ExtractionException extends RuntimeException {

    public ExtractionException(String message) {
        super(message);
    }

    public ExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

}