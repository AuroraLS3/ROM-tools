package com.djrapitops.rom.game;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class ConsoleTest {

    @Test
    @Ignore("Test ignored: Re-enable after Metadata is fetched")
    public void testAtari2600FileName() {
        Console expected = Console.ATARI_2600;
        File file = new File("Pac-Man (1982)(Atari, Tod Frye - Sears)(CX2646 -49-7943).bin");
        Console result = Console.resolveForFile(file);

        assertEquals(expected, result);
    }

    @Test
    public void testAtari7800FileName() {
        Console expected = Console.ATARI_7800;
        File file = new File("Ace of Aces.a78");
        Console result = Console.resolveForFile(file);

        assertEquals(expected, result);
    }

    @Test
    @Ignore("Test ignored: Re-enable after Metadata is fetched")
    public void testSegaCDFileName() {
        Console expected = Console.SEGA_CD;
        File file = new File("Sonic CD.bin");
        Console result = Console.resolveForFile(file);

        assertEquals(expected, result);
    }
}