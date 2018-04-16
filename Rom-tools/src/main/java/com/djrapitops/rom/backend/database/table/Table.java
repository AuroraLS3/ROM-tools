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

    public Table(SQLDatabase db, String tableName) {
        this.db = db;
        this.tableName = tableName;
    }

    public static String getTableColumn(Table table, String column) {
        return table.tableName + "." + column;
    }

    public abstract void createTable();

    protected void createTable(String sql) {
        db.createTable(sql);
    }

    protected <T> T query(QueryStatement<T> query) {
        return db.query(query);
    }

    protected void execute(ExecuteStatement execute) {
        db.execute(execute);
    }

    protected void executeBatch(ExecuteStatement execute) {
        db.executeBatch(execute);
    }
}
