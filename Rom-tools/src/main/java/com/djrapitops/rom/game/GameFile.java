package com.djrapitops.rom.game;

import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.util.file.MD5CheckSum;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Object that contains information about a file that stores the game.
 *
 * @author Rsl1122
 */
public class GameFile {

    private final String extension;
    private final String filePath;
    private final String binaryHash;

    private File file;

    /**
     * Constructor for GameFile in the database.
     *
     * @param extension  FileExtension of the GameFile
     * @param filePath   Absolute path of the file.
     * @param binaryHash MD5 hash of the file calculated earlier.
     */
    public GameFile(String extension, String filePath, String binaryHash) {
        this.extension = extension;
        this.filePath = filePath;
        this.binaryHash = binaryHash;
    }

    /**
     * Constructor for new instances of GameFile.
     * <p>
     * Performs IO operations on the file - reads all bytes to generate a MD5 hash.
     *
     * @param file File to create object for.
     * @throws IOException If file can not be read.
     */
    public GameFile(File file) throws IOException {
        this.file = file;

        filePath = file.getAbsolutePath();
        extension = FileExtension.getFor(file);
        binaryHash = new MD5CheckSum(file).toHash();
    }

    public File getFile() {
        if (file == null) {
            file = new File(filePath);
        }
        return file;
    }

    /**
     * Check if the file exists on the machine.
     * <p>
     * Creates a File object if not present.
     *
     * @return true/false
     */
    public boolean exists() {
        return getFile().exists();
    }

    public String getAbsolutePath() {
        return filePath;
    }

    /**
     * Used to get the file name without using IO operations on a File object.
     *
     * @return Name of the file from the absolute path String.
     */
    public String getFileName() {
        String absolutePath = getAbsolutePath();
        return absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * Check if the file exists and matches the hash calculated earlier.
     *
     * @return true/false
     */
    public boolean matchesHash() {
        try {
            return exists() && binaryHash.equals(new MD5CheckSum(getFile()).toHash());
        } catch (IOException e) {
            ExceptionHandler.handle(Level.WARNING, e);
            return false;
        }
    }

    public String getExtension() {
        return extension;
    }

    public String getHash() {
        return binaryHash;
    }

    /**
     * Get a clean name of the file without extension or anything inside brackets.
     * <p>
     * Example: "game (Atari) [1983].a26"
     *
     * @return Clean name, Example: "game"
     */
    public String getCleanName() {
        String fileName = getFileName();

        // Remove brackets
        fileName = fileName.replaceAll("\\(.*\\)", "");
        fileName = fileName.replaceAll("\\[.*]", "");
        // Remove extension
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        // Remove underscores
        fileName = fileName.replace("_", " ");

        return fileName.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameFile gameFile = (GameFile) o;
        return extension.equals(gameFile.extension) &&
                Objects.equals(binaryHash, gameFile.binaryHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extension, binaryHash);
    }

    @Override
    public String toString() {
        return "GameFile{" +
                "extension=" + extension +
                ", filePath='" + filePath + '\'' +
                ", binaryHash='" + binaryHash + '\'' +
                '}';
    }
}
