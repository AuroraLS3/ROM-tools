package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryAllStatement;
import com.djrapitops.rom.backend.database.sql.QueryStatement;
import com.djrapitops.rom.backend.database.sql.TableSQLParser;
import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.Metadata;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents 'metadata' table in the SQLDatabase.
 *
 * @author Rsl1122
 * @see SQLDatabase
 */
public class MetadataTable extends Table {

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
                .toString();
        createTable(sql);
    }

    /**
     * Used to save Metadata to the table.
     *
     * @param metadata Metadata to save.
     * @return ID of the new saved metadata.
     */
    public int saveMetadata(Metadata metadata) {
        String sql = "REPLACE INTO " + tableName + "(" +
                Col.NAME + ", " +
                Col.CONSOLE +
                ") VALUES (?, ?)";

        execute(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setString(1, metadata.getName());
                statement.setString(2, metadata.getConsole().name());
            }
        });

        return getMetadataId(metadata);
    }

    /**
     * Used to get the ID of a specific Metadata object.
     *
     * @param metadata Object to search for.
     * @return ID or -1 if not found.
     */
    public int getMetadataId(Metadata metadata) {
        String selectSql = "SELECT " + Col.ID + " FROM " + tableName +
                " WHERE " + Col.NAME + "=? AND " + Col.CONSOLE + "=?" +
                " ORDER BY " + GameTable.Col.ID + " DESC LIMIT 1";
        return query(new QueryStatement<Integer>(selectSql, 1) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setString(1, metadata.getName());
                statement.setString(2, metadata.getConsole().name());
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
     * Used to get a all Metadata in the table.
     *
     * @return Map with ID-Metadata key-value pairs.
     */
    public Map<Integer, Metadata> getMetadata() {
        String sql = "SELECT * FROM " + tableName;

        return query(new QueryAllStatement<Map<Integer, Metadata>>(sql, 10000) {
            @Override
            public Map<Integer, Metadata> processResults(ResultSet set) throws SQLException {
                Map<Integer, Metadata> metadataMap = new HashMap<>();
                while (set.next()) {
                    int metadataId = set.getInt(Col.ID);
                    String name = set.getString(Col.NAME);
                    Console console = Console.valueOf(set.getString(Col.CONSOLE));

                    Metadata metadata = Metadata.create()
                            .setName(name)
                            .setConsole(console)
                            .build();
                    metadataMap.put(metadataId, metadata);
                }
                return metadataMap;
            }
        });
    }

    /**
     * Class containing MetadataTable column names.
     */
    public static class Col {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String CONSOLE = "console";

        private Col() {
            /* Should not be constructed */
        }
    }
}
