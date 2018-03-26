package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.TableSQLParser;
import com.djrapitops.rom.exceptions.BackendException;

/**
 * Represents 'metadata' table in the SQLDatabase.
 *
 * @author Rsl1122
 * @see SQLDatabase
 */
public class MetadataTable extends Table {

    public static final String TABLE_NAME = "metadata";

    public MetadataTable(SQLDatabase database) {
        super(database, TABLE_NAME);
    }

    @Override
    public void createTable() throws BackendException {
        String sql = TableSQLParser.createTable(tableName)
                .primaryKeyIDColumn(Col.ID)
                .column(Col.NAME, "varchar(500)").notNull()
                .toString();
        createTable(sql);
    }

    public static class Col {
        public static final String ID = "id";
        public static final String NAME = "name";
    }
}
