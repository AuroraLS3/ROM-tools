package com.djrapitops.rom.backend.database.operations;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.Tables;
import com.djrapitops.rom.backend.operations.SaveOperations;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.Game;

/**
 * SaveOperations implementation for SQLDatabase.
 *
 * @author Rsl1122
 * @see SaveOperations
 * @see SQLDatabase
 */
public class SQLSaveOps implements SaveOperations {

    private final Tables tables;

    public SQLSaveOps(SQLDatabase database) {
        this.tables = database.getTables();
    }

    @Override
    public void saveGame(Game game) throws BackendException {
        int gameId = tables.getGameTable().saveGame(game);

        tables.getMetadataTable().saveMetadata(gameId, game.getMetadata());

        tables.getFileTable().saveGameFiles(gameId, game.getGameFiles());
    }
}
