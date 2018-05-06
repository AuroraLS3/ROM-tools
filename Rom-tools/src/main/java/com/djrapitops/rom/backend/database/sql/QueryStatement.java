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

    /**
     * Constructor without fetch size.
     * <p>
     * Uses fetch size of 10.
     *
     * @param sql SQL to use for query.
     */
    public QueryStatement(String sql) {
        this(sql, 10);
    }

    /**
     * Constructor without fetch size.
     *
     * @param sql       SQL to use for query.
     * @param fetchSize Fetch size, affects how many rows SQL implementation fetches before giving them to Java.
     */
    public QueryStatement(String sql, int fetchSize) {
        this.sql = sql;
        this.fetchSize = fetchSize;
    }

    /**
     * Executes the given statement.
     *
     * @param statement PreparedStatement to execute.
     * @return Result of the Query after processing results.
     * @throws SQLException If there is an error in SQL syntax.
     */
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

    /**
     * Prepares the statement for query.
     *
     * @param statement PreparedStatement to set values to.
     * @throws SQLException If there is an error in SQL syntax.
     */
    protected abstract void prepare(PreparedStatement statement) throws SQLException;

    /**
     * Process the ResultSet into the actual objects.
     *
     * @param set ResultSet given by the PrepareStatement when queried.
     * @return Processed objects from ResultSet.
     * @throws SQLException If there is an error in SQL syntax.
     */
    protected abstract T processResults(ResultSet set) throws SQLException;

    public String getSql() {
        return sql;
    }

}
