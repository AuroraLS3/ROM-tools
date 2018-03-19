package com.djrapitops.rom.backend.database.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL query that closes proper elements.
 *
 * @author Rsl1122
 */
public abstract class QueryStatement<T> {

    private final String sql;
    private final int fetchSize;

    public QueryStatement(String sql) {
        this(sql, 10);
    }

    public QueryStatement(String sql, int fetchSize) {
        this.sql = sql;
        this.fetchSize = fetchSize;
    }

    public T executeQuery(PreparedStatement statement) throws SQLException {
        try {
            statement.setFetchSize(fetchSize);
            prepare(statement);
            try (ResultSet set = statement.executeQuery()) {
                return processResults(set);
            }
        } finally {
            statement.close();
        }
    }

    public abstract void prepare(PreparedStatement statement) throws SQLException;

    public abstract T processResults(ResultSet set) throws SQLException;

    public String getSql() {
        return sql;
    }

}
