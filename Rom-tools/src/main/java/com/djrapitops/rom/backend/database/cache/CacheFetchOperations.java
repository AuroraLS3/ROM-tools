package com.djrapitops.rom.backend.database.cache;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.operations.FetchOperations;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.util.ThrowingWrapper;

import java.util.List;

/**
 * FetchOperations implementation for GameCache.
 *
 * @author Rsl1122
 * @see GameCache
 * @see GameCache#getOrFetch(GameCache.Request, ThrowingWrapper)
 */
public class CacheFetchOperations implements FetchOperations {

    private final GameCache cache;
    private final GameBackend mainBackend;

    public CacheFetchOperations(GameCache cache) {
        this.cache = cache;
        mainBackend = cache.getMainBackend();
    }

    @Override
    public List<Game> getGames() throws BackendException {
        return cache.getOrFetch(GameCache.Request.GET_GAMES, () -> mainBackend.fetch().getGames());
    }
}
