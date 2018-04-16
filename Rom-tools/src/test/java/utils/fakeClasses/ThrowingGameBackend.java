package utils.fakeClasses;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.Operation;
import com.djrapitops.rom.exceptions.BackendException;

public class ThrowingGameBackend implements GameBackend {

    public static final String OPEN = "Open throws exception success";
    public static final String SAVE = "Save throws exception success";
    public static final String FETCH = "Fetch throws exception success";
    public static final String REMOVE = "Remove throws exception success";

    @Override
    public <T> void save(Operation<T> op, T obj) {
        throw new BackendException(SAVE);
    }

    @Override
    public <T> T fetch(Operation<T> op) {
        throw new BackendException(FETCH);
    }

    @Override
    public <T> void remove(Operation<T> op, T obj) {
        throw new BackendException(REMOVE);
    }

    @Override
    public void open() {
        throw new BackendException(OPEN);
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void close() {
    }
}
