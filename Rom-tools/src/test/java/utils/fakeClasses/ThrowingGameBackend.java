package utils.fakeClasses;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.operations.FetchOperations;
import com.djrapitops.rom.backend.operations.RemoveOperations;
import com.djrapitops.rom.backend.operations.SaveOperations;
import com.djrapitops.rom.exceptions.BackendException;

public class ThrowingGameBackend implements GameBackend {

    public static final String OPEN = "Open throws exception success";

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
    public void open() throws BackendException {
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
