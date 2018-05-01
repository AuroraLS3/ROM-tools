package com.djrapitops.rom.backend.database;

import com.djrapitops.rom.backend.database.table.SQLTables;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.util.Wrapper;

import java.io.Serializable;
import java.util.Map;

/**
 * Abstract Data-Access-Object.
 * <p>
 * Used for implementing backend functionality to SQLDatabase.
 *
 * @author Rsl1122
 * @see SQLDatabase
 * @see SQLTables
 */
public interface DAO<T> {

    /**
     * Add an object to the backend.
     *
     * @param tables SQLTables to use for the save.
     * @param obj    Object to add.
     * @throws BackendException If backend fails to save.
     */
    void add(SQLTables tables, T obj);

    /**
     * Get an object with the Filter specified.
     *
     * @param tables SQLTables to use for the fetch.
     * @param filter Filter to reduce the selection of the DAO Selector.
     * @return T object.
     */
    T get(SQLTables tables, Filter filter);

    /**
     * Remove an object from the backend.
     *
     * @param tables SQLTables to use for the removal.
     * @param obj    Object to remove.
     */
    void remove(SQLTables tables, T obj);

    /**
     * Functional interface for a Wrapper that returns a Map.
     * <p>
     * Used for filtering the fetch result.
     */
    interface Filter extends Wrapper<Map<Integer, Serializable>> {

    }
}
