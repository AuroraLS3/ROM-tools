package com.djrapitops.rom.util.file;

import com.djrapitops.rom.exceptions.ExtractionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ZipExtractorTest extends ExtractorTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private File zipFile;
    private File passwordZipFile;
    private File emptyZipFile;
    private File contentsFile;

    @Before
    public void setUp() {
        zipFile = getFile("archives/zip.zip");
        passwordZipFile = getFile("archives/passworded_zip.zip");
        emptyZipFile = getFile("archives/empty_zip.zip");
        contentsFile = getFile("archives/zipContents");
    }

    @Test
    public void testExtraction() throws IOException {
        testExtractionUnencrypted(lines(contentsFile), zipFile, temporaryFolder.getRoot());
    }

    @Test
    public void testExtractionEncrypted() throws IOException {
        List<String> expected = lines(contentsFile);

        // Password for the zip file is 'password'
        ArchiveExtractor extractor = ArchiveExtractor.createExtractorFor(
                passwordZipFile, temporaryFolder.getRoot(), () -> "password"
        );
        extractor.extract();

        File unZipped = new File(temporaryFolder.getRoot(), "zipContents");
        assertNotNull(unZipped);
        List<String> result = lines(unZipped);
        assertEquals(expected, result);
    }

    @Test
    public void extractionOfEmptyZipThrowsException() {
        thrown.expect(ExtractionException.class);
        thrown.expectMessage("Failed to extract: " +
                "java.io.IOException: Negative seek offset");

        ZipExtractor extractor = new ZipExtractor(emptyZipFile, temporaryFolder.getRoot(), () -> "No Password");
        extractor.extract();
    }

}