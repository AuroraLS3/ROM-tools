package com.djrapitops.rom.util;

/**
 * Interface for passing method references as function parameters.
 *
 * @author Rsl1122
 */
public interface MethodReference<T> {

    void call(T variable);

    interface Dual<T, K> {

        void call(T variable, K variable2);

    }

}

