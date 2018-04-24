package com.djrapitops.rom.frontend.state;

import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.Game;
import javafx.application.Platform;

import java.util.*;
import java.util.stream.Collectors;

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
    private Set<Console> displayedConsoles;

    private String loadingInformation;

    public State() {
        loadedGames = new ArrayList<>();
        selectedGames = new HashSet<>();
        displayedConsoles = new HashSet<>();
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
        setSelectedGames(new HashSet<>());
        setSearch("");
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
        updateVisibleGames();
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

    public Set<Console> getDisplayedConsoles() {
        return displayedConsoles;
    }

    public void setDisplayedConsoles(Collection<Console> displayedConsoles) {
        this.displayedConsoles = new HashSet<>(displayedConsoles);
    }

    private void updateVisibleGames() {
        if (search.startsWith("\"")) {
            visibleGames = getLoadedGames().stream()
                    .filter(game -> game.getName().contains(search.substring(1)))
                    .collect(Collectors.toList());
        } else {
            visibleGames = getLoadedGames().stream()
                    .filter(game -> game.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (!displayedConsoles.isEmpty()) {
            visibleGames = visibleGames.stream()
                    .filter(game -> displayedConsoles.contains(game.getMetadata().getConsole()))
                    .collect(Collectors.toList());
        }
    }

    public void activateConsoleFilter(Console console) {
        displayedConsoles.add(console);
        updateVisibleGames();
    }

    public void deactivateConsoleFilter(Console console) {
        if (displayedConsoles.isEmpty()) {
            displayedConsoles.addAll(Arrays.asList(Console.values()));
        }
        displayedConsoles.remove(console);
        updateVisibleGames();
    }

    public boolean hasFilteredConsoles() {
        return displayedConsoles.isEmpty() || displayedConsoles.containsAll(Arrays.asList(Console.values()));
    }
}
