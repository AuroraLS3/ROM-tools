package com.djrapitops.rom.util.file;

import com.djrapitops.rom.util.Verify;
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
public class ZipExtractor {

    private final File sourceFile;
    private final File destinationFolder;
    private final Wrapper<String> password;

    /**
     * Constructor.
     *
     * @param sourceFile        Source file (zip) - Not null
     * @param destinationFolder Folder to extract to. - Not null
     * @param password          Wrapper to get password in case zip is password protected
     * @throws IllegalArgumentException If not null value is null or folder is not a directory.
     */
    public ZipExtractor(File sourceFile, File destinationFolder, Wrapper<String> password) {
        Verify.notNull(sourceFile, () -> new IllegalArgumentException("Source file was null"));
        Verify.notNull(destinationFolder, () -> new IllegalArgumentException("Destination folder was null"));

        boolean isFolder = !destinationFolder.exists() || destinationFolder.isDirectory();
        Verify.isTrue(isFolder, () -> new IllegalArgumentException("Destination folder was not a folder; " + destinationFolder));

        this.sourceFile = sourceFile;
        this.destinationFolder = destinationFolder;
        this.password = password;
    }

    /**
     * Extracts the zip archive.
     *
     * @throws ZipException If the extraction fails.
     */
    public void unzip() throws ZipException {
        ZipFile zipFile = new ZipFile(sourceFile);
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(password.get());
        }
        zipFile.extractAll(destinationFolder.getAbsolutePath());
    }

}
