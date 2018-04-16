package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Backend;
import com.djrapitops.rom.backend.Operations;
import com.djrapitops.rom.game.Game;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Process that removes games from the database and loads them again.
 *
 * @author Rsl1122
 */
public class GameRemovalProcess implements Callable<List<Game>> {

    private final Future<List<Game>> toRemove;

    public GameRemovalProcess(Future<List<Game>> toRemove) {
        this.toRemove = toRemove;
    }

    @Override
    public List<Game> call() throws Exception {
        Operations.ALL_GAMES.remove(toRemove.get());
        Backend backend = Backend.getInstance();
        return backend.submitTask(new GameLoadingProcess(backend.getGameBackend())).get();
    }
}
