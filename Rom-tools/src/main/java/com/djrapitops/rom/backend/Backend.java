package com.djrapitops.rom.backend;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.backend.database.SQLiteDatabase;
import com.djrapitops.rom.backend.database.cache.GameCache;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.Metadata;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main backend class in charge of initialization and management of other backend related objects and processes.
 *
 * @author Rsl1122
 */
public class Backend {

    private final GameBackend gameStorage;
    private final GameBackend gameBackend;

    private final ExecutorService taskService;

    private ExceptionHandler exceptionHandler;

    public Backend() {
        gameStorage = new SQLiteDatabase();
        gameBackend = new GameCache(gameStorage);

        taskService = Executors.newFixedThreadPool(1);

        // Dummy Exception handler that logs to console if frontend doesn't set one.
        exceptionHandler = (level, throwable) -> {
            Logger.getGlobal().log(Level.SEVERE, "ERROR: " + level, throwable);
        };
    }

    public static Backend getInstance() {
        return Main.getBackend();
    }

    public <V> Future<V> submitTask(Callable<V> task) {
        return taskService.submit(task);
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
        Game fakeGame = new Game("Fakegame");
        fakeGame.setMetadata(Metadata.create().setName("Fake Game").setConsole(Console.GAMECUBE).build());
        gameBackend.save().saveGame(fakeGame);
    }

    public void close() {
        gameBackend.close();
        taskService.shutdown();
    }
}
