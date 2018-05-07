package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.MainTestingVariables;
import com.djrapitops.rom.game.*;
import com.djrapitops.rom.util.file.FileTest;
import org.junit.Test;
import utils.fakeClasses.DummyBackend;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

public class ParseGameProcessTest extends FileTest {

    @Test
    public void atari7800Game() throws Exception {
        File file = getFile("games" + File.separator + "Ace of Aces.a78");

        Game game = GameParsing.parseGame(file);

        Metadata metadata = game.getMetadata();

        assertEquals("Ace of Aces", metadata.getName());
        assertEquals(Console.ATARI_7800, metadata.getConsole());
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
        assertEquals(Console.METADATA, metadata.getConsole());
    }

    @Test
    public void parseFolder() throws Exception {
        // Required for Log.log in GameParsing.parseGamesFromFiles
        MainTestingVariables.setBackend(new DummyBackend());

        File file = new File("src/test/resources/games");

        List<Game> games = GameParsing.parseGamesFromFile(file);

        assertEquals(Objects.requireNonNull(file.listFiles()).length, games.size());
    }

    @Test
    public void multiFileWorksForDisks1() {
        Collection<GameFile> files = Collections.singletonList(
                new GameFile(FileExtension.CUE, "any.cue", "")
        );
        assertTrue(GameParsing.hasDiskFormatFiles(files));
    }

    @Test
    public void multiFileWorksForDisks2() {
        Collection<GameFile> files = Collections.singletonList(
                new GameFile(FileExtension.BIN, "any.bin", "")
        );
        assertTrue(GameParsing.hasDiskFormatFiles(files));
    }

    @Test
    public void multiFileRegexWorks1() {
        Collection<GameFile> files = Arrays.asList(
                new GameFile(FileExtension.A26, "game (Chip 1).a26", ""),
                new GameFile(FileExtension.A26, "game (Chip 2).a26", "")
        );
        assertTrue(GameParsing.hasDiskFormatFiles(files));
    }

    @Test
    public void multiFileRegexWorks2() {
        // Normally these would be bin or cue files, but those are combined separately as well.
        Collection<GameFile> files = Arrays.asList(
                new GameFile(FileExtension.NDS, "game (Disk 1).nds", ""),
                new GameFile(FileExtension.NDS, "game (Disk 2).nds", "")
        );
        assertTrue(GameParsing.hasDiskFormatFiles(files));
    }

    @Test
    public void multiFileRegexWorks3() {
        // Normally these would be bin or cue files, but those are combined separately as well.
        Collection<GameFile> files = Arrays.asList(
                new GameFile(FileExtension.NDS, "game (Track 1).nds", ""),
                new GameFile(FileExtension.NDS, "game (Track 2).nds", "")
        );
        assertTrue(GameParsing.hasDiskFormatFiles(files));
    }

    @Test
    public void multiFileFalseWorks() {
        Collection<GameFile> files = Collections.singletonList(
                new GameFile(FileExtension.NDS, "game.nds", "")
        );
        assertFalse(GameParsing.hasDiskFormatFiles(files));
    }

}