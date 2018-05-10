package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.Main;
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
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

/**
 * Class that contains process entry points.
 * <p>
 * These include processes run when a button is pressed or the program is started, etc.
 *
 * @author Rsl1122
 */
public class MainProcesses {

    private MainProcesses() {
        /* Hides Constructor */
    }

    /**
     * Perform actions that should be done when the program starts.
     * <p>
     * Loads games and verifies files of those loaded games.
     * <p>
     * Removes games with bad files.
     */
    public static void loadGamesFromBackendOnProgramStart() {
        ExecutorService execSvc = Main.getExecutorService();

        CompletableFuture<List<Game>> loaded = CompletableFuture.supplyAsync(GameProcesses::loadGames, execSvc);

        loaded.thenAcceptAsync(games -> updateState(state -> state.setLoadedGames(games)))
                .handle(ExceptionHandler.handle(Level.SEVERE));

        // Verify loaded games' files.
        loaded.thenApplyAsync(FileProcesses::verifyFiles, execSvc)
                .thenAccept(GameProcesses::removeGames)
                .thenApply(nothing -> GameProcesses.loadGames())
                .thenAccept(games -> updateState(state -> {
                    if (state.getLoadedGames().size() != games.size()) {
                        state.setLoadedGames(games);
                    }
                }))
                .handle(ExceptionHandler.handle());
    }

    /**
     * Processes given files into Games that are placed into the GameBackend.
     *
     * @param chosenFiles Files chosen by user.
     */
    public static void processFilesGivenWhenAddingGames(List<File> chosenFiles) {
        ExecutorService execSvc = Main.getExecutorService();

        CompletableFuture.supplyAsync(() -> chosenFiles, execSvc)
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
                .thenAccept(nothing -> FileProcesses.cleanFolder(new File("extracted")))
                .handle(ExceptionHandler.handle(Level.SEVERE));
    }

    /**
     * Processes given folder into Games that are placed into the GameBackend.
     *
     * @param chosenFolder Folder chosen by user.
     */
    public static void processFolderGivenWhenAddingGames(File chosenFolder) {
        File[] files = chosenFolder.listFiles();
        if (files == null) {
            return;
        }
        processFilesGivenWhenAddingGames(Arrays.asList(files));
    }

    /**
     * Processes file moving to a user given folder.
     *
     * @param chosenFolder  Folder to move game files to.
     * @param selectedGames Games which files need to be moved.
     */
    public static void processFileMoveToGivenFolder(File chosenFolder, List<Game> selectedGames) {
        if (selectedGames.isEmpty()) {
            return;
        }

        ExecutorService execSvc = Main.getExecutorService();
        CompletableFuture<List<Game>> gamesFuture = CompletableFuture.supplyAsync(() -> selectedGames, execSvc);

        gamesFuture.thenApplyAsync(games -> FileProcesses.moveToSingleFolder(games, chosenFolder), execSvc)
                .thenAccept(success -> Log.log(success ? "Moved files successfully." : "Some files could not be moved"))
                .handle(ExceptionHandler.handle(Level.SEVERE));
        gamesFuture.thenAcceptAsync(GameProcesses::removeGames, execSvc)
                .thenApply(nothing -> GameProcesses.loadGames())
                .thenAccept(games -> updateState(state -> state.setLoadedGames(games)))
                .handle(ExceptionHandler.handle(Level.SEVERE));
    }

    /**
     * Processes file copying to a user given folder.
     *
     * @param chosenFolder  Folder to move game files to.
     * @param selectedGames Games which files need to be copied4.
     */
    public static void processFileCopyToGivenFolder(File chosenFolder, List<Game> selectedGames) {
        if (selectedGames.isEmpty()) {
            return;
        }

        ExecutorService execSvc = Main.getExecutorService();
        CompletableFuture.supplyAsync(() -> selectedGames, execSvc)
                .thenApply(games -> FileProcesses.copyToSingleFolder(games, chosenFolder))
                .thenAccept(success -> Log.log(success ? "Copied files successfully." : "Some files could not be copied"))
                .handle(ExceptionHandler.handle(Level.SEVERE));
    }

    /**
     * Processes file moving to a user given folder in subfolders.
     *
     * @param chosenFolder  Folder to move game files to.
     * @param selectedGames Games which files need to be moved.
     */
    public static void processFileMoveToSubFolders(File chosenFolder, List<Game> selectedGames) {
        if (selectedGames.isEmpty()) {
            return;
        }

        ExecutorService execSvc = Main.getExecutorService();
        CompletableFuture<List<Game>> gamesFuture = CompletableFuture.supplyAsync(() -> selectedGames, execSvc);

        gamesFuture.thenApplyAsync(games -> FileProcesses.moveToSubFolders(games, chosenFolder), execSvc)
                .thenAccept(success -> Log.log(success ? "Moved files successfully." : "Some files could not be moved"))
                .handle(ExceptionHandler.handle(Level.SEVERE));
        gamesFuture.thenAcceptAsync(GameProcesses::removeGames, execSvc)
                .thenApply(nothing -> GameProcesses.loadGames())
                .thenAccept(games -> updateState(state -> state.setLoadedGames(games)))
                .handle(ExceptionHandler.handle(Level.SEVERE));
    }

    /**
     * Processes file copying to a user given folder in subfolders.
     *
     * @param chosenFolder  Folder to move game files to.
     * @param selectedGames Games which files need to be copied.
     */
    public static void processFileCopyToSubFolders(File chosenFolder, List<Game> selectedGames) {
        if (selectedGames.isEmpty()) {
            return;
        }

        ExecutorService execSvc = Main.getExecutorService();
        CompletableFuture<List<Game>> gamesFuture = CompletableFuture.supplyAsync(() -> selectedGames, execSvc);

        gamesFuture.thenApplyAsync(games -> FileProcesses.copyToSubFolders(games, chosenFolder), execSvc)
                .thenAccept(success -> Log.log(success ? "Copied files successfully." : "Some files could not be copied"))
                .handle(ExceptionHandler.handle(Level.SEVERE));
    }

    private static void updateState(StateOperation operation) {
        Backend.getInstance().getFrontend().getState().performStateChange(operation);
    }

}