package com.djrapitops.rom.frontend.state;

import java.util.function.Consumer;

/**
 * Interface for any components that can be updated using the results of Futures.
 *
 * @author Rsl1122
 * @see java.util.concurrent.Future
 */
public interface Updatable<T> extends Consumer<T> {

    @Override
    default void accept(T with) {
        update(with);
    }

    /**
     * Updates the component.
     *
     * @param with Object used as parameters for the update.
     */
    void update(T with);

}
