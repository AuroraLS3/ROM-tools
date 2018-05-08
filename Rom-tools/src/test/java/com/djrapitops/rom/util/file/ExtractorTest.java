package com.djrapitops.rom.util.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

class ExtractorTest extends FileTest {

    void testExtractionUnencrypted(List<String> expected, File archive, File destination) throws IOException {
        ArchiveExtractor extractor = ArchiveExtractor.createExtractorFor(
                archive, destination, () -> "No Password"
        );
        extractor.extract();

        File unZipped = new File(destination, "zipContents");
        assertNotNull(unZipped);
        List<String> result = lines(unZipped);
        assertEquals(expected, result);
    }

}