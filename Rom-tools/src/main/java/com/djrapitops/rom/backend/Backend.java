package com.djrapitops.rom.backend;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.backend.cache.GameCache;
import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.SQLiteDatabase;
import com.djrapitops.rom.backend.processes.MainProcesses;
import com.djrapitops.rom.backend.settings.SettingsManager;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.frontend.Frontend;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Main backend class in charge of initialization and management of other backend related objects and processes.
 *
 * @author Rsl1122
 */
public class Backend {

    private SQLDatabase gameStorage;
    private GameBackend gameBackend;

    private ExceptionHandler exceptionHandler;
    private boolean open = false;
    private Frontend frontend;
    private SettingsManager settingsManager;

    public Backend() {
        gameStorage = new SQLiteDatabase();
        gameBackend = new GameCache(gameStorage);

        settingsManager = new SettingsManager(new File("settings.conf"));

        // Dummy Exception handler that logs to console if frontend doesn't set one.
        exceptionHandler = (level, throwable) -> Logger.getGlobal().log(level, throwable.getMessage(), throwable);
    }

    public static Backend getInstance() {
        return Main.getBackend();
    }

    public void setGameStorage(SQLDatabase gameStorage) {
        this.gameStorage = gameStorage;
        this.gameBackend = new GameCache(gameStorage);
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

    /**
     * Open the Backend using a specific Frontend.
     *
     * @param frontend Frontend to use for updates.
     * @throws BackendException If Backend fails to open.
     */
    public void open(Frontend frontend) {
        try {
            this.frontend = frontend;
            settingsManager.open();
            gameBackend.open();
            open = true;

            MainProcesses.loadGamesFromBackendOnProgramStart();
        } catch (BackendException e) {
            if (e.getCause() instanceof SQLException && e.getCause().getMessage().contains("database is locked")) {
                throw new BackendException("Program is already running! Only one instance can run at a time.");
            } else {
                throw e;
            }
        }
    }

    public void close() {
        Main.getExecutorService().shutdown();
        gameBackend.close();
        open = false;
    }

    public boolean isOpen() {
        return open;
    }

    public Frontend getFrontend() {
        return frontend;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}
