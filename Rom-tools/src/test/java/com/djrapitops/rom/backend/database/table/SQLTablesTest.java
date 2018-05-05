package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLiteDatabase;
import com.djrapitops.rom.exceptions.BackendException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;

public class SQLTablesTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();
    private static SQLiteDatabase db;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        File databaseFile = new File(temporaryFolder.getRoot(), "games.db");
        db = new SQLiteDatabase(databaseFile);
    }

    @AfterClass
    public static void tearDownClass() {
        db.close();
    }

    @Test
    public void createTablesDoesNotThrowExceptionByDefault() {
        SQLTables sqlTables = new SQLTables(db);
        // This test ensures that version is set properly in the future
        sqlTables.createTables();
    }

    @Test
    public void createTablesThrowsExceptionIfVersionIsNotUpdatedToNewestSchemaVersion() {
        thrown.expect(BackendException.class);
        thrown.expectMessage("Failed to update database schema version");

        SQLTables sqlTables = new SQLTables(db);

        // Set versionTable to a table that always returns wrong version
        sqlTables.versionTable = new VersionTable(db) {
            @Override
            public int getVersion() {
                return 0;
            }
        };

        // Throws
        sqlTables.createTables();
    }

}