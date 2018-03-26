package com.djrapitops.rom.backend.database;

import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryStatement;
import com.djrapitops.rom.backend.database.table.SQLTables;
import com.djrapitops.rom.backend.operations.FetchOperations;
import com.djrapitops.rom.backend.operations.SaveOperations;
import com.djrapitops.rom.exceptions.BackendException;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Represents a SQLite based GameBackend.
 *
 * @author Rsl1122
 */
public class SQLiteDatabase extends SQLDatabase {

    private Connection connection;

    private final File databaseFile;

    private boolean open;

    public SQLiteDatabase() {
        this(new File("games.db"));
    }

    public SQLiteDatabase(File databaseFile) {
        this.databaseFile = databaseFile;
        this.open = false;
        tables = new SQLTables(this);
    }

    @Override
    public void execute(ExecuteStatement statement) throws BackendException {
        try {
            statement.execute(getConnection().prepareStatement(statement.getSql()));
        } catch (SQLException e) {
            throw new BackendException("Failed to execute statement: " + statement.getSql(), e);
        }
    }

    @Override
    public void executeBatch(ExecuteStatement statement) throws BackendException {
        try {
            statement.executeBatch(getConnection().prepareStatement(statement.getSql()));
        } catch (SQLException e) {
            throw new BackendException("Failed to execute batch statement: " + statement.getSql(), e);
        }
    }

    @Override
    public <T> T query(QueryStatement<T> statement) throws BackendException {
        try {
            return statement.executeQuery(connection.prepareStatement(statement.getSql()));
        } catch (SQLException e) {
            throw new BackendException("Failed to query statement: " + statement.getSql(), e);
        }
    }

    @Override
    public SaveOperations save() {
        return null; // TODO SQLSaveOperations
    }

    @Override
    public FetchOperations fetch() {
        return null; // TODO SQLFetchOperations
    }

    @Override
    public void open() throws BackendException {
        connection = getConnection();
        tables.createTables();
        open = true;
    }

    @Override
    public boolean isOpen() {
        return tables != null && open;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException ignored) {
            /* Ignored, closing */
        } finally {
            open = false;
        }
    }

    private Connection getConnection() throws BackendException {
        if (connection == null) {
            connection = getNewConnection();
        }
        return connection;
    }

    private Connection getNewConnection() throws BackendException {
        try {
            Class.forName("org.sqlite.JDBC");

            String dbFilePath = databaseFile.getAbsolutePath();

            Connection newConnection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath + "?journal_mode=WAL");
            newConnection.setAutoCommit(false);

            return newConnection;
        } catch (ClassNotFoundException | SQLException | RuntimeException e) {
            throw new BackendException("Failed to open a new database connection", e);
        }
    }

    @Override
    public SQLTables getTables() {
        return tables;
    }
}
