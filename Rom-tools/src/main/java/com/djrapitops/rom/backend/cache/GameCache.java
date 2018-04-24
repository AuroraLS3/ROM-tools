package com.djrapitops.rom.backend.cache;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.Operation;
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

    private final Map<Keys, Object> cache;

    public GameCache(GameBackend mainBackend) {
        this.mainBackend = mainBackend;
        cache = new EnumMap<>(Keys.class);
    }

    @Override
    public <T> void save(Operation<T> op, T obj) {
        mainBackend.save(op, obj);
        clear(op.getKey());
    }

    @Override
    public <T> T fetch(Operation<T> op) {
        return getOrFetch(op.getKey(), () -> mainBackend.fetch(op));
    }

    @Override
    public <T> void remove(Operation<T> op, T obj) {
        mainBackend.remove(op, obj);
        clear(op.getKey());
    }

    <T> T getOrFetch(Keys request, ThrowingWrapper<T, BackendException> fetch) {
        Object inCache = cache.get(request);
        if (inCache != null) {
            return (T) inCache;
        }
        T result = fetch.get();
        cache.put(request, result);
        return result;
    }

    @Override
    public void open() {
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

    public void clear(Keys request) {
        cache.remove(request);
    }
}
