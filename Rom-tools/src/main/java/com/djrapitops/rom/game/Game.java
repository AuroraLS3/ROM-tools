package com.djrapitops.rom.game;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a game
 *
 * @author Rsl1122
 */
public class Game {

    private final Set<GameFile> gameFiles;
    private final Metadata metadata;

    public Game(Metadata metadata) {
        gameFiles = new HashSet<>();
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(metadata, game.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadata);
    }
}
