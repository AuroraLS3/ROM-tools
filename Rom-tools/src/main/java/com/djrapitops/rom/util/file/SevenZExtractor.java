package com.djrapitops.rom.util.file;

import com.djrapitops.rom.exceptions.ExtractionException;
import com.djrapitops.rom.util.Wrapper;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility for extracting .7z-files using commons-compress.
 * <p>
 * Inspired by https://stackoverflow.com/a/21898539
 *
 * @author Rsl1122
 */
public class SevenZExtractor extends ArchiveExtractor {

    public SevenZExtractor(File sourceFile, File destinationFolder, Wrapper<String> password) {
        super(sourceFile, destinationFolder, password);
    }

    @Override
    public void extract() {
        try (SevenZFile sevenZFile = new SevenZFile(sourceFile)) {
            extractFile(sevenZFile);
        } catch (IOException e) {
            throw new ExtractionException("Failed to extract " + sourceFile.getAbsolutePath() + ": " + e.getMessage(), e);
        }
    }

    private void extractFile(SevenZFile sevenZFile) throws IOException {
        SevenZArchiveEntry entry = sevenZFile.getNextEntry();
        while (entry != null) {
            extractEntry(sevenZFile, entry);
            entry = sevenZFile.getNextEntry();
        }
    }

    private void extractEntry(SevenZFile sevenZFile, SevenZArchiveEntry entry) throws IOException {
        File out = new File(destinationFolder, entry.getName());
        try (FileOutputStream outputStream = new FileOutputStream(out)) {
            byte[] content = new byte[(int) entry.getSize()];
            sevenZFile.read(content, 0, content.length);
            outputStream.write(content);
        }
    }
}