package com.djrapitops.rom.util;

/**
 * Callback utility for using with UI processes.
 *
 * @author Rsl1122
 */
public interface Callback<T> {

    /**
     * Used for returning result for the callback after required tasks have finished.
     *
     * @param result Result of the callback.
     */
    void result(T result);

}
