package com.djrapitops.rom.game;

/**
 * Enum for supported file extensions and consoles they're linked to.
 *
 * @author Rsl1122
 */
public enum FileExtension {
    ;

    private final String extension;
    private final String console;

    FileExtension(String extension, String console) {
        this.extension = extension;
        this.console = console;
    }

    public static FileExtension getExtensionFor(String extension) {
        for (FileExtension ext : values()) {
            if (ext.extension.equals(extension)) {
                return ext;
            }
        }
        throw new IllegalArgumentException("Unsupported extension: " + extension);
    }

    public String getExtension() {
        return extension;
    }

    public String getConsole() {
        return console;
    }
}
