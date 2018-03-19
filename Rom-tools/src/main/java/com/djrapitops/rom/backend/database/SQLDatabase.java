package com.djrapitops.rom.backend.database;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.database.sql.ExecuteStatement;
import com.djrapitops.rom.backend.database.sql.QueryStatement;

import java.sql.SQLException;

/**
 * Represents a SQL Database that can store & retrieve items.
 *
 * @author Rsl1122
 */
public interface SQLDatabase extends GameBackend {

    /**
     * Executes an ExecuteStatement in the Database.
     *
     * @param statement Statement to execute.
     * @throws SQLException If the statement fails to execute.
     */
    void execute(ExecuteStatement statement) throws SQLException;

    /**
     * Queries the database with the QueryStatement.
     *
     * @param statement Statement to query.
     * @param <T>       Return type of the Query
     * @return Result of the query
     * @throws SQLException If the statement fails to execute.
     */
    <T> T query(QueryStatement<T> statement) throws SQLException;

}
