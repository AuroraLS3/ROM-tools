package com.djrapitops.rom.backend.operations;

import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.Game;

import java.util.List;

/**
 * Interface for Fetch related backend operations.
 *
 * @author Rsl1122
 */
public interface FetchOperations {

    List<Game> getGames() throws BackendException;

}
