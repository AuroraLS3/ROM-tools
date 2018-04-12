package utils.fakeClasses;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.operations.FetchOperations;
import com.djrapitops.rom.backend.operations.RemoveOperations;
import com.djrapitops.rom.backend.operations.SaveOperations;

public class TestGameBackend implements GameBackend {

    private boolean open = false;

    @Override
    public SaveOperations save() {
        return null;
    }

    @Override
    public FetchOperations fetch() {
        return null;
    }

    @Override
    public RemoveOperations remove() {
        return null;
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
}
