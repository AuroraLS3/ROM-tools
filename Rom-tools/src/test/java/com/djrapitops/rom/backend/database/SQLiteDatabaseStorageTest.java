package com.djrapitops.rom.backend.database;

import com.djrapitops.rom.backend.Operations;
import com.djrapitops.rom.game.FileExtension;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import utils.fakeClasses.TestMetadata;

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
    public static void setUpClass() {
        File databaseFile = new File(temporaryFolder.getRoot(), "games.db");
        db = new SQLiteDatabase(databaseFile);
        db.open();
    }

    @AfterClass
    public static void tearDownClass() {
        db.close();
    }

    @Test
    public void savesGamesSuccessfully() {
        Game expected = createGame();
        db.save(Operations.GAME, expected);

        List<Game> games = db.fetch(Operations.ALL_GAMES);
        assertEquals("Game was not in database after save", 1, games.size());

        Game result = games.get(0);
        assertEquals(expected, result);
    }

    @Test
    public void databaseIsAtomicBetweenOpenCloseOpen() {
        Game expected = createGame();
        db.save(Operations.GAME, expected);

        List<Game> games = db.fetch(Operations.ALL_GAMES);
        assertEquals("Game was not in database after save", 1, games.size());

        Game result = games.get(0);
        assertEquals(expected, result);

        db.close();
        db.open();

        games = db.fetch(Operations.ALL_GAMES);
        assertEquals("Game was not in database after restart", 1, games.size());

        result = games.get(0);
        assertEquals(expected, result);
    }

    @Test
    public void removeGamesSuccessfully() {
        savesGamesSuccessfully();

        List<Game> singleton = Collections.singletonList(createGame());
        db.remove(Operations.ALL_GAMES, singleton);

        List<Game> games = db.fetch(Operations.ALL_GAMES);
        assertTrue(games.isEmpty());
    }

    private Game createGame() {
        Game game = new Game("TestGame");
        GameFile file = new GameFile(FileExtension.GB, "Example Path", "Hash");
        GameFile file2 = new GameFile(FileExtension.GB, "Example Path2", "Hash2");
        game.setGameFiles(Arrays.asList(file, file2));
        game.setMetadata(TestMetadata.createForTestGame());
        return game;
    }
}