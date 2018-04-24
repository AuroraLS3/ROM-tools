package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Backend;
import com.djrapitops.rom.backend.Log;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.frontend.state.StateOperation;
import com.djrapitops.rom.game.Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
                .thenAccept(games -> updateState(state -> {
                    if (state.getLoadedGames().size() != games.size()) {
                        state.setLoadedGames(games);
                    }
                }))
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
        File[] files = chosenFolder.listFiles();
        if (files == null) {
            return;
        }
        processFilesGivenWhenAddingGames(Arrays.asList(files));
    }

    public static void processFileMoveToGivenFolder(File chosenFolder) {
        List<Game> selectedGames = new ArrayList<>(Backend.getInstance().getFrontend().getState().getSelectedGames());

        if (selectedGames.isEmpty()) {
            return;
        }

        CompletableFuture<List<Game>> gamesFuture = CompletableFuture.supplyAsync(() -> selectedGames);

        gamesFuture.thenApplyAsync(games -> FileProcesses.moveToSingleFolder(games, chosenFolder))
                .thenAccept(success -> Log.log(success ? "Moved files successfully." : "Some files could not be moved"));
        gamesFuture.thenAcceptAsync(GameProcesses::removeGames)
                .thenApply(nothing -> GameProcesses.loadGames())
                .thenAccept(games -> updateState(state -> state.setLoadedGames(games)));
    }

    public static void processFileCopyToGivenFolder(File chosenFolder) {
        List<Game> selectedGames = new ArrayList<>(Backend.getInstance().getFrontend().getState().getSelectedGames());

        if (selectedGames.isEmpty()) {
            return;
        }

        CompletableFuture.supplyAsync(() -> selectedGames)
                .thenApply(games -> FileProcesses.copyToSingleFolder(games, chosenFolder))
                .thenAccept(success -> Log.log(success ? "Copied files successfully." : "Some files could not be copied"));
    }

    private static void updateState(StateOperation operation) {
        Backend.getInstance().getFrontend().getState().performStateChange(operation);
    }

}