package com.djrapitops.rom.util.file;

import net.lingala.zip4j.exception.ZipException;
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

public class ZipExtractorTest extends FileTest {

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
        zipFile = getFile("zip.zip");
        passwordZipFile = getFile("passworded_zip.zip");
        emptyZipFile = getFile("empty_zip.zip");
        contentsFile = getFile("zipContents");
    }

    @Test
    public void testExtraction() throws IOException, ZipException {
        List<String> expected = lines(contentsFile);

        ZipExtractor extractor = new ZipExtractor(zipFile, temporaryFolder.getRoot(), () -> "No Password");
        extractor.unzip();

        File unZipped = new File(temporaryFolder.getRoot(), "zipContents");
        assertNotNull(unZipped);
        List<String> result = lines(unZipped);
        assertEquals(expected, result);
    }

    @Test
    public void testExtractionEncrypted() throws IOException, ZipException {
        List<String> expected = lines(contentsFile);

        // Password for the zip file is 'password'
        ZipExtractor extractor = new ZipExtractor(passwordZipFile, temporaryFolder.getRoot(), () -> "password");
        extractor.unzip();

        File unZipped = new File(temporaryFolder.getRoot(), "zipContents");
        assertNotNull(unZipped);
        List<String> result = lines(unZipped);
        assertEquals(expected, result);
    }

    @Test
    public void extractionOfEmptyZipThrowsZipException() throws ZipException {
        thrown.expect(ZipException.class);

        ZipExtractor extractor = new ZipExtractor(emptyZipFile, temporaryFolder.getRoot(), () -> "No Password");
        extractor.unzip();
    }

}