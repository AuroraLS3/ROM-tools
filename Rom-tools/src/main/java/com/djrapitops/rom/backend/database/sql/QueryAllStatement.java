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

    /**
     * Constructor without fetch size.
     * <p>
     * Uses fetch size of 10.
     *
     * @param sql SQL to use for the query.
     */
    public QueryAllStatement(String sql) {
        this(sql, 10);
    }

    /**
     * Constructor with fetch size.
     *
     * @param sql       SQL to use for the query.
     * @param fetchSize Fetch size, affects how many rows SQL implementation fetches before giving them to Java.
     */
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
