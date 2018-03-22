package com.djrapitops.rom.backend.database.sql;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import utils.fakeClasses.ThrowingFakePreparedStatement;
import utils.fakeClasses.ValuedFakePreparedStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExecuteStatementTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private PreparedStatement valuedPreparedStatement;
    private PreparedStatement throwingPreparedStatement;

    @Before
    public void setUp() {
        valuedPreparedStatement = new ValuedFakePreparedStatement();
        throwingPreparedStatement = new ThrowingFakePreparedStatement();
    }

    @Test
    public void executeSuccess() throws SQLException {
        ExecuteStatement statement = new ExecuteStatement("") {
            @Override
            public void prepare(PreparedStatement statement) {
                /* Does not throw */
            }
        };
        assertTrue(statement.execute(valuedPreparedStatement));
    }

    @Test
    public void executeThrowsException() throws SQLException {
        thrown.expect(SQLException.class);
        thrown.expectMessage(ThrowingFakePreparedStatement.EXECUTE);

        ExecuteStatement statement = new ExecuteStatement("") {
            @Override
            public void prepare(PreparedStatement statement) {
                /* Does not throw */
            }
        };
        // Throws expected exception
        statement.execute(throwingPreparedStatement);
    }

    @Test
    public void executeBatchSuccess() throws SQLException {
        ExecuteStatement statement = new ExecuteStatement("") {
            @Override
            public void prepare(PreparedStatement statement) {
                /* Does not throw */
            }
        };
        statement.executeBatch(valuedPreparedStatement);
    }

    @Test
    public void executeBatchThrowsException() throws SQLException {
        thrown.expect(SQLException.class);
        thrown.expectMessage(ThrowingFakePreparedStatement.EXECUTE_BATCH);

        ExecuteStatement statement = new ExecuteStatement("") {
            @Override
            public void prepare(PreparedStatement statement) {
                /* Does not throw */
            }
        };
        // Throws expected exception
        statement.executeBatch(throwingPreparedStatement);
    }

    @Test
    public void getSqlReturnsValue() {
        String expected = "Test Success";

        ExecuteStatement statement = new ExecuteStatement(expected) {
            @Override
            public void prepare(PreparedStatement statement) {

            }
        };
        assertEquals(expected, statement.getSql());
    }
}