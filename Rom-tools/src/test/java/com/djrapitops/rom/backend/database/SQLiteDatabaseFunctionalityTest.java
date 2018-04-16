package com.djrapitops.rom.backend.database;

import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryAllStatement;
import com.djrapitops.rom.backend.database.table.FileTable;
import com.djrapitops.rom.exceptions.BackendException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import utils.fakeClasses.ThrowingFile;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SQLiteDatabaseFunctionalityTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static File databaseFile;
    private static SQLDatabase db;

    @BeforeClass
    public static void setUpClass() {
        databaseFile = new File(temporaryFolder.getRoot(), "games.db");
        db = new SQLiteDatabase(databaseFile);
        db.open();
    }

    @AfterClass
    public static void tearDownClass() {
        db.close();
    }

    @Test
    public void databaseOpensWithoutErrors() {
        assertTrue(db.isOpen());
    }

    @Test
    public void failsToOpenWhenNoSQLiteClass() {
        thrown.expect(BackendException.class);
        thrown.expectMessage("Failed to open a new database connection");

        SQLDatabase db = new SQLiteDatabase(new ThrowingFile(temporaryFolder.getRoot(), "games.db"));
        db.open();
    }

    @Test
    public void executeSucceeds() {
        String sql = "INSERT INTO " + FileTable.TABLE_NAME + " (" +
                FileTable.Col.GAME_ID + ", " +
                FileTable.Col.CHECKSUM + ", " +
                FileTable.Col.FILE_PATH + ", " +
                FileTable.Col.EXTENSION +
                ") VALUES (?, ?, ?, ?)";
        String expected = "Test";

        db.execute(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setInt(1, 1);
                statement.setString(2, "not expected");
                statement.setString(3, expected);
                statement.setString(4, "not expected");
            }
        });

        String result = db.query(new QueryAllStatement<String>("SELECT * FROM " + FileTable.TABLE_NAME) {
            @Override
            public String processResults(ResultSet set) throws SQLException {
                return set.getString(FileTable.Col.FILE_PATH);
            }
        });
        assertEquals(expected, result);
    }

    @Test
    public void executeBatchSucceeds() {
        String sql = "INSERT INTO " + FileTable.TABLE_NAME + " (" +
                FileTable.Col.GAME_ID + ", " +
                FileTable.Col.CHECKSUM + ", " +
                FileTable.Col.FILE_PATH + ", " +
                FileTable.Col.EXTENSION +
                ") VALUES (?, ?, ?, ?)";
        String expected = "Test";

        db.executeBatch(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setInt(1, 1);
                statement.setString(2, "not expected");
                statement.setString(3, expected);
                statement.setString(4, "not expected");
                statement.addBatch();
            }
        });

        String result = db.query(new QueryAllStatement<String>("SELECT * FROM " + FileTable.TABLE_NAME) {
            @Override
            public String processResults(ResultSet set) throws SQLException {
                return set.getString(FileTable.Col.FILE_PATH);
            }
        });
        assertEquals(expected, result);
    }

    @Test
    public void executeFailsOnBadSQL() {
        String sql = "INSERT INTO " + FileTable.TABLE_NAME + " (" +
                FileTable.Col.FILE_PATH +
                ") VALUES (?)";

        thrown.expect(BackendException.class);
        thrown.expectMessage("Failed to execute statement: " + sql);

        db.execute(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) {
            }
        });
    }

    @Test
    public void executeBatchFailsOnBadSQL() {
        String sql = "INSERT INTO " + FileTable.TABLE_NAME + " (" +
                FileTable.Col.FILE_PATH +
                ") VALUES (?)";

        thrown.expect(BackendException.class);
        thrown.expectMessage("Failed to execute batch statement: " + sql);

        db.executeBatch(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.addBatch();
            }
        });
    }

    @Test
    public void queryFailsOnBadSQL() {
        String sql = "INSERT INTO " + FileTable.TABLE_NAME + " (" +
                FileTable.Col.FILE_PATH +
                ") VALUES (?)";

        thrown.expect(BackendException.class);
        thrown.expectMessage("Failed to query statement: " + sql);

        db.query(new QueryAllStatement<String>(sql) {
            @Override
            public String processResults(ResultSet set) throws SQLException {
                return set.getString(FileTable.Col.FILE_PATH);
            }
        });
    }


}