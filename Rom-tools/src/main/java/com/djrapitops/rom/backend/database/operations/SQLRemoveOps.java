package com.djrapitops.rom.backend.database.operations;

import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.Tables;
import com.djrapitops.rom.backend.database.table.FileTable;
import com.djrapitops.rom.backend.database.table.GameTable;
import com.djrapitops.rom.backend.database.table.MetadataTable;
import com.djrapitops.rom.backend.operations.RemoveOperations;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.game.Game;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RemoveOperations implementation for SQLDatabase.
 *
 * @author Rsl1122
 * @see SQLDatabase
 */
public class SQLRemoveOps implements RemoveOperations {

    private final Tables tables;

    public SQLRemoveOps(SQLDatabase database) {
        this.tables = database.getTables();
    }

    @Override
    public void games(Collection<Game> games) throws BackendException {
        GameTable gameTable = tables.getGameTable();
        MetadataTable metadataTable = tables.getMetadataTable();
        FileTable fileTable = tables.getFileTable();

        Map<String, Integer> gameIDMap = gameTable.getGameIDMap();
        List<Integer> gameIDs = games.stream().map(Game::getName).map(gameIDMap::get).collect(Collectors.toList());

        gameTable.removeGames(games);
        metadataTable.removeMetadata(gameIDs);
        fileTable.removeFiles(gameIDs);
    }
}
