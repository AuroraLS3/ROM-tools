package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Operations;
import com.djrapitops.rom.game.Game;

import java.util.List;

/**
 * Class that holds methods for CompletableFuture related to GameBackend.
 *
 * @author Rsl1122
 * @see java.util.concurrent.CompletableFuture
 */
public class GameProcesses {

    public GameProcesses() {
        /* Hides constructor */
    }

    public static List<Game> loadGames() {
        return Operations.ALL_GAMES.get();
    }

    public static void removeGames(List<Game> games) {
        Operations.ALL_GAMES.remove(games);
    }
}