package com.djrapitops.rom.frontend.state;

import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.game.Game;
import javafx.application.Platform;

import java.util.*;

/**
 * Object that holds the Frontend state so that it can be kept in sync with the backend changes.
 *
 * @author Rsl1122
 */
public class State {

    private final Frontend frontend;

    private List<Game> loadedGames;
    private Set<Game> selectedGames;
    private List<Game> visibleGames;

    private String search;

    public State(Frontend frontend) {
        this.frontend = frontend;

        loadedGames = new ArrayList<>();
        selectedGames = new HashSet<>();
        visibleGames = new ArrayList<>();

        search = "";
    }

    public void performStateChange(StateOperation operation) {
        State state = this;
        Platform.runLater(() -> {
            operation.operateOnState(state);
            frontend.update(state);
        });
    }

    public List<Game> getLoadedGames() {
        return loadedGames;
    }

    public void setLoadedGames(List<Game> loadedGames) {
        Collections.sort(loadedGames);
        this.loadedGames = loadedGames;
    }

    public Set<Game> getSelectedGames() {
        return selectedGames;
    }

    public void setSelectedGames(Set<Game> selectedGames) {
        this.selectedGames = selectedGames;
    }

    public List<Game> getVisibleGames() {
        return visibleGames;
    }

    public void setVisibleGames(List<Game> visibleGames) {
        this.visibleGames = visibleGames;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void gameSelected(Game game, Boolean isSelected) {
        if (isSelected) {
            selectedGames.add(game);
        } else {
            selectedGames.remove(game);
        }
    }

    public boolean isSelected(Game game) {
        return selectedGames.contains(game);
    }
}
