package com.djrapitops.rom.game;

import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.util.file.MD5CheckSum;

import java.io.File;
import java.io.IOException;
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
        return getFile().getName();
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
}
