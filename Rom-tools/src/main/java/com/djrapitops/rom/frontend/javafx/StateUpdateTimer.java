package com.djrapitops.rom.frontend.javafx;

import com.djrapitops.rom.frontend.state.State;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Timer that prevents lag caused by large amounts of updates to the state.
 *
 * @author Rsl1122
 */
class StateUpdateTimer extends AnimationTimer {

    private final JavaFXFrontend frontend;
    private long lastUpdate = 0;

    StateUpdateTimer(JavaFXFrontend frontend) {
        this.frontend = frontend;
    }

    @Override
    public void handle(long now) {
        if (now - lastUpdate < 1000000) {
            return;
        }
        State state = frontend.getState();

        // New List to prevent Concurrent Exception due to GamesView adding more games that are updatable.
        List<Consumer<State>> elementsToUpdate = new ArrayList<>(state.getUpdateOnChange());

        elementsToUpdate.forEach(toUpdate -> toUpdate.accept(state));
        lastUpdate = now;
    }
}
