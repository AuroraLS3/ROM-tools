package com.djrapitops.rom.backend;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.backend.cache.GameCache;
import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.SQLiteDatabase;
import com.djrapitops.rom.backend.processes.StartProcess;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.Metadata;

import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Main backend class in charge of initialization and management of other backend related objects and processes.
 *
 * @author Rsl1122
 */
public class Backend {

    private final SQLDatabase gameStorage;
    private final GameBackend gameBackend;

    private final ExecutorService taskService;

    private ExceptionHandler exceptionHandler;
    private boolean open = false;

    public Backend() {
        gameStorage = new SQLiteDatabase();
        gameBackend = new GameCache(gameStorage);

        taskService = Executors.newFixedThreadPool(5);

        // Dummy Exception handler that logs to console if frontend doesn't set one.
        exceptionHandler = (level, throwable) -> {
            Logger.getGlobal().log(level, throwable.getMessage(), throwable);
        };
    }

    public static Backend getInstance() {
        return Main.getBackend();
    }

    public <V> Future<V> submitTask(Callable<V> task) {
        return taskService.submit(task);
    }

    public Future submitTask(Runnable task) {
        return taskService.submit(task);
    }

    public SQLDatabase getGameStorage() {
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

    public void open(Frontend frontend) throws BackendException {
        try {
            gameBackend.open();
            Game fakeGame = new Game("Fakegame");
            fakeGame.setMetadata(Metadata.create().setName("Fake Game").setConsole(Console.GAMECUBE).build());
            Operations.GAME.save(fakeGame);
            Game fakeGame2 = new Game("Fakegame2");
            fakeGame2.setMetadata(Metadata.create().setName("Fake Game 2").setConsole(Console.GAMECUBE).build());
            Operations.GAME.save(fakeGame2);

            submitTask(new StartProcess(this, frontend));
        } catch (BackendException e) {
            if (e.getCause() instanceof SQLException && e.getCause().getMessage().contains("database is locked")) {
                throw new BackendException("Program is already running! Only one instance can run at a time.");
            } else {
                throw e;
            }
        }
    }

    public void close() {
        gameBackend.close();
        taskService.shutdown();
        open = false;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
