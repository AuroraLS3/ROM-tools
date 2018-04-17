package com.djrapitops.rom.frontend;

import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;

/**
 * Represents a UI implementation.
 *
 * @author Rsl1122
 */
public interface Frontend extends Updatable<State> {

    State getState();
}
