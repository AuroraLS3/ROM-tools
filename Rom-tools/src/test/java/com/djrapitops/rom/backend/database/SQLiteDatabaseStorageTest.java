package com.djrapitops.rom.backend.database;

import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.FileExtension;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import com.djrapitops.rom.game.Metadata;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SQLiteDatabaseStorageTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();
    private static SQLDatabase db;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() throws Exception {
        File databaseFile = new File(temporaryFolder.getRoot(), "games.db");
        db = new SQLiteDatabase(databaseFile);
        db.open();
    }

    @AfterClass
    public static void tearDownClass() {
        db.close();
    }

    @Test
    public void savesGamesSuccessfully() throws BackendException {
        Game expected = createGame();
        db.save().saveGame(expected);

        List<Game> games = db.fetch().getGames();
        assertEquals(1, games.size());

        Game result = games.get(0);
        assertEquals(expected, result);
    }

    @Test
    public void removeGamesSuccessfully() throws BackendException {
        savesGamesSuccessfully();

        db.remove().games(Collections.singleton(createGame()));

        List<Game> games = db.fetch().getGames();
        assertTrue(games.isEmpty());
    }

    private Game createGame() {
        Game game = new Game("Testgame");
        GameFile file = new GameFile(FileExtension.GB, "Example Path", "Hash");
        GameFile file2 = new GameFile(FileExtension.GB, "Example Path2", "Hash2");
        Metadata metadata = Metadata.create().setConsole(FileExtension.GB).setName("Testgame").build();
        game.setGameFiles(Arrays.asList(file, file2));
        game.setMetadata(metadata);
        return game;
    }
}