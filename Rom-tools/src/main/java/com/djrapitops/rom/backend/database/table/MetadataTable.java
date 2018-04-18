package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryAllStatement;
import com.djrapitops.rom.backend.database.sql.TableSQLParser;
import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.Metadata;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents 'metadata' table in the SQLDatabase.
 *
 * @author Rsl1122
 * @see SQLDatabase
 */
public class MetadataTable extends GameIDTable {

    public static final String TABLE_NAME = "metadata";

    public MetadataTable(SQLDatabase database) {
        super(database, TABLE_NAME);
    }

    @Override
    public void createTable() {
        String sql = TableSQLParser.createTable(tableName)
                .primaryKeyIDColumn(Col.ID)
                .column(Col.NAME, "varchar(500)").notNull()
                .column(Col.CONSOLE, "varchar(10)").notNull()
                .column(Col.GAME_ID, "integer").notNull()
                .foreignKey(Col.GAME_ID, GameTable.TABLE_NAME, GameTable.Col.ID)
                .toString();
        createTable(sql);
    }

    public void saveMetadata(int gameId, Metadata metadata) {
        String sql = "REPLACE INTO " + tableName + "(" +
                Col.GAME_ID + ", " +
                Col.NAME + ", " +
                Col.CONSOLE +
                ") VALUES (?, ?, ?)";

        execute(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setInt(1, gameId);
                statement.setString(2, metadata.getName());
                statement.setString(3, metadata.getConsole().name());
            }
        });
    }

    public Map<Integer, Metadata> getMetadata() {
        String sql = "SELECT * FROM " + tableName;

        return query(new QueryAllStatement<Map<Integer, Metadata>>(sql, 10000) {
            @Override
            public Map<Integer, Metadata> processResults(ResultSet set) throws SQLException {
                Map<Integer, Metadata> metadataMap = new HashMap<>();
                while (set.next()) {
                    int gameId = set.getInt(Col.GAME_ID);
                    String name = set.getString(Col.NAME);
                    Console console = Console.valueOf(set.getString(Col.CONSOLE));

                    Metadata metadata = Metadata.create()
                            .setName(name)
                            .setConsole(console)
                            .build();
                    metadataMap.put(gameId, metadata);
                }
                return metadataMap;
            }
        });
    }

    public void removeMetadata(List<Integer> gameIDs) {
        removeRelatedToIDs(gameIDs);
    }

    public static class Col {
        public static final String ID = "id";
        public static final String GAME_ID = GameIDTable.Col.GAME_ID;
        public static final String NAME = "name";
        public static final String CONSOLE = "console";

        private Col() {
            /* Should not be constructed */
        }
    }
}
