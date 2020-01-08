package com.djrapitops.rom.frontend.state;

import java.util.function.Consumer;

/**
 * Interface for performing state changes.
 *
 * @author Rsl1122
 */
public interface StateOperation extends Consumer<State> {

    @Override
    default void accept(State state) {
        operateOnState(state);
    }

    /**
     * Performs an operation on State.
     *
     * @param state State to operate on.
     * @see State
     */
    void operateOnState(State state);

}
