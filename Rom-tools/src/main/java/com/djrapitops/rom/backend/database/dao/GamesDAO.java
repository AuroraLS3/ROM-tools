package com.djrapitops.rom.backend.database.dao;

import com.djrapitops.rom.backend.Log;
import com.djrapitops.rom.backend.Operations;
import com.djrapitops.rom.backend.database.DAO;
import com.djrapitops.rom.backend.database.table.FileTable;
import com.djrapitops.rom.backend.database.table.GameTable;
import com.djrapitops.rom.backend.database.table.SQLTables;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import com.djrapitops.rom.game.Metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DAO for operations on multiple games.
 *
 * @author Rsl1122
 */
public class GamesDAO implements DAO<List<Game>> {

    @Override
    public void add(SQLTables tables, List<Game> games) {
        Log.log("Saving games..");
        int i = 1;
        int size = games.size();
        for (Game game : games) {
            Log.log("Saving games: (" + i + "/" + size + ") " + game.getMetadata().getName());
            Operations.GAME.save(game);
            i++;
        }
        Log.log("Saved " + size + " games..");
    }

    @Override
    public List<Game> get(SQLTables tables, Filter filter) {
        Map<Integer, Game> games = tables.getGameTable().getGames();

        Map<Integer, Metadata> metadata = tables.getMetadataTable().getMetadata();
        for (Game game : games.values()) {
            game.setMetadata(metadata.get(game.getMetadataId()));
        }

        Map<Integer, List<GameFile>> paths = tables.getFileTable().getGameFiles();
        for (Map.Entry<Integer, List<GameFile>> entry : paths.entrySet()) {
            games.get(entry.getKey()).setGameFiles(entry.getValue());
        }

        return new ArrayList<>(games.values());
    }

    @Override
    public void remove(SQLTables tables, List<Game> games) {
        GameTable gameTable = tables.getGameTable();
        FileTable fileTable = tables.getFileTable();

        List<Integer> gameIDs = games.stream().map(Game::getId).collect(Collectors.toList());

        gameTable.removeGames(games);
        fileTable.removeFiles(gameIDs);
    }
}