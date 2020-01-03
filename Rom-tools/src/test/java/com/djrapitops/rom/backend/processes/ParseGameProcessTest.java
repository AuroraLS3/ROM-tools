package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.MainTestingVariables;
import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.Consoles;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.Metadata;
import com.djrapitops.rom.util.file.FileTest;
import org.junit.Test;
import utils.fakeClasses.DummyBackend;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParseGameProcessTest extends FileTest {

    @Test
    public void atari7800Game() throws Exception {
        File file = getFile("games" + File.separator + "Ace of Aces.a78");

        Game game = GameParsing.parseGame(file);

        Metadata metadata = game.getMetadata();

        assertEquals("Ace of Aces", metadata.getName());
        Optional<Console> expected = Consoles.findByName("Atari 7800");
        Optional<Console> result = metadata.getConsole();
        assertTrue(expected.isPresent());
        assertTrue(result.isPresent());
        assertEquals(expected.get(), result.get());
    }

    @Test
    public void atari2600Game() throws Exception {
        File file = getFile("games" + File.separator
                + "Pac-Man (1982) (Atari, Tod Frye - Sears) (CX2646 - 49-75185).bin"
        );

        Game game = GameParsing.parseGame(file);

        Metadata metadata = game.getMetadata();

        assertEquals("Pac-Man", metadata.getName());
        // TODO Change back to ATARI_2600 after metadata is fetched
        assertEquals(Optional.empty(), metadata.getConsole());
    }

    @Test
    public void parseFolder() throws Exception {
        // Required for Log.log in GameParsing.parseGamesFromFiles
        MainTestingVariables.setBackend(new DummyBackend());

        File file = new File("src/test/resources/games");

        List<Game> games = GameParsing.parseGamesFromFile(file);

        assertEquals(Objects.requireNonNull(file.listFiles()).length, games.size());
    }
}