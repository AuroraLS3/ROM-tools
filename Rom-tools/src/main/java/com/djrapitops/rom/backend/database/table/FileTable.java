package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryAllStatement;
import com.djrapitops.rom.backend.database.sql.QueryStatement;
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
    private static final String INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME + "(" +
            Col.GAME_ID + ", " +
            Col.EXTENSION + ", " +
            Col.FILE_PATH + ", " +
            Col.CHECKSUM +
            ") VALUES (?, ?, ?, ?)";

    public FileTable(SQLDatabase database) {
        super(database, TABLE_NAME);
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
        executeBatch(new ExecuteStatement(INSERT_STATEMENT) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                for (GameFile gameFile : files) {
                    statement.setInt(1, gameId);
                    statement.setString(2, gameFile.getExtension().name());
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
    public Map<Integer, List<GameFile>> getGameFiles() {
        String sql = "SELECT * FROM " + tableName;

        return query(new QueryAllStatement<Map<Integer, List<GameFile>>>(sql, 30000) {
            @Override
            public Map<Integer, List<GameFile>> processResults(ResultSet set) throws SQLException {
                Map<Integer, List<GameFile>> gameFileMap = new HashMap<>();
                while (set.next()) {
                    int gameId = set.getInt(Col.GAME_ID);
                    String extension = set.getString(Col.EXTENSION);
                    String path = set.getString(Col.FILE_PATH);
                    String checksum = set.getString(Col.CHECKSUM);

                    List<GameFile> paths = gameFileMap.getOrDefault(gameId, new ArrayList<>());
                    paths.add(new GameFile(FileExtension.valueOf(extension), path, checksum));
                    gameFileMap.put(gameId, paths);
                }
                return gameFileMap;
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

    /**
     * Check if the table contains given game files.
     *
     * @param gameFiles collection of GameFiles to check against.
     * @return true if a matching checksum is found from the database.
     */
    public boolean containsGame(Collection<GameFile> gameFiles) {
        String sql = "SELECT COUNT(*) as c FROM " + tableName +
                " WHERE " + Col.CHECKSUM + "=?" +
                " AND " + Col.FILE_PATH + "=?";

        for (GameFile gameFile : gameFiles) {
            if (query(new QueryStatement<Boolean>(sql, 100) {
                @Override
                public void prepare(PreparedStatement statement) throws SQLException {
                    statement.setString(1, gameFile.getHash());
                    statement.setString(2, gameFile.getAbsolutePath());
                }

                @Override
                public Boolean processResults(ResultSet set) throws SQLException {
                    set.next();
                    return set.getInt("c") > 0;
                }
            })) {
                return true;
            }
        }
        return false;
    }

    /**
     * Class containing FileTable column names.
     */
    public static class Col extends GameIDTable.Col {
        public static final String ID = "id";
        public static final String EXTENSION = "file_extension";
        public static final String FILE_PATH = "file_path";
        public static final String CHECKSUM = "md5_checksum";

        private Col() {
            /* Should not be constructed */
        }
    }
}
