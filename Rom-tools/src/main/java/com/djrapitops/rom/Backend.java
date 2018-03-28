package com.djrapitops.rom;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.database.SQLiteDatabase;
import com.djrapitops.rom.backend.database.cache.GameCache;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.exceptions.ExceptionHandler;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Main backend class in charge of initialization and management of other backend related objects and processes.
 *
 * @author Rsl1122
 */
public class Backend {

    private final GameBackend gameStorage;
    private final GameBackend gameBackend;

    private final ExecutorService executorService;

    private ExceptionHandler exceptionHandler;

    public Backend() {
        gameStorage = new SQLiteDatabase();
        gameBackend = new GameCache(gameStorage);

        executorService = Executors.newFixedThreadPool(1);

        // Dummy Exception handler that logs to console if frontend doesn't set one.
        exceptionHandler = (level, throwable) -> {
            System.out.println("ERROR: " + level);
            throwable.printStackTrace();
        };
    }

    public static Backend getInstance() {
        return Main.getBackend();
    }

    public <V> Future<V> submitTask(Callable<V> task) {
        return executorService.submit(task);
    }

    public GameBackend getGameStorage() {
        return gameStorage;
    }

    public GameBackend getGameBackend() {
        return gameBackend;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void open() throws BackendException {
        gameBackend.open();
    }
}
