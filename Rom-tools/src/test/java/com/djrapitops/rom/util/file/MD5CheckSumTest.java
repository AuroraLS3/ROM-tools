package com.djrapitops.rom.util.file;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MD5CheckSumTest extends FileTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File sourceFile;

    @Before
    public void setUp() {
        sourceFile = getFile("zipContents");
    }

    @Test
    public void testCheckSum() throws IOException {
        // Expected Checksum generated on the internet.
        String expected = "22512B4CD7C2162FABBCB649A9E664F4".toLowerCase();
        String result = new MD5CheckSum(sourceFile).toHash();
        assertEquals(expected, result);
    }
}