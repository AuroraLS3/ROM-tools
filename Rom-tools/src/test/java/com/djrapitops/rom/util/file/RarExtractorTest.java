package com.djrapitops.rom.util.file;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

public class RarExtractorTest extends ExtractorTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private File rarFile;
    private File contentsFile;

    @Before
    public void setUp() {
        rarFile = getFile("archives/rar.rar");
        contentsFile = getFile("archives/zipContents");
    }

    @Test
    public void testExtraction() throws IOException {
        testExtractionUnencrypted(lines(contentsFile), rarFile, temporaryFolder.getRoot());
    }

}