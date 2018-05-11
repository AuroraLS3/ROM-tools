package com.djrapitops.rom.exceptions;

/**
 * Exception thrown when an archive file could not be extracted.
 *
 * @author Rsl1122
 */
public class ExtractionException extends AbstractFileException {

    public ExtractionException(String message, String sourcePath) {
        super(message, sourcePath);
    }

    public ExtractionException(String message, Throwable cause, String sourcePath) {
        super(message, cause, sourcePath);
    }

}