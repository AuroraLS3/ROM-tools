package com.djrapitops.rom.backend.database.sql;

/**
 * SQLParser Class for parsing table creation, removal and modification statements.
 *
 * @author Rsl1122
 */
public class TableSQLParser extends SQLParser {

    private int columns = 0;

    public TableSQLParser() {
        this("");
    }

    public TableSQLParser(String start) {
        super(start);
    }

    public static TableSQLParser createTable(String tableName) {
        return new TableSQLParser("CREATE TABLE IF NOT EXISTS " + tableName + " (");
    }

    public static String dropTable(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    /**
     * Used for ALTER TABLE sql statements.
     *
     * @param column column to modify
     * @return TableSQLParser object
     */
    public static TableSQLParser newColumn(String column, String type) {
        return new TableSQLParser().column(column, type);
    }


    public TableSQLParser column(String column, String type) {
        if (columns > 0) {
            append(", ");
        }
        append(column).addSpace();
        append(type);

        columns++;
        return this;
    }


    public TableSQLParser foreignKey(String column, String referencedTable, String referencedColumn) {
        if (columns > 0) {
            append(", ");
        }
        append("FOREIGN KEY(")
                .append(column)
                .append(") REFERENCES ")
                .append(referencedTable)
                .append("(")
                .append(referencedColumn)
                .append(")");
        columns++;
        return this;
    }

    public TableSQLParser notNull() {
        addSpace();
        append("NOT NULL");
        return this;
    }

    public TableSQLParser unique() {
        addSpace();
        append("UNIQUE");
        return this;
    }

    public TableSQLParser defaultValue(boolean value) {
        return defaultValue(value ? "1" : "0");
    }

    public TableSQLParser defaultValue(String value) {
        addSpace();
        append("DEFAULT ").append(value);
        return this;
    }


    public TableSQLParser primaryKeyIDColumn(String column) {
        if (columns > 0) {
            append(", ");
        }
        append(column).addSpace();
        append("integer").addSpace();
        append("PRIMARY KEY");
        columns++;
        return this;
    }

    @Override
    public String toString() {
        append(")");
        return super.toString();
    }
}