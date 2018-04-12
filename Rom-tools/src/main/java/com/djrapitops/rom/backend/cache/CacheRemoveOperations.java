package com.djrapitops.rom.backend.cache;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.operations.RemoveOperations;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.util.ThrowingWrapper;

import java.util.Collection;

/**
 * FetchOperations implementation for GameCache.
 *
 * @author Rsl1122
 * @see GameCache
 * @see GameCache#getOrFetch(GameCache.Request, ThrowingWrapper)
 */
public class CacheRemoveOperations implements RemoveOperations {

    private final GameCache cache;
    private final GameBackend mainBackend;

    public CacheRemoveOperations(GameCache cache) {
        this.cache = cache;
        mainBackend = cache.getMainBackend();
    }

    @Override
    public void games(Collection<Game> games) throws BackendException {
        mainBackend.remove().games(games);
        cache.clear(GameCache.Request.GET_GAMES);
    }
}
