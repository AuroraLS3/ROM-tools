package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryAllStatement;
import com.djrapitops.rom.backend.database.sql.TableSQLParser;
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
public class FileTable extends GameIDTable {

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
    public void createTable() {
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

    /**
     * Save a collection of game files.
     *
     * @param gameId Game ID in Games table.
     * @param files  Collection of GameFiles related to a single game.
     */
    public void saveGameFiles(int gameId, Collection<GameFile> files) {
        executeBatch(new ExecuteStatement(insestStatement) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                for (GameFile gameFile : files) {
                    statement.setInt(1, gameId);
                    statement.setString(2, gameFile.getExtension().getExtension());
                    statement.setString(3, gameFile.getAbsolutePath());
                    statement.setString(4, gameFile.getHash());
                    statement.addBatch();
                }
            }
        });
    }

    /**
     * Get all GameFiles.
     *
     * @return Map with ID, GameFile collection pairs
     */
    public Map<Integer, Set<GameFile>> getPaths() {
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

    /**
     * Removes GameFiles related to certain game IDs.
     *
     * @param gameIDs IDs of the Games' files to remove
     */
    public void removeFiles(List<Integer> gameIDs) {
        removeRelatedToIDs(gameIDs);
    }

    public static class Col {
        public static final String ID = "id";
        public static final String GAME_ID = GameIDTable.Col.GAME_ID;
        public static final String EXTENSION = "file_extension";
        public static final String FILE_PATH = "file_path";
        public static final String CHECKSUM = "md5_checksum";

        private Col() {
            /* Should not be constructed */
        }
    }
}
