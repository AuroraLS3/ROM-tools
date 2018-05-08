package com.djrapitops.rom.util.file;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

public class SevenZExtractorTest extends ExtractorTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private File sevenZFile;
    private File contentsFile;

    @Before
    public void setUp() {
        sevenZFile = getFile("archives/7z.7z");
        contentsFile = getFile("archives/zipContents");
    }

    @Test
    public void testExtraction() throws IOException {
        testExtractionUnencrypted(lines(contentsFile), sevenZFile, temporaryFolder.getRoot());
    }

}