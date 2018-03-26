package com.djrapitops.rom.backend.database;

import com.djrapitops.rom.exceptions.BackendException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import utils.fakeClasses.ThrowingFile;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SQLiteDatabaseTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private File databaseFile;

    @Before
    public void setUp() {
        databaseFile = new File(temporaryFolder.getRoot(), "games.db");
    }

    @Test
    public void databaseOpensWithoutErrors() throws BackendException {
        SQLDatabase db = new SQLiteDatabase(databaseFile);
        db.open();
        assertTrue(db.isOpen());
        db.close();
        assertFalse(db.isOpen());
    }

    @Test
    public void failsToOpenWhenNoSQLiteClass() throws BackendException {
        thrown.expect(BackendException.class);
        thrown.expectMessage("Failed to open a new database connection");

        SQLDatabase db = new SQLiteDatabase(new ThrowingFile(temporaryFolder.getRoot(), "games.db"));
        db.open();
    }

}