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

    private final FileExtension extension;
    private final String filePath;
    private final String binaryHash;

    private File file;

    public GameFile(FileExtension extension, String filePath, String binaryHash) {
        this.extension = extension;
        this.filePath = filePath;
        this.binaryHash = binaryHash;
    }

    public GameFile(File file) throws IOException {
        this.file = file;

        filePath = file.getAbsolutePath();
        String fileName = file.getName();
        try {
            int beginIndex = fileName.lastIndexOf('.');
            if (beginIndex == -1) {
                throw new IllegalArgumentException("File did not have a file format");
            }
            extension = FileExtension.getExtensionFor(
                    fileName.substring(beginIndex)
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() + ": " + file.getAbsolutePath());
        }
        binaryHash = new MD5CheckSum(file).toHash();
    }

    private File getFile() {
        if (file == null) {
            file = new File(filePath);
        }
        return file;
    }

    public boolean exists() {
        return getFile().exists();
    }

    public String getAbsolutePath() {
        return filePath;
    }

    public String getFileName() {
        String absolutePath = getAbsolutePath();
        return absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1);
    }

    public boolean matchesHash() {
        try {
            return exists() && binaryHash.equals(new MD5CheckSum(getFile()).toHash());
        } catch (IOException e) {
            ExceptionHandler.handle(Level.WARNING, e);
            return false;
        }
    }

    public FileExtension getExtension() {
        return extension;
    }

    public String getHash() {
        return binaryHash;
    }

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameFile gameFile = (GameFile) o;
        return extension == gameFile.extension &&
                Objects.equals(filePath, gameFile.filePath) &&
                Objects.equals(binaryHash, gameFile.binaryHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extension, filePath, binaryHash);
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
