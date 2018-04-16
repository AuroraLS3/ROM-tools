package com.djrapitops.rom.backend.database.dao;

import com.djrapitops.rom.backend.database.DAO;
import com.djrapitops.rom.backend.database.table.SQLTables;
import com.djrapitops.rom.game.Game;

/**
 * DAO for operations on a single game.
 *
 * @author Rsl1122
 */
public class GameDAO extends DAO<Game> {

    @Override
    public void add(SQLTables tables, Game game) {
        int gameId = tables.getGameTable().saveGame(game);

        tables.getMetadataTable().saveMetadata(gameId, game.getMetadata());

        tables.getFileTable().saveGameFiles(gameId, game.getGameFiles());
    }

    @Override
    public Game get(SQLTables tables, Filter filter) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove(SQLTables tables, Game obj) {
        throw new UnsupportedOperationException("Not implemented");
    }
}