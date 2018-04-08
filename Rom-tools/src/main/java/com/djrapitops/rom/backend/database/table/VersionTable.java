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

    public VersionTable(SQLDatabase db) {
        super(db, VERSION_COL);
    }

    @Override
    public void createTable() throws BackendException {
        createTable(TableSQLParser.createTable(tableName)
                .column(VERSION_COL, "integer").notNull()
                .toString()
        );
    }

    public boolean isNewDatabase() throws BackendException {
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

    public int getVersion() throws BackendException {
        String sql = "SELECT * FROM " + tableName;

        return query(new QueryAllStatement<Integer>(sql) {
            @Override
            public Integer processResults(ResultSet set) throws SQLException {
                int version = 0;
                if (set.next()) {
                    version = set.getInt(VERSION_COL);
                }
                return version;
            }
        });
    }

    public void setVersion(int version) throws BackendException {
        String sql = "REPLACE INTO " + tableName + " (" + VERSION_COL + ") VALUES (?)";

        execute(new ExecuteStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setInt(1, version);
            }
        });
    }
}
