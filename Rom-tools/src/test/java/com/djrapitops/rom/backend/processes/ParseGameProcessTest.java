package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.Metadata;
import com.djrapitops.rom.util.file.FileTest;
import org.junit.Test;

import java.io.File;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ParseGameProcessTest extends FileTest {

    @Test
    public void testAtari7800Game() throws Exception {
        File file = getFile("games" + File.separator + "Ace of Aces.a78");

        ParseGameProcess process = new ParseGameProcess(Collections.singletonList(file));

        Game game = process.call();
        Metadata metadata = game.getMetadata();

        assertEquals("Ace of Aces", game.getName());
        assertEquals("Ace of Aces", metadata.getName());
        assertEquals(Console.ATARI_7800, metadata.getConsole());
    }

    @Test
    public void testAtari2600Game() throws Exception {
        File file = getFile("games" + File.separator
                + "Pac-Man (1982) (Atari, Tod Frye - Sears) (CX2646 - 49-75185).bin"
        );

        ParseGameProcess process = new ParseGameProcess(Collections.singletonList(file));

        Game game = process.call();
        Metadata metadata = game.getMetadata();

        assertEquals("Pac-Man", game.getName());
        assertEquals("Pac-Man", metadata.getName());
        assertEquals(Console.ATARI_2600, metadata.getConsole());
    }

}