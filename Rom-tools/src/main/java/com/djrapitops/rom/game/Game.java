package com.djrapitops.rom.game;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Represents a game.
 *
 * @author Rsl1122
 */
public class Game implements Comparable<Game> {

    private int id;
    private int metadataId;
    private Collection<GameFile> gameFiles;
    private Metadata metadata;

    public Game(int id, int metadataId) {
        this.id = id;
        this.metadataId = metadataId;
        gameFiles = Collections.emptyList();
    }

    public Game() {
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
    public int compareTo(Game game) {
        return this.getMetadata().getName()
                .compareTo(game.getMetadata().getName());
    }

    public int getId() {
        return id;
    }

    public int getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(int metadataId) {
        this.metadataId = metadataId;
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
        return Objects.equals(gameFiles, game.gameFiles) &&
                Objects.equals(metadata, game.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameFiles, metadata);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameFiles=" + gameFiles +
                ", metadata=" + metadata +
                '}';
    }
}
