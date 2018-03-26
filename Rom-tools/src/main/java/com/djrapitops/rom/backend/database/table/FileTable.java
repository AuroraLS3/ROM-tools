package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.TableSQLParser;
import com.djrapitops.rom.exceptions.BackendException;

/**
 * Represents 'files' table in the SQLDatabase.
 *
 * @author Rsl1122
 * @see SQLDatabase
 */
public class FileTable extends Table {

    public static final String TABLE_NAME = "files";

    public FileTable(SQLDatabase database) {
        super(database, TABLE_NAME);
    }

    @Override
    public void createTable() throws BackendException {
        String sql = TableSQLParser.createTable(tableName)
                .primaryKeyIDColumn(Col.ID)
                .column(Col.FILE_PATH, "varchar(8192)").notNull()
                .toString();
        createTable(sql);
    }

    public static class Col {
        public static final String ID = "id";
        public static final String FILE_PATH = "file_path";
    }
}
