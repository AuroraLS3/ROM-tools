package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Backend;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.updating.Update;
import com.djrapitops.rom.game.Game;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

/**
 * Process that performs backend processes.
 *
 * @author Rsl1122
 */
public class StartProcess implements Runnable {

    private final Backend backend;
    private final Frontend frontend;

    public StartProcess(Backend backend, Frontend frontend) {
        this.backend = backend;
        this.frontend = frontend;
    }

    @Override
    public void run() {
        Future<List<Game>> gameLoadingTask = backend.submitTask(new GameLoadingProcess(backend.getGameBackend()));
        Future<List<Game>> fileVerificationTask = backend.submitTask(new FileVerificationProcess(gameLoadingTask));

        frontend.getUiUpdateProcess().submitTask(new Update<>(frontend, gameLoadingTask));

        removeModifiedGames(fileVerificationTask);
    }

    private void removeModifiedGames(Future<List<Game>> fileVerificationTask) {
        try {
            List<Game> changedGames = fileVerificationTask.get();
            backend.getGameBackend().remove().games(changedGames);
        } catch (InterruptedException | ExecutionException | BackendException e) {
            ExceptionHandler.handle(Level.SEVERE, e);
        }
    }
}
