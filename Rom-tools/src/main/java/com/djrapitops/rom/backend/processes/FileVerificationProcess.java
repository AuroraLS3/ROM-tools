package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Verifies the games exist and that the files have not changed.
 *
 * @author Rsl1122
 */
public class FileVerificationProcess implements Callable<List<Game>> {

    private final List<Game> games;

    public FileVerificationProcess(List<Game> games) {
        this.games = games;
    }

    @Override
    public List<Game> call() {
        List<Game> gamesWithChangedFiles = new ArrayList<>();

        for (Game game : games) {
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
