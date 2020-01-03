package com.djrapitops.rom.frontend.javafx.views;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.components.ConsoleFilterButton;
import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;
import com.djrapitops.rom.game.Console;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.scene.layout.BorderPane;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Filters view in the UI.
 *
 * @author Rsl1122
 */
public class FiltersView extends BorderPane implements Updatable<State> {

    private Set<Console> loadedConsoles;

    private JFXMasonryPane masonryPane;

    public FiltersView(JavaFXFrontend frontend, BorderPane mainContainer) {
        loadedConsoles = new HashSet<>();

        prefWidthProperty().bind(mainContainer.widthProperty());

        masonryPane = new JFXMasonryPane();
        masonryPane.prefWidthProperty().bind(widthProperty());

        State state = frontend.getState();
        setCenter(masonryPane);
        update(state);

        state.addStateListener(this);
    }

    @Override
    public void update(State state) {
        Set<Console> stateConsoles = state.getLoadedGames().stream()
                .map(game -> game.getMetadata().getConsole())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        if (loadedConsoles.equals(stateConsoles)) {
            return;
        }

        loadedConsoles = stateConsoles;

        state.clearStateListenerInstances(ConsoleFilterButton.class);

        masonryPane.getChildren().clear();
        for (Console console : stateConsoles) {
            ConsoleFilterButton cfButton = new ConsoleFilterButton(console, state);
            masonryPane.getChildren().add(cfButton);
        }
    }
}
