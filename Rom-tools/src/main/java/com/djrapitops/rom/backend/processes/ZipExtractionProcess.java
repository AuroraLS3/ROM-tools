package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.util.Wrapper;
import com.djrapitops.rom.util.file.ZipExtractor;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Extracts the given ZIP file and callbacks with the extracted files.
 *
 * @author Rsl1122
 */
public class ZipExtractionProcess implements Callable<List<File>> {

    private final long extractTime;

    private final File zipFile;
    private final File destinationFolder;
    private final Wrapper<String> password;

    public ZipExtractionProcess(File zipFile, File destinationFolder, Wrapper<String> password) {
        this.zipFile = zipFile;

        extractTime = System.currentTimeMillis();

        // TODO Format timestamp
        this.destinationFolder = new File(destinationFolder, "" + extractTime);
        this.password = password;
    }

    @Override
    public List<File> call() {
        ZipExtractor zip = new ZipExtractor(zipFile, destinationFolder, password);
        try {
            zip.unzip();
            File[] files = destinationFolder.listFiles();
            if (files != null) {
                return Arrays.asList(files);
            }
        } catch (ZipException e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException("No files extracted");
    }
}
