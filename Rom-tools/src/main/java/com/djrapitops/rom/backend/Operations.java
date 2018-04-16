package com.djrapitops.rom.backend;

import com.djrapitops.rom.backend.cache.Keys;
import com.djrapitops.rom.backend.database.dao.GameDAO;
import com.djrapitops.rom.backend.database.dao.GamesDAO;
import com.djrapitops.rom.game.Game;

import java.util.Collections;
import java.util.List;

/**
 * Class that holds all Operations that can be performed in the GameBackend.
 *
 * @author Rsl1122
 */
public class Operations {

    public static final Operation<List<Game>> ALL_GAMES =
            new Operation<List<Game>>(GamesDAO::new, () -> Collections.EMPTY_MAP, Keys.GAMES);
    public static final Operation<Game> GAME =
            new Operation<Game>(GameDAO::new, () -> Collections.EMPTY_MAP, Keys.GAMES);

}