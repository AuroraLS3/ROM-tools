package utils.fakeClasses;

import java.sql.ResultSet;

public class ValuedFakePreparedStatement extends FakePreparedStatement {

    public static final ResultSet QUERY = null;
    public static final int EXECUTE = 100;
    public static final int[] EXECUTE_BATCH = new int[]{100};

    @Override
    public ResultSet executeQuery() {
        return QUERY;
    }

    @Override
    public int executeUpdate() {
        return EXECUTE;
    }

    @Override
    public int[] executeBatch() {
        return EXECUTE_BATCH;
    }

}
