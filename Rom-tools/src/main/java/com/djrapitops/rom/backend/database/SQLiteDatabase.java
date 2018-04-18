package com.djrapitops.rom.backend.database;

import com.djrapitops.rom.backend.Log;
import com.djrapitops.rom.backend.Operation;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryStatement;
import com.djrapitops.rom.backend.database.table.SQLTables;
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
    }

    @Override
    public void execute(ExecuteStatement statement) {
        Log.log("DB Execute: " + statement.getSql());
        try {
            statement.execute(getConnection().prepareStatement(statement.getSql()));
        } catch (SQLException e) {
            throw new BackendException("Failed to execute statement: " + statement.getSql(), e);
        }
    }

    @Override
    public void executeBatch(ExecuteStatement statement) {
        Log.log("DB Execute Batch: " + statement.getSql());
        try {
            statement.executeBatch(getConnection().prepareStatement(statement.getSql()));
        } catch (SQLException e) {
            throw new BackendException("Failed to execute batch statement: " + statement.getSql(), e);
        }
    }

    @Override
    public <T> T query(QueryStatement<T> statement) {
        Log.log("DB Query: " + statement.getSql());
        try {
            return statement.executeQuery(connection.prepareStatement(statement.getSql()));
        } catch (SQLException e) {
            throw new BackendException("Failed to query statement: " + statement.getSql(), e);
        }
    }

    @Override
    public void open() {
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
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignored) {
            /* Ignored, closing */
        } finally {
            open = false;
            connection = null;
        }
    }

    private Connection getConnection() {
        if (connection == null) {
            connection = getNewConnection();
        }
        return connection;
    }

    private Connection getNewConnection() {
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

    @Override
    public <T> void save(Operation<T> op, T obj) {
        super.save(op, obj);
        commitChanges();
    }

    @Override
    public <T> void remove(Operation<T> op, T obj) {
        super.remove(op, obj);
        commitChanges();
    }

    private void commitChanges() {
        // Ensures atomicity because autoCommit is false.
        try {
            getConnection().commit();
        } catch (SQLException e) {
            throw new BackendException("Failed to commit to database", e);
        }
    }

}
