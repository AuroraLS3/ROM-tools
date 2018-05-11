package com.djrapitops.rom.exceptions;

/**
 * Abstract exception that contains path of the file that caused the exception.
 *
 * @author Rsl1122
 */
public abstract class AbstractFileException extends RuntimeException {

    private final String sourcePath;

    AbstractFileException(String message, String sourcePath) {
        super(message);
        this.sourcePath = sourcePath;
    }

    AbstractFileException(String message, Throwable cause, String sourcePath) {
        super(message, cause);
        this.sourcePath = sourcePath;
    }

    public String getSourcePath() {
        return sourcePath;
    }
}
