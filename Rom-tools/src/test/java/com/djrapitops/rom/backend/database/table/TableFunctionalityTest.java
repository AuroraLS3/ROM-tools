package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.Operations;
import com.djrapitops.rom.backend.database.SQLiteDatabase;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import utils.GameCreationUtility;
import utils.MetadataCreationUtility;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;

public class TableFunctionalityTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();
    private static SQLiteDatabase db;

    @BeforeClass
    public static void setUpClass() {
        File databaseFile = new File(temporaryFolder.getRoot(), "games.db");
        db = new SQLiteDatabase(databaseFile);
    }

    @AfterClass
    public static void tearDownClass() {
        db.close();
    }

    @Before
    public void setUp() {
        new SQLTables(db).createTables();
        db.remove(Operations.ALL_GAMES, db.fetch(Operations.ALL_GAMES));
    }

    @Test
    public void fileTableDoesNotFindNonExistentGame() throws IOException {
        FileTable testedTable = new FileTable(db);

        Game game = GameCreationUtility.createGameWithCorrectFileHash(temporaryFolder.newFile());
        Collection<GameFile> gameFiles = game.getGameFiles();
        assertFalse(testedTable.containsGame(gameFiles));
    }

    @Test
    public void fileTableFindsExistingGame() throws IOException {
        FileTable testedTable = new FileTable(db);

        Game game = GameCreationUtility.createGameWithCorrectFileHash(temporaryFolder.newFile());
        db.save(Operations.GAME, game);

        Collection<GameFile> gameFiles = game.getGameFiles();
        assertTrue(testedTable.containsGame(gameFiles));
    }

    @Test
    public void gameTableDoesNotFindNonExistentGameID() throws IOException {
        GameTable testedTable = new GameTable(db);

        Game game = GameCreationUtility.createGameWithCorrectFileHash(temporaryFolder.newFile());
        game.setMetadataId(-1);
        assertEquals(-1, testedTable.getGameId(game));
    }

    @Test
    public void gameTableFindsExistingGameID() throws IOException {
        GameTable testedTable = new GameTable(db);

        Game game = GameCreationUtility.createGameWithCorrectFileHash(temporaryFolder.newFile());
        db.save(Operations.GAME, game);

        assertEquals(1, testedTable.getGameId(game));
    }

    @Test
    public void metadataTableDoesNotFindNonExistentMetadataID() {
        MetadataTable testedTable = new MetadataTable(db);

        assertEquals(-1, testedTable.getMetadataId(MetadataCreationUtility.createNonExistent()));
    }

    @Test
    public void metadataTableFindsExistingMetadataID() throws IOException {
        MetadataTable testedTable = new MetadataTable(db);

        Game game = GameCreationUtility.createGameWithCorrectFileHash(temporaryFolder.newFile());
        db.save(Operations.GAME, game);

        assertEquals(1, testedTable.getMetadataId(MetadataCreationUtility.createForTestGame()));
    }

    @Test
    public void versionTableReturnsSetVersion() {
        VersionTable testedTable = new VersionTable(db);

        assertEquals(1, testedTable.getVersion());
        testedTable.setVersion(2);
        assertEquals(2, testedTable.getVersion());
    }
}