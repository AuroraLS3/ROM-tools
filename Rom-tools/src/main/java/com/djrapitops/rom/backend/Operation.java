package com.djrapitops.rom.backend;

import com.djrapitops.rom.backend.cache.Keys;
import com.djrapitops.rom.backend.database.DAO;
import com.djrapitops.rom.util.Wrapper;

/**
 * Operation uses a DAO for performing an operation, but contains useful variables for performing it.
 *
 * @author Rsl1122
 */
public class Operation<T> {

    private static final Wrapper<GameBackend> BACKEND_WRAPPER = () -> Backend.getInstance().getGameBackend();

    private final Wrapper<DAO<T>> dao;
    private final DAO.Filter filter;
    private final Keys key;

    public Operation(Wrapper<DAO<T>> dao, DAO.Filter filter, Keys key) {
        this.dao = dao;
        this.filter = filter;
        this.key = key;
    }

    public T get() {
        return BACKEND_WRAPPER.get().fetch(this);
    }

    public void save(T obj) {
        BACKEND_WRAPPER.get().save(this, obj);
    }

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