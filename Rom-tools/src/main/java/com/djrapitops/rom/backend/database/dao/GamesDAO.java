package com.djrapitops.rom.backend.database.dao;

import com.djrapitops.rom.backend.Operations;
import com.djrapitops.rom.backend.database.DAO;
import com.djrapitops.rom.backend.database.table.FileTable;
import com.djrapitops.rom.backend.database.table.GameTable;
import com.djrapitops.rom.backend.database.table.MetadataTable;
import com.djrapitops.rom.backend.database.table.SQLTables;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import com.djrapitops.rom.game.Metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DAO for multiple games.
 *
 * @author Rsl1122
 */
public class GamesDAO implements DAO<List<Game>> {

    @Override
    public void add(SQLTables tables, List<Game> games) {
        for (Game game : games) {
            Operations.GAME.save(game);
        }
    }

    @Override
    public List<Game> get(SQLTables tables, Filter filter) {
        Map<Integer, Game> games = tables.getGameTable().getGames();

        Map<Integer, Metadata> metadata = tables.getMetadataTable().getMetadata();
        for (Map.Entry<Integer, Metadata> entry : metadata.entrySet()) {
            Game game = games.get(entry.getKey());
            if (game == null) {
                continue;
            }
            game.setMetadata(entry.getValue());
        }

        Map<Integer, Set<GameFile>> paths = tables.getFileTable().getGameFiles();
        for (Map.Entry<Integer, Set<GameFile>> entry : paths.entrySet()) {
            games.get(entry.getKey()).setGameFiles(entry.getValue());
        }

        return new ArrayList<>(games.values());
    }

    @Override
    public void remove(SQLTables tables, List<Game> games) {
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