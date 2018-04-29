package com.djrapitops.rom.backend.database;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.backend.Operations;
import com.djrapitops.rom.game.Game;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import utils.GameCreationUtility;
import utils.fakeClasses.DummyBackend;

import java.io.File;
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
        // Required for Log.log in Operations.ALL_GAMES.save
        DummyBackend backend = new DummyBackend();
        // Required for Operations.GAME.save(Game)
        backend.setGameStorage(db);
        Main.setBackend(backend);

        Game expected = createGame();
        db.save(Operations.ALL_GAMES, Collections.singletonList(expected));

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

        List<Game> saved = db.fetch(Operations.ALL_GAMES);
        db.remove(Operations.ALL_GAMES, saved);

        List<Game> games = db.fetch(Operations.ALL_GAMES);
        assertTrue(games.isEmpty());
    }

    private Game createGame() {
        return GameCreationUtility.createTestGameWithTwoFakeFiles();
    }
}