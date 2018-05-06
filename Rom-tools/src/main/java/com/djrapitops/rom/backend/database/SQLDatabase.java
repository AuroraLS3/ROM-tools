package com.djrapitops.rom.backend.database;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.Operation;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryStatement;
import com.djrapitops.rom.backend.database.table.SQLTables;
import com.djrapitops.rom.exceptions.BackendException;

import java.sql.PreparedStatement;

/**
 * Represents a SQL Database that can store & retrieve items.
 *
 * @author Rsl1122
 */
public abstract class SQLDatabase implements GameBackend {

    protected final Tables tables;

    /**
     * Constructor that initializes SQLTables.
     *
     * @see SQLTables
     */
    public SQLDatabase() {
        tables = new SQLTables(this);
    }

    @Override
    public <T> void save(Operation<T> op, T obj) {
        op.getDao().add(tables, obj);
    }

    @Override
    public <T> T fetch(Operation<T> op) {
        return op.getDao().get(tables, op.getFilter());
    }

    @Override
    public <T> void remove(Operation<T> op, T obj) {
        op.getDao().remove(tables, obj);
    }

    /**
     * Executes an ExecuteStatement in the Database.
     *
     * @param statement Statement to execute.
     * @throws BackendException If the statement fails to execute.
     */
    public abstract void execute(ExecuteStatement statement);

    /**
     * Executes an ExecuteStatement (batch) in the Database.
     *
     * @param statement Statement to execute.
     * @throws BackendException If the statement fails to execute.
     */
    public abstract void executeBatch(ExecuteStatement statement);

    /**
     * Queries the database with the QueryStatement.
     *
     * @param statement Statement to query.
     * @param <T>       Return type of the Query
     * @return Result of the query
     * @throws BackendException If the statement fails to execute.
     */
    public abstract <T> T query(QueryStatement<T> statement);

    /**
     * Create a new table using the SQL string.
     *
     * @param sql SQL for creating the table.
     * @throws BackendException If there is a syntax error in the SQL.
     */
    public void createTable(String sql) {
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
}
