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
        sourceFile = getFile("games/Ace of Aces.a78");
    }

    @Test
    public void testCheckSum() throws IOException {
        // Expected Checksum generated on the internet.
        String expected = "4dd96d99c1228bb1fcae8c1b5c239197".toLowerCase();
        String result = new MD5CheckSum(sourceFile).toHash();
        assertEquals(expected, result);
    }
}