package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Contains common code for tables with Game IDs.
 *
 * @author Rsl1122
 */
public abstract class GameIDTable extends Table {

    public GameIDTable(SQLDatabase db, String tableName) {
        super(db, tableName);
    }

    /**
     * Method that removes items from the table where a GAME_ID matches.
     *
     * @param gameIDs GAME_IDs to remove.
     */
    protected void removeRelatedToIDs(List<Integer> gameIDs) {
        String sql = "DELETE FROM " + tableName + " WHERE " + Col.GAME_ID + "=?";

        executeBatch(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                for (Integer gameId : gameIDs) {
                    statement.setInt(1, gameId);
                    statement.addBatch();
                }
            }
        });
    }

    /**
     * Class that contains column names for a GameIDTable.
     * <p>
     * Classes extending GameIDTable that have a Col class should extend this class.
     */
    public static class Col {
        public static final String GAME_ID = "game_id";

        Col() {
            /* Hides constructor */
        }
    }

}