package com.djrapitops.rom.game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConsoleTest {

    @Test
    public void testAtari2600FileName() {
        Console expected = Console.ATARI_2600;
        String fileName = "Pac-Man (1982)(Atari, Tod Frye - Sears)(CX2646 -49-7943).bin";
        Console result = Console.resolveFromFilename(fileName);

        assertEquals(expected, result);
    }

    @Test
    public void testAtari7800FileName() {
        Console expected = Console.ATARI_7800;
        String fileName = "Ace of Aces.a78";
        Console result = Console.resolveFromFilename(fileName);

        assertEquals(expected, result);
    }

    @Test
    public void testSegaCDFileName() {
        Console expected = Console.SEGA_CD;
        String fileName = "Sonic CD.bin";
        Console result = Console.resolveFromFilename(fileName);

        assertEquals(expected, result);
    }
}