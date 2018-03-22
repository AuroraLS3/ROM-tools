package com.djrapitops.rom.backend.database.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL query that closes proper elements and doesn't require preparing the statement.
 *
 * @author Rsl1122
 */
public abstract class QueryAllStatement<T> extends QueryStatement<T> {
    public QueryAllStatement(String sql) {
        this(sql, 10);
    }

    public QueryAllStatement(String sql, int fetchSize) {
        super(sql, fetchSize);
    }

    @Override
    public void prepare(PreparedStatement statement) {
        /* None Required */
    }

    @Override
    public abstract T processResults(ResultSet set) throws SQLException;
}
