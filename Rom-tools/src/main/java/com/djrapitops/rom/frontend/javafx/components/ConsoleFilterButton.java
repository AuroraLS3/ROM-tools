package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.javafx.Style;
import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;
import com.djrapitops.rom.game.Console;
import com.jfoenix.controls.JFXButton;

import java.util.Set;

/**
 * Button that changes Console filter status.
 *
 * @author Rsl1122
 */
public class ConsoleFilterButton extends JFXButton implements Updatable<State> {

    private final Console console;
    private boolean active = true;

    public ConsoleFilterButton(Console console, State state) {
        super(console.getFullName());
        this.console = console;

        setOnAction(event -> state.performStateChange(currentState -> {
            if (active) {
                currentState.deactivateConsoleFilter(console);
            } else {
                currentState.activateConsoleFilter(console);
            }
        }));

        update(state);
        state.addStateListener(this);
    }

    @Override
    public void update(State state) {
        Set<Console> displayedConsoles = state.getDisplayedConsoles();
        active = displayedConsoles.isEmpty() || displayedConsoles.contains(console);

        setStyle(
                Style.BUTTON_SQUARE + (active ? Style.BG_LIGHT_GREEN : Style.BG_LIGHT_GRAY_CYAN)
        );
    }
}
