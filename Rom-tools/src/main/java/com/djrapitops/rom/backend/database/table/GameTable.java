package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.TableSQLParser;
import com.djrapitops.rom.exceptions.BackendException;

/**
 * Represents 'games' table in the SQLDatabase.
 *
 * @author Rsl1122
 * @see SQLDatabase
 */
public class GameTable extends Table {

    public static final String TABLE_NAME = "games";

    public GameTable(SQLDatabase database) {
        super(database, TABLE_NAME);
    }

    @Override
    public void createTable() throws BackendException {
        String sql = TableSQLParser.createTable(tableName)
                .primaryKeyIDColumn(Col.ID)
                .column(Col.METADATA_ID, "integer").notNull()
                .foreignKey(Col.METADATA_ID, MetadataTable.TABLE_NAME, MetadataTable.Col.ID)
                .toString();
        createTable(sql);
    }

    public static class Col {
        public static final String ID = "id";
        public static final String METADATA_ID = "metadata_id";
    }
}
