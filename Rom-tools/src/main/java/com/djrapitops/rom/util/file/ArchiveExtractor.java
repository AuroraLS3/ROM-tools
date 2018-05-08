package com.djrapitops.rom.util.file;

import com.djrapitops.rom.exceptions.ExtractionException;
import com.djrapitops.rom.util.Verify;
import com.djrapitops.rom.util.Wrapper;

import java.io.File;

/**
 * Abstract representation of a class that manages extracting archives.
 *
 * @author Rsl1122
 */
public abstract class ArchiveExtractor {

    private static final String[] SUPPORTED_FORMATS = new String[]{".zip", ".rar", ".7z"};
    final File sourceFile;
    final File destinationFolder;
    final Wrapper<String> password;

    /**
     * Constructor.
     *
     * @param sourceFile        Source file (zip) - Not null
     * @param destinationFolder Folder to extract to. - Not null
     * @param password          Wrapper to get password in case zip is password protected
     * @throws IllegalArgumentException If not null value is null or folder is not a directory.
     */
    ArchiveExtractor(File sourceFile, File destinationFolder, Wrapper<String> password) {
        Verify.notNull(sourceFile, () -> new IllegalArgumentException("Source file was null"));
        Verify.notNull(destinationFolder, () -> new IllegalArgumentException("Destination folder was null"));

        boolean isFolder = !destinationFolder.exists() || destinationFolder.isDirectory();
        Verify.isTrue(isFolder, () -> new IllegalArgumentException("Destination folder was not a folder; " + destinationFolder));

        this.sourceFile = sourceFile;
        this.destinationFolder = destinationFolder;
        this.password = password;
    }

    /**
     * Method for creating a new ArchiveExtractor instance that is appropriate for the file.
     *
     * @param sourceFile        File to extract.
     * @param destinationFolder Folder to extract to.
     * @param password          Wrapper method for a possible password.
     * @return new instance implementing ArchiveExtractor for the file format.
     */
    public static ArchiveExtractor createExtractorFor(File sourceFile, File destinationFolder, Wrapper<String> password) {
        String fileName = sourceFile.getName();

        if (fileName.endsWith(".zip")) {
            return new ZipExtractor(sourceFile, destinationFolder, password);
        } else if (fileName.endsWith(".rar")) {
            return new RarExtractor(sourceFile, destinationFolder, password);
        } else if (fileName.endsWith(".7z")) {
            return new SevenZExtractor(sourceFile, destinationFolder, password);
        }
        throw new ExtractionException("Unsupported archive format");
    }

    public static boolean isSupportedArchiveFile(String fileName) {
        for (String supportedFormat : SUPPORTED_FORMATS) {
            if (fileName.endsWith(supportedFormat)) {
                return true;
            }
        }
        return false;

    }

    /**
     * Extracts the zip archive.
     *
     * @throws ExtractionException If the extraction fails.
     */
    public abstract void extract();
}