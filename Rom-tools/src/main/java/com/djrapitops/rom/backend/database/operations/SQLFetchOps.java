package com.djrapitops.rom.backend.database.operations;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.Tables;
import com.djrapitops.rom.backend.operations.FetchOperations;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import com.djrapitops.rom.game.Metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * FetchOperations implementation for SQLDatabase.
 *
 * @author Rsl1122
 * @see FetchOperations
 * @see SQLDatabase
 */
public class SQLFetchOps implements FetchOperations {

    private final Tables tables;

    public SQLFetchOps(SQLDatabase database) {
        this.tables = database.getTables();
    }

    @Override
    public List<Game> getGames() throws BackendException {
        Map<Integer, Game> games = tables.getGameTable().getGames();

        Map<Integer, Metadata> metadata = tables.getMetadataTable().getMetadata();
        for (Map.Entry<Integer, Metadata> entry : metadata.entrySet()) {
            games.get(entry.getKey()).setMetadata(entry.getValue());
        }

        Map<Integer, Set<GameFile>> paths = tables.getFileTable().getPaths();
        for (Map.Entry<Integer, Set<GameFile>> entry : paths.entrySet()) {
            games.get(entry.getKey()).setGameFiles(entry.getValue());
        }

        return new ArrayList<>(games.values());
    }
}
