package com.djrapitops.rom.backend.database.operations;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.operations.FetchOperations;
import com.djrapitops.rom.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * FetchOperations implementation for SQLDatabase.
 *
 * @author Rsl1122
 * @see SQLDatabase
 */
public class SQLFetchOps implements FetchOperations {

    private final SQLDatabase database;

    public SQLFetchOps(SQLDatabase database) {
        this.database = database;
    }

    @Override
    public List<Game> getGames() {
        return new ArrayList<>(); // TODO implement
    }
}
