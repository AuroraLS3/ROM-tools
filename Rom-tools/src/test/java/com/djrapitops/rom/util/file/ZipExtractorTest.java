package com.djrapitops.rom.util.file;

import net.lingala.zip4j.exception.ZipException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ZipExtractorTest extends FileTest {

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
    public void testExtraction() throws IOException, ZipException {
        List<String> expected = lines(contentsFile);

        ZipExtractor extractor = new ZipExtractor(sourceFile, temporaryFolder.getRoot(), () -> "No Password");
        extractor.unzip();

        File unZipped = new File(temporaryFolder.getRoot(), "zipContents");
        assertNotNull(unZipped);
        List<String> result = lines(unZipped);
        assertEquals(expected, result);
    }

}