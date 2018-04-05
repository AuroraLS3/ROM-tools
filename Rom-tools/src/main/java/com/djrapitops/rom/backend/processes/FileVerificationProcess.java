package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.exceptions.Level;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Verifies the games exist and that the files have not changed.
 *
 * @author Rsl1122
 */
public class FileVerificationProcess implements Callable<List<Game>> {

    private final Future<List<Game>> games;

    public FileVerificationProcess(Future<List<Game>> games) {
        this.games = games;
    }

    @Override
    public List<Game> call() {
        try {
            return tryToVerify();
        } catch (InterruptedException | ExecutionException e) {
            ExceptionHandler.handle(Level.WARNING, e);
        }
        return new ArrayList<>();
    }

    private List<Game> tryToVerify() throws InterruptedException, ExecutionException {
        List<Game> gamesWithChangedFiles = new ArrayList<>();

        for (Game game : games.get()) {
            for (GameFile gameFile : game.getGameFiles()) {
                if (!gameFile.matchesHash()) {
                    gamesWithChangedFiles.add(game);
                    break;
                }
            }
        }

        return gamesWithChangedFiles;
    }
}
