package utils.fakeClasses;

import com.djrapitops.rom.backend.database.DAO;
import com.djrapitops.rom.backend.database.Tables;

public class FakeDAO<T> implements DAO<T> {

    @Override
    public void add(Tables tables, T obj) {

    }

    @Override
    public T get(Tables tables, Filter filter) {
        return null;
    }

    @Override
    public void remove(Tables tables, T obj) {

    }
}