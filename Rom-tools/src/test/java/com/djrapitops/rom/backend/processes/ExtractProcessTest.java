package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.util.file.FileTest;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class ExtractProcessTest extends FileTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File sourceFile;
    private File contentsFile;


    @Before
    public void setUp() {
        sourceFile = getFile("zip.zip");
        contentsFile = getFile("zipContents");
    }

    @Test
    public void testExtraction() throws IOException {
        List<String> expected = lines(contentsFile);

        List<File> extracted = FileProcesses.extract(sourceFile, temporaryFolder.getRoot(), () -> null);

        assertFalse(expected.isEmpty());
        File unZipped = extracted.get(0);
        assertNotNull(unZipped);
        List<String> result = lines(unZipped);
        assertEquals(expected, result);
    }
}