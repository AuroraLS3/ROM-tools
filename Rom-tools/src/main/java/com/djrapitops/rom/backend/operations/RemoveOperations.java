package com.djrapitops.rom.backend.operations;

import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.Game;

import java.util.Collection;

/**
 * Database operations for removing items from the database.
 *
 * @author Rsl1122
 */
public interface RemoveOperations {

    void games(Collection<Game> games) throws BackendException;

}
