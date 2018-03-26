package com.djrapitops.rom.backend.database;

import com.djrapitops.rom.exceptions.BackendException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SQLiteDatabaseTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

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

}