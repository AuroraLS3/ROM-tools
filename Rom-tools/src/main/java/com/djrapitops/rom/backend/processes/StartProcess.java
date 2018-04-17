package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Backend;
import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.updating.Update;
import com.djrapitops.rom.game.Game;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        CompletableFuture<List<Game>> loaded = CompletableFuture.supplyAsync(GameProcesses::loadGames);

        loaded.thenApplyAsync(FileProcesses::verifyFiles)
                .thenAccept(GameProcesses::removeGames)
                .thenApply(nothing -> GameProcesses.loadGames())
                .thenAccept(this::updateFrontendWith);

        loaded.thenAcceptAsync(this::updateFrontendWith)
                .thenAccept(nothing -> backend.setOpen(true));
    }

    private void updateFrontendWith(List<Game> loaded) {
        frontend.getUiUpdateProcess().submitTask(new Update<>(frontend, loaded));
    }
}
