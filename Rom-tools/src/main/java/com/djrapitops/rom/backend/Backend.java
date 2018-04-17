package com.djrapitops.rom.backend;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.backend.cache.GameCache;
import com.djrapitops.rom.backend.database.SQLDatabase;
import com.djrapitops.rom.backend.database.SQLiteDatabase;
import com.djrapitops.rom.backend.processes.FileProcesses;
import com.djrapitops.rom.backend.processes.GameProcesses;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.state.StateOperation;
import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.Metadata;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Main backend class in charge of initialization and management of other backend related objects and processes.
 *
 * @author Rsl1122
 */
public class Backend {

    private final SQLDatabase gameStorage;
    private final GameBackend gameBackend;

    private ExceptionHandler exceptionHandler;
    private boolean open = false;
    private Frontend frontend;

    public Backend() {
        gameStorage = new SQLiteDatabase();
        gameBackend = new GameCache(gameStorage);

        // Dummy Exception handler that logs to console if frontend doesn't set one.
        exceptionHandler = (level, throwable) -> throwable.printStackTrace();
    }

    public static Backend getInstance() {
        return Main.getBackend();
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

    /**
     * Open the Backend using a specific Frontend.
     *
     * @param frontend Frontend to use for updates.
     * @throws BackendException If Backend fails to open.
     */
    public void open(Frontend frontend) {
        try {
            this.frontend = frontend;
            gameBackend.open();
            open = true;

            Game fakeGame = new Game("Fakegame");
            fakeGame.setMetadata(Metadata.create().setName("Fake Game").setConsole(Console.GAMECUBE).build());
            Operations.GAME.save(fakeGame);
            Game fakeGame2 = new Game("Fakegame2");
            fakeGame2.setMetadata(Metadata.create().setName("Fake Game 2").setConsole(Console.GAMECUBE).build());
            Operations.GAME.save(fakeGame2);

            start();
        } catch (BackendException e) {
            if (e.getCause() instanceof SQLException && e.getCause().getMessage().contains("database is locked")) {
                throw new BackendException("Program is already running! Only one instance can run at a time.");
            } else {
                throw e;
            }
        }
    }

    private void start() {
        CompletableFuture<List<Game>> loaded = CompletableFuture.supplyAsync(GameProcesses::loadGames);

        loaded.thenAcceptAsync(games -> updateFrontend(state -> state.setLoadedGames(games)))
                .thenAccept(nothing -> setOpen(true));

        loaded.thenApplyAsync(FileProcesses::verifyFiles)
                .thenAccept(GameProcesses::removeGames)
                .thenApply(nothing -> GameProcesses.loadGames())
                .thenAccept(games -> updateFrontend(state -> state.setLoadedGames(games)));
    }

    private void updateFrontend(StateOperation stateOperation) {
        frontend.getState().performStateChange(stateOperation);
    }

    public void close() {
        gameBackend.close();
        open = false;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
