package com.djrapitops.rom.backend;

import com.djrapitops.rom.backend.operations.FetchOperations;
import com.djrapitops.rom.backend.operations.RemoveOperations;
import com.djrapitops.rom.backend.operations.SaveOperations;
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
     * @return Object to use for the operation.
     */
    SaveOperations save();

    /**
     * Perform a Fetch operation.
     *
     * @return Object to use for the operation.
     */
    FetchOperations fetch();

    /**
     * Performs actions necessary to open the backend.
     *
     * @throws BackendException If the operation fails.
     */
    void open() throws BackendException;

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


    RemoveOperations remove();
}
