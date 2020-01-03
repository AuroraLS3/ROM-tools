package com.djrapitops.rom.game;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConsoleEnumTest {

    @Test
    @Ignore("Test ignored: Re-enable after Metadata is fetched")
    public void testAtari2600FileName() {
        Console expected = Consoles.findByName("Atari 2600").get();
        File file = new File("Pac-Man (1982)(Atari, Tod Frye - Sears)(CX2646 -49-7943).bin");
        List<Console> got = Consoles.findAllMatchingExtension(FileExtension.getFor(file));

        assertEquals(expected, got);
    }

    @Test
    public void testAtari7800FileName() {
        Console expected = Consoles.findAllMatchingExtension(FileExtension.A78).get(0);
        File file = new File("Ace of Aces.a78");
        Console result = Consoles.findAllMatchingExtension(FileExtension.getFor(file)).get(0);

        assertEquals(expected, result);
    }

    @Test
    @Ignore("Test ignored: Re-enable after Metadata is fetched")
    public void testSegaCDFileName() {
        Console expected = Consoles.findByName("Sega CD").get();
        File file = new File("Sonic CD.bin");
        List<Console> got = Consoles.findAllMatchingExtension(FileExtension.getFor(file));

        assertEquals(expected, got);
    }
}