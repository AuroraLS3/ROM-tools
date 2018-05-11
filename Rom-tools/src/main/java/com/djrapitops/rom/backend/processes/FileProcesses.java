package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Log;
import com.djrapitops.rom.backend.settings.Settings;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import com.djrapitops.rom.util.MethodReference;
import com.djrapitops.rom.util.Verify;
import com.djrapitops.rom.util.Wrapper;
import com.djrapitops.rom.util.file.ArchiveExtractor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Class that holds methods for CompletableFuture related to Files.
 *
 * @author Rsl1122
 * @see java.util.concurrent.CompletableFuture
 */
public class FileProcesses {

    private FileProcesses() {
        /* Hides constructor */
    }

    /**
     * Verifies GameFiles of the given games.
     *
     * @param games Games to check files of.
     * @return List of games with changed/missing files.
     */
    public static List<Game> verifyFiles(List<Game> games) {
        Log.log("Verifying Files..");
        List<Game> gamesWithChangedFiles = new ArrayList<>();

        int i = 1;
        int size = games.size();
        for (Game game : new ArrayList<>(games)) {
            String gameName = game.getMetadata().getName();
            for (GameFile gameFile : game.getGameFiles()) {
                Log.log("Verifying Game File Locations (" + i + "/" + size + "): " + gameName);
                if (!gameFile.matchesHash()) {
                    gamesWithChangedFiles.add(game);
                    break;
                }
            }
            i++;
        }
        Log.log("Verified files of " + size + " games.");
        return gamesWithChangedFiles;
    }

    /**
     * Extract an archive file.
     *
     * @param archive           File to extract.
     * @param destinationFolder Folder to extract to.
     * @param password          Wrapper for a possible password.
     * @return List of the extracted files.
     */
    public static List<File> extract(File archive, File destinationFolder, Wrapper<String> password) {
        ArchiveExtractor.createExtractorFor(archive, destinationFolder, password).extract();
        return Arrays.asList(Objects.requireNonNull(destinationFolder.listFiles()));
    }

    /**
     * Moves all files of a list of games to a single folder.
     *
     * @param games             Games of which files to move.
     * @param destinationFolder Folder to move to.
     * @return true if everything went smoothly, false if error(s) occurred
     */
    public static boolean moveToSingleFolder(List<Game> games, File destinationFolder) {
        return performSingleFolderOperation(games, destinationFolder, FileUtils::moveFile);
    }

    /**
     * Copies all files of a list of games to a single folder.
     *
     * @param games             Games of which files to copy.
     * @param destinationFolder Folder to copy to.
     * @return true if everything went smoothly, false if error(s) occurred
     */
    public static boolean copyToSingleFolder(List<Game> games, File destinationFolder) {
        return performSingleFolderOperation(games, destinationFolder, FileUtils::copyFile);
    }

    private static boolean performSingleFolderOperation(List<Game> games, File destinationFolder,
                                                        MethodReference.ThrowingDual<File, File, IOException> method) {
        List<File> files = games.stream().map(Game::getGameFiles)
                .flatMap(Collection::stream)
                .map(GameFile::getAbsolutePath)
                .map(File::new)
                .collect(Collectors.toList());

        return performAnOperationOnFiles(destinationFolder, method, files);
    }

    private static boolean performAnOperationOnFiles(File destinationFolder, MethodReference.ThrowingDual<File, File, IOException> method, List<File> files) {
        boolean noErrors = true;
        for (File file : files) {
            try {
                method.call(file, new File(destinationFolder, file.getName()));
            } catch (IOException e) {
                ExceptionHandler.handle(Level.WARNING, e);
                noErrors = false;
            }
        }
        return noErrors;
    }

    /**
     * Moves all files of a list of games to a folder in subfolders.
     *
     * @param games        Games of which files to move.
     * @param chosenFolder Folder to move to.
     * @return true if everything went smoothly, false if error(s) occurred
     */
    public static boolean moveToSubFolders(List<Game> games, File chosenFolder) {
        return performSubfolderOperation(games, chosenFolder, FileUtils::moveFile);
    }

    /**
     * Copies all files of a list of games to a folder in subfolders.
     *
     * @param games        Games of which files to copy.
     * @param chosenFolder Folder to copy to.
     * @return true if everything went smoothly, false if error(s) occurred
     */
    public static boolean copyToSubFolders(List<Game> games, File chosenFolder) {
        return performSubfolderOperation(games, chosenFolder, FileUtils::copyFile);
    }

    private static boolean performSubfolderOperation(List<Game> games, File chosenFolder,
                                                     MethodReference.ThrowingDual<File, File, IOException> method) {
        Map<Console, List<File>> gameFiles = new EnumMap<>(Console.class);
        for (Game game : games) {
            Console console = game.getMetadata().getConsole();
            List<File> files = gameFiles.getOrDefault(console, new ArrayList<>());
            game.getGameFiles().stream()
                    .map(GameFile::getAbsolutePath)
                    .map(File::new)
                    .forEach(files::add);
            gameFiles.put(console, files);
        }

        boolean noErrors = true;
        for (Map.Entry<Console, List<File>> entry : gameFiles.entrySet()) {
            Console console = entry.getKey();
            Settings setting = Settings.valueOf("FOLDER_" + console.name());

            String subfolderName = setting.asString();
            if (!setting.isValidValue(subfolderName)) {
                subfolderName = setting.getDefaultValue().toString();
            }
            File subfolder = new File(chosenFolder, subfolderName);

            List<File> files = entry.getValue();
            if (!performAnOperationOnFiles(subfolder, method, files)) {
                noErrors = false;
            }
        }
        return noErrors;
    }

    /**
     * Method used for cleaning the folder of different archive files.
     *
     * @param folder Folder to clean.
     * @throws IllegalArgumentException If given folder is not folder.
     */
    public static void cleanFolder(File folder) {
        if (!folder.exists()) {
            return;
        }
        Verify.isTrue(folder.isDirectory(), () -> new IllegalArgumentException("Not a folder."));
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isFile() && ArchiveExtractor.isSupportedArchiveFile(file.getName())) {
                try {
                    Files.deleteIfExists(file.toPath());
                } catch (IOException e) {
                    ExceptionHandler.handle(Level.WARNING, e);
                }
            } else if (file.isDirectory()) {
                cleanFolder(file);
            }
        }
    }
}