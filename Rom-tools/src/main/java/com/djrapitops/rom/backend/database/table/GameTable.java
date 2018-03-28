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
    public void createTable() throws BackendException {
        String sql = TableSQLParser.createTable(tableName)
                .primaryKeyIDColumn(Col.ID)
                .column(Col.NAME, "varchar(500)").notNull().unique()
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
    public int saveGame(Game game) throws BackendException {
        String sql = "REPLACE INTO " + tableName + " (" +
                Col.NAME +
                ") VALUES (?)";
        execute(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setString(1, game.getName());
            }
        });
        String selectSql = "SELECT " + Col.ID + " FROM " + tableName + " WHERE " + Col.NAME + "=?";
        return query(new QueryStatement<Integer>(selectSql, 1) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setString(1, game.getName());
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

    public Map<Integer, Game> getGames() throws BackendException {
        String sql = "SELECT * FROM " + tableName;

        return query(new QueryAllStatement<Map<Integer, Game>>(sql, 10000) {
            @Override
            public Map<Integer, Game> processResults(ResultSet set) throws SQLException {
                Map<Integer, Game> games = new HashMap<>();
                while (set.next()) {
                    int gameId = set.getInt(Col.ID);
                    String name = set.getString(Col.NAME);
                    games.put(gameId, new Game(name));
                }
                return games;
            }
        });
    }

    public static class Col {
        public static final String ID = "id";
        public static final String NAME = "name";
    }
}
