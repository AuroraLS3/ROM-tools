package com.djrapitops.rom.frontend.state;

import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.Consoles;
import com.djrapitops.rom.game.Game;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Object that holds the Frontend state so that it can be kept in sync with the backend changes.
 *
 * @author Rsl1122
 */
public class State {

    private final List<Consumer<State>> updateOnChange;

    private List<Game> loadedGames;
    private Set<Game> selectedGames;
    private List<Game> visibleGames;

    private String search;
    private Set<Console> displayedConsoles;

    private String status;

    public State() {
        loadedGames = new ArrayList<>();
        selectedGames = new HashSet<>();
        displayedConsoles = new HashSet<>();
        visibleGames = new ArrayList<>();

        search = "";
        status = "";
        updateOnChange = new ArrayList<>();
    }

    public void performStateChange(Consumer<State> operation) {
        State state = this;
        operation.accept(state);
    }

    public void addStateListener(Consumer<State> listener) {
        updateOnChange.add(listener);
    }

    public <T extends Consumer<State>> void clearStateListenerInstances(Class<T> classOfInstance) {
        updateOnChange.removeIf(updatable -> updatable.getClass().equals(classOfInstance));
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Console> getDisplayedConsoles() {
        return displayedConsoles;
    }

    private void updateVisibleGames() {
        if (search.startsWith("\"")) {
            visibleGames = getLoadedGames().stream()
                    .filter(game -> game.getMetadata().getName().contains(search.substring(1)))
                    .collect(Collectors.toList());
        } else {
            visibleGames = getLoadedGames().stream()
                    .filter(game -> game.getMetadata().getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (!displayedConsoles.isEmpty()) {
            visibleGames = visibleGames.stream()
                    .filter(game -> game.getMetadata().getConsole().map(displayedConsoles::contains).orElse(true))
                    .collect(Collectors.toList());
        }
    }

    public void activateConsoleFilter(Console console) {
        displayedConsoles.add(console);
        updateVisibleGames();
    }

    public void deactivateConsoleFilter(Console console) {
        if (displayedConsoles.isEmpty()) {
            displayedConsoles.addAll(Consoles.getAll());
        }
        displayedConsoles.remove(console);
        updateVisibleGames();
    }

    public boolean hasFilteredConsoles() {
        return displayedConsoles.isEmpty() || displayedConsoles.containsAll(Consoles.getAll());
    }

    public List<Consumer<State>> getUpdateOnChange() {
        return updateOnChange;
    }
}
