package com.djrapitops.rom.backend.operations;

import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.Game;

/**
 * Interface for Storage related Backend operations.
 *
 * @author Rsl1122
 */
public interface SaveOperations {
    void saveGame(Game game) throws BackendException;
}
