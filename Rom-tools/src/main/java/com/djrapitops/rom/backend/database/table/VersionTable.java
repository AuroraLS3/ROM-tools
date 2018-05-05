package com.djrapitops.rom.backend.database.table;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryAllStatement;
import com.djrapitops.rom.backend.database.sql.QueryStatement;
import com.djrapitops.rom.backend.database.sql.TableSQLParser;
import com.djrapitops.rom.exceptions.BackendException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Keeps track of database schema version.
 *
 * @author Rsl1122
 */
public class VersionTable extends Table {

    private static final String VERSION_COL = "version";

    VersionTable(SQLDatabase db) {
        super(db, VERSION_COL);
    }

    @Override
    public void createTable() {
        createTable(TableSQLParser.createTable(tableName)
                .column(VERSION_COL, "integer").notNull()
                .toString()
        );
    }

    /**
     * Check if the database was just created.
     *
     * @return true/false
     * @throws BackendException If operation fails.
     */
    boolean isNewDatabase() {
        String sql = "SELECT tbl_name FROM sqlite_master WHERE tbl_name=?";

        return query(new QueryStatement<Boolean>(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setString(1, tableName);
            }

            @Override
            public Boolean processResults(ResultSet set) throws SQLException {
                return !set.next();
            }
        });
    }

    /**
     * Get the Database schema version.
     *
     * @return version in version table.
     * @throws BackendException If operation fails.
     */
    int getVersion() {
        String sql = "SELECT * FROM " + tableName;

        return query(new QueryAllStatement<Integer>(sql) {
            @Override
            public Integer processResults(ResultSet set) throws SQLException {
                set.next();
                return set.getInt(VERSION_COL);
            }
        });
    }

    /**
     * Set the Database schema version.
     *
     * @param version version in version table.
     */
    void setVersion(int version) {
        removeOldVersion();

        String sql = "INSERT INTO " + tableName + " (" + VERSION_COL + ") VALUES (?)";

        execute(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setInt(1, version);
            }
        });
    }

    private void removeOldVersion() {
        String sql = "DELETE FROM " + tableName;

        execute(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) {
                /* No variables required */
            }
        });
    }
}
