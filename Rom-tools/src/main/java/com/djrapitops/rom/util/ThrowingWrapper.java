package com.djrapitops.rom.util;

/**
 * Utility for wrapping other objects to use lambdas.
 *
 * @author Rsl1122
 */
public interface ThrowingWrapper<T, K extends Throwable> {

    /**
     * Use the wrapped method to get the wanted object.
     *
     * @return Wanted object.
     * @throws K If the wrapped method throws an error.
     */
    T get() throws K;

}
