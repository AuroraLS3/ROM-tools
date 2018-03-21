package com.djrapitops.rom.util;

/**
 * Utility for wrapping other objects to use lambdas.
 *
 * @author Rsl1122
 */
public interface Wrapper<T> {

    /**
     * Use the wrapped method to get the wanted object.
     *
     * @return Wanted object.
     */
    T get();

}
