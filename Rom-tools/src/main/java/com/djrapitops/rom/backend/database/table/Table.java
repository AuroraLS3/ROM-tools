package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryStatement;

/**
 * Abstract representation of a database table.
 *
 * @author Rsl1122
 */
public abstract class Table {

    protected final SQLDatabase db;
    protected final String tableName;

    /**
     * Constructor.
     *
     * @param db        SQLDatabase to use for queries and executes.
     * @param tableName Name of the table.
     */
    public Table(SQLDatabase db, String tableName) {
        this.db = db;
        this.tableName = tableName;
    }

    /**
     * Get a String that prevents ambiguous column names.
     *
     * @param table  Name of the table
     * @param column Name of the column
     * @return "table.column"
     */
    public static String getTableColumn(Table table, String column) {
        return table.tableName + "." + column;
    }

    /**
     * Call the methods for creating a table in the database.
     */
    public abstract void createTable();

    /**
     * Method for calling the create table SQL.
     *
     * @param sql SQL to create the table with.
     */
    protected void createTable(String sql) {
        db.createTable(sql);
    }

    /**
     * Query the table with a statement.
     *
     * @param query QueryStatement to use.
     * @param <T>   Type of the object returned by the Query.
     * @return The object returned by the Query.
     * @see QueryStatement
     */
    protected <T> T query(QueryStatement<T> query) {
        return db.query(query);
    }

    /**
     * Execute a statement.
     *
     * @param execute ExecuteStatement to execute.
     * @see ExecuteStatement
     */
    protected void execute(ExecuteStatement execute) {
        db.execute(execute);
    }

    /**
     * Execute a statement as a batch.
     *
     * @param execute ExecuteStatement to execute as a batch.
     * @see ExecuteStatement
     */
    protected void executeBatch(ExecuteStatement execute) {
        db.executeBatch(execute);
    }
}
