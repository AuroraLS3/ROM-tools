package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Backend;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.frontend.state.StateOperation;
import com.djrapitops.rom.game.Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Class that contains process entry points.
 * <p>
 * These include processes run when a button is pressed or the program is started, etc.
 *
 * @author Rsl1122
 */
public class MainProcesses {

    public static void loadGamesFromBackendOnProgramStart() {
        CompletableFuture<List<Game>> loaded = CompletableFuture.supplyAsync(GameProcesses::loadGames);

        loaded.thenAcceptAsync(games -> updateState(state -> state.setLoadedGames(games)))
                .handle(ExceptionHandler.handle());

        // Verify loaded games' files.
        loaded.thenApplyAsync(FileProcesses::verifyFiles)
                .thenAccept(GameProcesses::removeGames)
                .thenApply(nothing -> GameProcesses.loadGames())
                .thenAccept(games -> updateState(state -> state.setLoadedGames(games)))
                .handle(ExceptionHandler.handle());
    }

    public static void processFilesGivenWhenAddingGames(List<File> chosenFiles) {
        CompletableFuture.supplyAsync(() -> chosenFiles)
                .thenApply(files -> {
                    try {
                        return GameParsing.parseGamesFromFiles(files);
                    } catch (IOException e) {
                        ExceptionHandler.handle(Level.WARNING, e);
                        return new ArrayList<Game>();
                    }
                })
                .thenAccept(GameProcesses::addGames)
                .thenApply(nothing -> GameProcesses.loadGames())
                .thenAccept(games -> updateState(state -> state.setLoadedGames(games)))
                .handle(ExceptionHandler.handle());
    }

    public static void processFolderGivenWhenAddingGames(File chosenFolder) {
        CompletableFuture.supplyAsync(() -> chosenFolder)
                .thenApply(file -> {
                    try {
                        return GameParsing.parseGamesFromFile(file);
                    } catch (IOException e) {
                        ExceptionHandler.handle(Level.WARNING, e);
                        return new ArrayList<Game>();
                    }
                })
                .thenAccept(GameProcesses::addGames)
                .thenApply(nothing -> GameProcesses.loadGames())
                .thenAccept(games -> updateState(state -> state.setLoadedGames(games)))
                .handle(ExceptionHandler.handle());
    }

    private static void updateState(StateOperation operation) {
        Backend.getInstance().getFrontend().getState().performStateChange(operation);
    }

}