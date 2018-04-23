package com.djrapitops.rom.frontend.state;

import com.djrapitops.rom.game.Game;
import javafx.application.Platform;

import java.util.*;

/**
 * Object that holds the Frontend state so that it can be kept in sync with the backend changes.
 *
 * @author Rsl1122
 */
public class State {

    private final List<Updatable<State>> updateOnChange;

    private List<Game> loadedGames;
    private Set<Game> selectedGames;
    private List<Game> visibleGames;

    private String search;

    private String loadingInformation;

    public State() {
        loadedGames = new ArrayList<>();
        selectedGames = new HashSet<>();
        visibleGames = new ArrayList<>();

        search = "";
        loadingInformation = "";
        updateOnChange = new ArrayList<>();
    }

    public void performStateChange(StateOperation operation) {
        State state = this;
        Platform.runLater(() -> {
            operation.operateOnState(state);
            // New List to prevent Concurrent Exception due to GamesView adding more games that are updatable.
            new ArrayList<>(updateOnChange).forEach(toUpdate -> toUpdate.update(state));
        });
    }

    public void addStateListener(Updatable<State> listener) {
        updateOnChange.add(listener);
    }

    public <T extends Updatable<State>> void clearStateListenerInstances(Class<T> classOfInstance) {
        for (Updatable<State> updatable : new ArrayList<>(updateOnChange)) {
            if (updatable.getClass().equals(classOfInstance)) {
                updateOnChange.remove(updatable);
            }
        }
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

    public String getLoadingInformation() {
        return loadingInformation;
    }

    public void setLoadingInformation(String loadingInformation) {
        this.loadingInformation = loadingInformation;
    }
}
