package com.djrapitops.rom.util.file;

import com.djrapitops.rom.exceptions.ExtractionException;
import com.djrapitops.rom.util.Wrapper;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility for extracting .rar-files using junrar.
 * <p>
 * Inspiration from https://stackoverflow.com/a/14656534
 *
 * @author Rsl1122
 */
public class RarExtractor extends ArchiveExtractor {

    RarExtractor(File sourceFile, File destinationFolder, Wrapper<String> password) {
        super(sourceFile, destinationFolder, password);
    }

    @Override
    public void extract() {
        try {
            extractArchive(new Archive(new FileVolumeManager(sourceFile)));
        } catch (RarException | IOException e) {
            throw new ExtractionException("Failed to extract " + sourceFile.getAbsolutePath() + ": " + e.getMessage(), e);
        }
    }

    private void extractArchive(Archive archive) throws RarException, IOException {
        FileHeader fileHeader = archive.nextFileHeader();
        while (fileHeader != null) {
            extractEntry(archive, fileHeader);
            fileHeader = archive.nextFileHeader();
        }
    }

    private void extractEntry(Archive archive, FileHeader fileHeader) throws IOException, RarException {
        File out = new File(destinationFolder, fileHeader.getFileNameString().trim());
        try (FileOutputStream outputStream = new FileOutputStream(out)) {
            archive.extractFile(fileHeader, outputStream);
        }
    }
}