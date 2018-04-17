package com.djrapitops.rom.frontend.state;

/**
 * Interface for performing state changes.
 *
 * @author Rsl1122
 */
public interface StateOperation {

    /**
     * Performs an operation on State.
     *
     * @param state State to operate on.
     * @see State
     */
    void operateOnState(State state);

}
