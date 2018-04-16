package com.djrapitops.rom.backend;

import com.djrapitops.rom.exceptions.BackendException;

/**
 * Interface for game storage
 *
 * @author Rsl1122
 */
public interface GameBackend {

    /**
     * Perform a Save operation.
     *
     * @param op Operation object used to do the saving.
     */
    <T> void save(Operation<T> op, T obj);

    /**
     * Perform a Fetch operation.
     *
     * @return Result of the operation.
     */
    <T> T fetch(Operation<T> op);

    /**
     * Performs actions necessary to open the backend.
     *
     * @throws BackendException If the backend fails to open.
     */
    void open();

    /**
     * Check if backend is available.
     * <p>
     * If false try using {@code open}
     *
     * @return true/false
     * @see GameBackend#open()
     */
    boolean isOpen();

    /**
     * Performs actions related to closing the backend.
     */
    void close();

    <T> void remove(Operation<T> op, T obj);
}
