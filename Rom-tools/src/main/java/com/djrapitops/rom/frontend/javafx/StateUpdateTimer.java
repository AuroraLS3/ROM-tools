package com.djrapitops.rom.frontend.javafx;

import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Timer that prevents lag caused by large amounts of updates to the state.
 *
 * @author Rsl1122
 */
public class StateUpdateTimer extends AnimationTimer {

    private final JavaFXFrontend frontend;
    private long lastUpdate = 0;

    public StateUpdateTimer(JavaFXFrontend frontend) {
        this.frontend = frontend;
    }

    @Override
    public void handle(long now) {
        if (now - lastUpdate < 1000000) {
            return;
        }
        State state = frontend.getState();

        List<Updatable<State>> elementsToUpdate = new ArrayList<>(state.getUpdateOnChange());
        Platform.runLater(() -> {
            // New List to prevent Concurrent Exception due to GamesView adding more games that are updatable.
            elementsToUpdate.forEach(toUpdate -> toUpdate.update(state));
        });
        lastUpdate = now;
    }
}
