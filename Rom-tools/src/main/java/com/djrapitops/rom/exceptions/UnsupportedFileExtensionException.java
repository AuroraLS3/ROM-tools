package com.djrapitops.rom.exceptions;

/**
 * Exception thrown when a file extension is not supported by the program.
 *
 * @author Rsl1122
 */
public class UnsupportedFileExtensionException extends AbstractFileException {

    private final String extension;

    public UnsupportedFileExtensionException(String message, String sourcePath, String extension) {
        super(message, sourcePath);
        this.extension = extension;
    }

    public UnsupportedFileExtensionException(String message, Throwable cause, String sourcePath, String extension) {
        super(message, cause, sourcePath);
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
