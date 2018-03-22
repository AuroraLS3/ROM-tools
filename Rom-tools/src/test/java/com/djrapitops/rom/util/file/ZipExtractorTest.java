package com.djrapitops.rom.util.file;

import net.lingala.zip4j.exception.ZipException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ZipExtractorTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File sourceFile;
    private File contentsFile;

    private File getFile(String resourceName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        assertNotNull(file);
        return file;
    }

    private List<String> lines(File file) throws IOException {
        try (Stream<String> lines = Files.lines(file.toPath())) {
            return lines.collect(Collectors.toList());
        }
    }

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