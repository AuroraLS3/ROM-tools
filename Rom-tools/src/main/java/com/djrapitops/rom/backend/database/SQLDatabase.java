package com.djrapitops.rom.backend.database;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.database.operations.SQLFetchOps;
import com.djrapitops.rom.backend.database.operations.SQLRemoveOps;
import com.djrapitops.rom.backend.database.operations.SQLSaveOps;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryStatement;
import com.djrapitops.rom.backend.database.table.SQLTables;
import com.djrapitops.rom.backend.operations.FetchOperations;
import com.djrapitops.rom.backend.operations.RemoveOperations;
import com.djrapitops.rom.backend.operations.SaveOperations;
import com.djrapitops.rom.exceptions.BackendException;

import java.sql.PreparedStatement;

/**
 * Represents a SQL Database that can store & retrieve items.
 *
 * @author Rsl1122
 */
public abstract class SQLDatabase implements GameBackend {

    protected SQLTables tables;
    private final SQLFetchOps fetchOps;
    private final SQLSaveOps saveOps;
    private final SQLRemoveOps removeOps;

    public SQLDatabase() {
        tables = new SQLTables(this);
        fetchOps = new SQLFetchOps(this);
        saveOps = new SQLSaveOps(this);
        removeOps = new SQLRemoveOps(this);
    }

    @Override
    public FetchOperations fetch() {
        return fetchOps;
    }

    @Override
    public SaveOperations save() {
        return saveOps;
    }

    @Override
    public RemoveOperations remove() {
        return removeOps;
    }

    /**
     * Executes an ExecuteStatement in the Database.
     *
     * @param statement Statement to execute.
     * @throws BackendException If the statement fails to execute.
     */
    public abstract void execute(ExecuteStatement statement) throws BackendException;

    /**
     * Executes an ExecuteStatement (batch) in the Database.
     *
     * @param statement Statement to execute.
     * @throws BackendException If the statement fails to execute.
     */
    public abstract void executeBatch(ExecuteStatement statement) throws BackendException;


    /**
     * Queries the database with the QueryStatement.
     *
     * @param statement Statement to query.
     * @param <T>       Return type of the Query
     * @return Result of the query
     * @throws BackendException If the statement fails to execute.
     */
    public abstract <T> T query(QueryStatement<T> statement) throws BackendException;

    public void createTable(String sql) throws BackendException {
        try {
            execute(new ExecuteStatement(sql) {
                @Override
                public void prepare(PreparedStatement statement) {
                    /* No preparing required */
                }
            });
        } catch (BackendException e) {
            throw new BackendException("Failed to create table", e.getCause());
        }
    }

    public abstract Tables getTables();
}
