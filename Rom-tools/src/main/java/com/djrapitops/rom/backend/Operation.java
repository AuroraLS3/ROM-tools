package com.djrapitops.rom.backend;

import com.djrapitops.rom.backend.cache.Keys;
import com.djrapitops.rom.backend.database.DAO;
import com.djrapitops.rom.util.Wrapper;

/**
 * Class for performing game backend operations.
 * <p>
 * Operation uses a DAO for performing an operation, but contains useful variables for performing it.
 *
 * @param <T> Type of the object manipulated by the operation.
 * @author Rsl1122
 */
public class Operation<T> {

    private static final Wrapper<GameBackend> BACKEND_WRAPPER = () -> Backend.getInstance().getGameBackend();

    private final Wrapper<DAO<T>> dao;
    private final DAO.Filter filter;
    private final Keys key;

    /**
     * Constructor.
     *
     * @param dao    DAO to use for backend functionality.
     * @param filter Filter for fetch functionality.
     * @param key    Key for caching functionality.
     */
    public Operation(Wrapper<DAO<T>> dao, DAO.Filter filter, Keys key) {
        this.dao = dao;
        this.filter = filter;
        this.key = key;
    }

    /**
     * Get the object from the GameBackend.
     *
     * @return Object returned by the backend.
     */
    public T get() {
        return BACKEND_WRAPPER.get().fetch(this);
    }

    /**
     * Save the object to the GameBackend.
     *
     * @param obj Object to save.
     */
    public void save(T obj) {
        BACKEND_WRAPPER.get().save(this, obj);
    }

    /**
     * Remove the object from the GameBackend.
     *
     * @param obj Object to remove.
     */
    public void remove(T obj) {
        BACKEND_WRAPPER.get().remove(this, obj);
    }

    public DAO<T> getDao() {
        return dao.get();
    }

    public DAO.Filter getFilter() {
        return filter;
    }

    public Keys getKey() {
        return key;
    }
}