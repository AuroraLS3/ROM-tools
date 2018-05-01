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

    /**
     * Constructor.
     *
     * @param sql SQL to use for execution.
     */
    public ExecuteStatement(String sql) {
        this.sql = sql;
    }

    /**
     * Execute the PreparedStatement.
     *
     * @param statement statement to execute.
     * @return true/false based on how many rows were updated.
     * @throws SQLException If there is an error in sql syntax.
     */
    public boolean execute(PreparedStatement statement) throws SQLException {
        try {
            prepare(statement);
            return statement.executeUpdate() > 0;
        } finally {
            statement.close();
        }
    }

    /**
     * Execute the PreparedStatement as a batch.
     *
     * @param statement statement to execute as a batch.
     * @throws SQLException If there is an error in SQL syntax.
     */
    public void executeBatch(PreparedStatement statement) throws SQLException {
        try {
            prepare(statement);
            statement.executeBatch();
        } finally {
            statement.close();
        }
    }

    /**
     * Method for preparing the statement for execution.
     * <p>
     * Can be used to call addBatch method on the PreparedStatement.
     *
     * @param statement PreparedStatement to set values to.
     * @throws SQLException If there is an error in SQL syntax.
     */
    public abstract void prepare(PreparedStatement statement) throws SQLException;

    public String getSql() {
        return sql;
    }

}
