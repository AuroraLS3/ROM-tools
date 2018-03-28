package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryAllStatement;
import com.djrapitops.rom.backend.database.sql.TableSQLParser;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.FileExtension;
import com.djrapitops.rom.game.GameFile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Represents 'files' table in the SQLDatabase.
 *
 * @author Rsl1122
 * @see SQLDatabase
 */
public class FileTable extends Table {

    public static final String TABLE_NAME = "files";
    private String insestStatement;

    public FileTable(SQLDatabase database) {
        super(database, TABLE_NAME);
        insestStatement = "INSERT INTO " + tableName + "(" +
                Col.GAME_ID + ", " +
                Col.EXTENSION + ", " +
                Col.FILE_PATH + ", " +
                Col.CHECKSUM +
                ") VALUES (?, ?, ?, ?)";
    }

    @Override
    public void createTable() throws BackendException {
        String sql = TableSQLParser.createTable(tableName)
                .primaryKeyIDColumn(Col.ID)
                .column(Col.EXTENSION, "varchar(20)").notNull()
                .column(Col.FILE_PATH, "varchar(8192)").notNull()
                .column(Col.CHECKSUM, "varchar(128)").notNull()
                .column(Col.GAME_ID, "integer").notNull()
                .foreignKey(Col.GAME_ID, GameTable.TABLE_NAME, GameTable.Col.ID)
                .toString();
        createTable(sql);
    }

    public void saveGameFiles(int gameId, Collection<GameFile> paths) throws BackendException {
        executeBatch(new ExecuteStatement(insestStatement) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                for (GameFile gameFile : paths) {
                    statement.setInt(1, gameId);
                    statement.setString(2, gameFile.getExtension().getExtension());
                    statement.setString(3, gameFile.getAbsolutePath());
                    statement.setString(4, gameFile.getHash());
                    statement.addBatch();
                }
            }
        });
    }

    public Map<Integer, Set<GameFile>> getPaths() throws BackendException {
        String sql = "SELECT * FROM " + tableName;

        return query(new QueryAllStatement<Map<Integer, Set<GameFile>>>(sql, 30000) {
            @Override
            public Map<Integer, Set<GameFile>> processResults(ResultSet set) throws SQLException {
                Map<Integer, Set<GameFile>> pathMap = new HashMap<>();
                while (set.next()) {
                    int gameId = set.getInt(Col.GAME_ID);
                    String extension = set.getString(Col.EXTENSION);
                    String path = set.getString(Col.FILE_PATH);
                    String checksum = set.getString(Col.CHECKSUM);

                    Set<GameFile> paths = pathMap.getOrDefault(gameId, new HashSet<>());
                    paths.add(new GameFile(FileExtension.getExtensionFor(extension), path, checksum));
                    pathMap.put(gameId, paths);
                }
                return pathMap;
            }
        });
    }

    public static class Col {
        public static final String ID = "id";
        public static final String GAME_ID = "game_id";
        public static final String EXTENSION = "file_extension";
        public static final String FILE_PATH = "file_path";
        public static final String CHECKSUM = "md5_checksum";
    }
}
