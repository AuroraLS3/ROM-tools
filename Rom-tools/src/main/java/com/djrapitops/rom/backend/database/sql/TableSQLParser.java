package com.djrapitops.rom.backend.database.sql;

/**
 * SQLParser Class for parsing table creation, removal and modification statements.
 *
 * @author Rsl1122
 */
public class TableSQLParser extends SQLParser {

    private int columns = 0;

    /**
     * Constructor for builder with initial value.
     *
     * @param start String to initialize the builder with.
     */
    private TableSQLParser(String start) {
        super(start);
    }

    /**
     * Creates a new TableSQLParser for creating tables.
     *
     * @param tableName name of the table to create.
     * @return a new instance of TableSQLParser.
     */
    public static TableSQLParser createTable(String tableName) {
        return new TableSQLParser("CREATE TABLE IF NOT EXISTS " + tableName + " (");
    }

    /**
     * Adds a column to the table that is being created.
     *
     * @param column name of the column to add.
     * @param type   SQL type of the column to add (integer, bigint, varchar(x) etc.)
     * @return The object which method was called.
     */
    public TableSQLParser column(String column, String type) {
        if (columns > 0) {
            append(", ");
        }
        append(column).addSpace();
        append(type);

        columns++;
        return this;
    }

    /**
     * Makes a column that was added previously a foreign key.
     *
     * @param column           name of the column to make into a foreign key.
     * @param referencedTable  name of the table the referenced column is in.
     * @param referencedColumn name of the referenced column.
     * @return The object which method was called.
     */
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

    /**
     * Add a NOT NULL constraint to a column.
     *
     * @return The object which method was called.
     */
    public TableSQLParser notNull() {
        addSpace();
        append("NOT NULL");
        return this;
    }

    /**
     * Add a UNIQUE constraint to a column.
     *
     * @return The object which method was called.
     */
    public TableSQLParser unique() {
        addSpace();
        append("UNIQUE");
        return this;
    }

    /**
     * Add a DEFAULT boolean value to a column.
     *
     * @param value value to set as default.
     * @return The object which method was called.
     */
    public TableSQLParser defaultValue(boolean value) {
        return defaultValue(value ? "1" : "0");
    }

    /**
     * Add a DEFAULt value to a column.
     *
     * @param value value to set as default.
     * @return The object which method was called.
     */
    public TableSQLParser defaultValue(String value) {
        addSpace();
        append("DEFAULT '").append(value).append("'");
        return this;
    }

    /**
     * Add a PRIMARY KEY column.
     *
     * @param column name of the column.
     * @return The object which method was called.
     */
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