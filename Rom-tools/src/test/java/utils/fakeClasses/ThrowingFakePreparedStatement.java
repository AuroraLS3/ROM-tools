package utils.fakeClasses;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ThrowingFakePreparedStatement extends FakePreparedStatement {

    public static final String QUERY = "Query Test Success";
    public static final String EXECUTE = "Update Test Success";
    public static final String EXECUTE_BATCH = "Execute Batch Test Success";

    @Override
    public ResultSet executeQuery() throws SQLException {
        throw new SQLException(QUERY);
    }

    @Override
    public int executeUpdate() throws SQLException {
        throw new SQLException(EXECUTE);

    }

    @Override
    public int[] executeBatch() throws SQLException {
        throw new SQLException(EXECUTE_BATCH);
    }

}
