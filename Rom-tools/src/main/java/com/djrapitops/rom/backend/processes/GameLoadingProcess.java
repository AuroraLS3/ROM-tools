package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.Game;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Loads required data from backend and sends it to the frontend.
 *
 * @author Rsl1122
 */
public class GameLoadingProcess implements Callable<List<Game>> {

    private final GameBackend backend;

    public GameLoadingProcess(GameBackend backend) {
        this.backend = backend;
    }

    @Override
    public List<Game> call() {
        try {
            return backend.fetch().getGames();
        } catch (BackendException e) {
            throw new IllegalStateException(e);
        }
    }
}
