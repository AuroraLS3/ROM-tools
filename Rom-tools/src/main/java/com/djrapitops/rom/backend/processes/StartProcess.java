package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Backend;
import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.javafx.updating.Update;
import com.djrapitops.rom.game.Game;

import java.util.List;
import java.util.concurrent.Future;

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
    }
}
