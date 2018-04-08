package com.djrapitops.rom.game;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Represents a game.
 *
 * @author Rsl1122
 */
public class Game {

    private final String name;
    private Collection<GameFile> gameFiles;
    private Metadata metadata;

    public Game(String fileName) {
        this.name = fileName;
        gameFiles = Collections.emptyList();
    }

    public Collection<GameFile> getGameFiles() {
        return gameFiles;
    }

    public void setGameFiles(Collection<GameFile> gameFiles) {
        this.gameFiles = gameFiles;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        return Objects.equals(metadata, game.metadata) &&
                Objects.equals(name, game.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(metadata, name);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameFiles=" + gameFiles +
                ", metadata=" + metadata +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }
}
