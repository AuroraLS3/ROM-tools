package com.djrapitops.rom.backend.cache;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.operations.FetchOperations;
import com.djrapitops.rom.backend.operations.RemoveOperations;
import com.djrapitops.rom.backend.operations.SaveOperations;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.util.ThrowingWrapper;

import java.util.EnumMap;
import java.util.Map;

/**
 * Wrapper Cache layer to serve between Database and UI in order to increase performance.
 *
 * @author Rsl1122
 */
public class GameCache implements GameBackend {

    private final GameBackend mainBackend;

    private final Map<Request, Object> cache;
    private final CacheFetchOperations fetchOperations;

    public GameCache(GameBackend mainBackend) {
        this.mainBackend = mainBackend;
        cache = new EnumMap<>(Request.class);
        fetchOperations = new CacheFetchOperations(this);
    }

    @Override
    public FetchOperations fetch() {
        return fetchOperations;
    }

    <T> T getOrFetch(Request request, ThrowingWrapper<T, BackendException> fetch) throws BackendException {
        Object inCache = cache.get(request);
        if (inCache != null) {
            return (T) inCache;
        }
        T result = fetch.get();
        cache.put(request, result);
        return result;
    }

    @Override
    public SaveOperations save() {
        return mainBackend.save();
    }

    @Override
    public RemoveOperations remove() {
        return mainBackend.remove();
    }

    @Override
    public void open() throws BackendException {
        mainBackend.open();
    }

    @Override
    public boolean isOpen() {
        return mainBackend.isOpen();
    }

    @Override
    public void close() {
        mainBackend.close();
    }

    public GameBackend getMainBackend() {
        return mainBackend;
    }

    public enum Request {
        GET_GAMES
    }
}
