package utils.fakeClasses;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.Operation;

public class TestGameBackend implements GameBackend {

    private boolean open = false;
    private Object lastSaved;
    private Object lastRemoved;

    @Override
    public <T> void save(Operation<T> op, T obj) {
        lastSaved = obj;
    }

    @Override
    public <T> T fetch(Operation<T> op) {
        return op.getDao().get(null, null);
    }

    @Override
    public <T> void remove(Operation<T> op, T obj) {
        lastRemoved = obj;
    }

    @Override
    public void open() {
        open = true;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void close() {
        open = false;
    }

    public Object getLastSaved() {
        return lastSaved;
    }

    public Object getLastRemoved() {
        return lastRemoved;
    }

}
