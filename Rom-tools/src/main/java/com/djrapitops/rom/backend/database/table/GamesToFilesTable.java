package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.TableSQLParser;
import com.djrapitops.rom.exceptions.BackendException;

/**
 * Represents 'files_games' table in the SQLDatabase.
 *
 * @author Rsl1122
 * @see SQLDatabase
 */
public class GamesToFilesTable extends Table {

    public static final String TABLE_NAME = "files_games";


    public GamesToFilesTable(SQLDatabase database) {
        super(database, TABLE_NAME);
    }

    @Override
    public void createTable() throws BackendException {
        String sql = TableSQLParser.createTable(tableName)
                .primaryKeyIDColumn(Col.ID)
                .column(Col.FILE_ID, "integer").notNull()
                .column(Col.GAME_ID, "integer").notNull()
                .foreignKey(Col.FILE_ID, FileTable.TABLE_NAME, FileTable.Col.ID)
                .foreignKey(Col.GAME_ID, GameTable.TABLE_NAME, GameTable.Col.ID)
                .toString();
        createTable(sql);
    }

    public static class Col {
        public static final String ID = "id";
        public static final String FILE_ID = "file_id";
        public static final String GAME_ID = "game_id";
    }
}
