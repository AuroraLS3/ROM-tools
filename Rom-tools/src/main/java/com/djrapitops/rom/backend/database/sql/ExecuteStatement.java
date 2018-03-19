package com.djrapitops.rom.backend.database.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SQL execution that closes proper elements.
 *
 * @author Rsl1122
 */
public abstract class ExecuteStatement {

    private final String sql;

    public ExecuteStatement(String sql) {
        this.sql = sql;
    }

    public boolean execute(PreparedStatement statement) throws SQLException {
        try {
            prepare(statement);
            return statement.executeUpdate() > 0;
        } finally {
            statement.close();
        }
    }

    public void executeBatch(PreparedStatement statement) throws SQLException {
        try {
            prepare(statement);
            statement.executeBatch();
        } finally {
            statement.close();
        }
    }

    public abstract void prepare(PreparedStatement statement) throws SQLException;

    public String getSql() {
        return sql;
    }

}
