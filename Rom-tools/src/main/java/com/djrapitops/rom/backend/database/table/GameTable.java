package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryAllStatement;
import com.djrapitops.rom.backend.database.sql.QueryStatement;
import com.djrapitops.rom.backend.database.sql.TableSQLParser;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.Game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    public void createTable() {
        String sql = TableSQLParser.createTable(tableName)
                .primaryKeyIDColumn(Col.ID)
                .column(Col.METADATA_ID, "integer").notNull()
                .foreignKey(Col.METADATA_ID, MetadataTable.TABLE_NAME, MetadataTable.Col.ID)
                .toString();
        createTable(sql);
    }

    /**
     * Saves a game to the game table.
     *
     * @param game Game to save.
     * @return ID of the new column, or ID of the existing column with this name.
     * @throws BackendException If an error occurs executing or querying the database.
     */
    public int saveGame(Game game) {
        String sql = "INSERT INTO " + tableName + " (" +
                Col.METADATA_ID +
                ") VALUES (?)";
        execute(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setInt(1, game.getMetadataId());
            }
        });
        return getGameId(game);
    }

    /**
     * Used to get the GAME_ID of a specific game.
     *
     * @param game Game to search for.
     * @return GAME_ID or -1 if not found.
     */
    public int getGameId(Game game) {
        String selectSql = "SELECT " + Col.ID + " FROM " + tableName +
                " WHERE " + Col.METADATA_ID + "=?" +
                " ORDER BY " + Col.ID + " DESC LIMIT 1";
        return query(new QueryStatement<Integer>(selectSql, 1) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setInt(1, game.getMetadataId());
            }

            @Override
            public Integer processResults(ResultSet set) throws SQLException {
                if (set.next()) {
                    return set.getInt(Col.ID);
                }
                return -1;
            }
        });
    }

    /**
     * Get All games in the database.
     *
     * @return Map with ID, Game - key, value pair
     * @throws BackendException If operation fails.
     */
    public Map<Integer, Game> getGames() {
        String sql = "SELECT * FROM " + tableName;

        return query(new QueryAllStatement<Map<Integer, Game>>(sql, 10000) {
            @Override
            public Map<Integer, Game> processResults(ResultSet set) throws SQLException {
                Map<Integer, Game> games = new HashMap<>();
                while (set.next()) {
                    int gameId = set.getInt(Col.ID);
                    int metadataId = set.getInt(Col.METADATA_ID);
                    games.put(gameId, new Game(gameId, metadataId));
                }
                return games;
            }
        });
    }

    /**
     * Removes specific games from the database.
     *
     * @param games Games to remove.
     * @throws BackendException If the operation fails.
     */
    public void removeGames(Collection<Game> games) {
        String sql = "DELETE FROM " + tableName + " WHERE " + Col.ID + "=?";

        executeBatch(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                for (Game game : games) {
                    statement.setInt(1, game.getId());
                    statement.addBatch();
                }
            }
        });
    }

    /**
     * Class containing GameTable column names.
     */
    public static class Col {
        public static final String ID = "id";
        public static final String METADATA_ID = "metadata_id";

        private Col() {
            /* Should not be constructed */
        }
    }
}
