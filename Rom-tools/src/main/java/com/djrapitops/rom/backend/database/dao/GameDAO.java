package com.djrapitops.rom.backend.database.dao;

import com.djrapitops.rom.backend.Log;
import com.djrapitops.rom.backend.database.DAO;
import com.djrapitops.rom.backend.database.Tables;
import com.djrapitops.rom.game.Game;

/**
 * DAO for operations on a single game.
 *
 * @author Rsl1122
 */
public class GameDAO implements DAO<Game> {

    @Override
    public void add(Tables tables, Game game) {
        Log.debug("Save game: " + game.getMetadata().getName());
        if (tables.getFileTable().containsGame(game.getGameFiles())) {
            return;
        }

        int metadataID = tables.getMetadataTable().saveMetadata(game.getMetadata());
        game.setMetadataId(metadataID);
        int gameId = tables.getGameTable().saveGame(game);
        tables.getFileTable().saveGameFiles(gameId, game.getGameFiles());
    }

    @Override
    public Game get(Tables tables, Filter filter) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void remove(Tables tables, Game obj) {
        throw new UnsupportedOperationException("Not implemented");
    }
}