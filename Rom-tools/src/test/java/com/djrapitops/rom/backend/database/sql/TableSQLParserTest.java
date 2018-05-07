package com.djrapitops.rom.backend.database.sql;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TableSQLParserTest {

    @Test
    public void createsValidSQL() {
        String expected = "CREATE TABLE IF NOT EXISTS test_table (" +
                "example varchar(16) NOT NULL UNIQUE DEFAULT 'Unknown', " +
                "id integer PRIMARY KEY, " +
                "foreign integer NOT NULL, " +
                "foreign_2 integer, " +
                "bool boolean DEFAULT '0', " +
                "FOREIGN KEY(foreign) REFERENCES table2(id), " +
                "FOREIGN KEY(foreign_2) REFERENCES table3(id)" +
                ")";
        String result = TableSQLParser.createTable("test_table")
                .column("example", "varchar(16)").notNull().unique().defaultValue("Unknown")
                .primaryKeyIDColumn("id")
                .column("foreign", "integer").notNull()
                .column("foreign_2", "integer")
                .column("bool", "boolean").defaultValue(false)
                .foreignKey("foreign", "table2", "id")
                .foreignKey("foreign_2", "table3", "id")
                .toString();
        assertEquals(expected, result);
    }

}