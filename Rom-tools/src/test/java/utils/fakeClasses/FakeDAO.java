package utils.fakeClasses;

import com.djrapitops.rom.backend.database.DAO;
import com.djrapitops.rom.backend.database.table.SQLTables;
import com.djrapitops.rom.exceptions.BackendException;

public class FakeDAO<T> extends DAO<T> {

    @Override
    public void add(SQLTables tables, T obj) {

    }

    @Override
    public T get(SQLTables tables, Filter filter) {
        return null;
    }

    @Override
    public void remove(SQLTables tables, T obj) throws BackendException {

    }
}