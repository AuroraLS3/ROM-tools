package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.util.Callback;

import java.util.List;

/**
 * Loads required data from backend and sends it to the frontend.
 *
 * @author Rsl1122
 */
public class GameLoadingProcess implements Runnable {

    private final GameBackend backend;
    private final Callback<List<Game>> callback;

    public GameLoadingProcess(GameBackend backend, Callback<List<Game>> callback) {
        this.backend = backend;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            List<Game> games = backend.fetch().getGames();
            callback.result(games);
        } catch (BackendException e) {
            // TODO Interrupt main program with error message
        }
    }
}
