package com.djrapitops.rom.util.file;

import com.djrapitops.rom.exceptions.ExtractionException;
import com.djrapitops.rom.util.Wrapper;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;

/**
 * Utility for extracting .zip files using zip4j.
 * <p>
 * Inspiration from https://stackoverflow.com/a/14656534
 *
 * @author Rsl1122
 */
public class ZipExtractor extends ArchiveExtractor {

    /**
     * Constructor.
     *
     * @param sourceFile        Source file (zip) - Not null
     * @param destinationFolder Folder to extract to. - Not null
     * @param password          Wrapper to get password in case zip is password protected
     * @throws IllegalArgumentException If not null value is null or folder is not a directory.
     */
    ZipExtractor(File sourceFile, File destinationFolder, Wrapper<String> password) {
        super(sourceFile, destinationFolder, password);
    }

    @Override
    public void extract() {
        try {
            ZipFile zipFile = new ZipFile(sourceFile);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password.get());
            }
            zipFile.extractAll(destinationFolder.getAbsolutePath());
        } catch (ZipException e) {
            throw new ExtractionException("Failed to extract " + sourceFile.getAbsolutePath() + ": " + e.getMessage(), e);
        }
    }

}
