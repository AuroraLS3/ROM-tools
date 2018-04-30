package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Log;
import com.djrapitops.rom.backend.settings.Settings;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import com.djrapitops.rom.util.Wrapper;
import com.djrapitops.rom.util.file.ZipExtractor;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
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

    public static List<File> extract(File zipFile, File destinationFolder, Wrapper<String> password) {
        ZipExtractor zip = new ZipExtractor(zipFile, destinationFolder, password);
        try {
            zip.unzip();
            return Arrays.asList(Objects.requireNonNull(destinationFolder.listFiles()));
        } catch (ZipException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * Moves all files of a list of games to a single folder.
     *
     * @param games             Games of which files to move.
     * @param destinationFolder Folder to move to.
     * @return true if everything went smoothly, false if error(s) occurred
     */
    public static boolean moveToSingleFolder(List<Game> games, File destinationFolder) {
        List<File> files = games.stream().map(Game::getGameFiles)
                .flatMap(Collection::stream)
                .map(GameFile::getAbsolutePath)
                .map(File::new)
                .collect(Collectors.toList());

        boolean noErrors = true;
        for (File file : files) {
            try {
                FileUtils.moveFile(file, new File(destinationFolder, file.getName()));
            } catch (IOException e) {
                ExceptionHandler.handle(Level.WARNING, e);
                noErrors = false;
            }
        }
        return noErrors;
    }

    public static boolean copyToSingleFolder(List<Game> games, File destinationFolder) {
        List<File> files = games.stream().map(Game::getGameFiles)
                .flatMap(Collection::stream)
                .map(GameFile::getAbsolutePath)
                .map(File::new)
                .collect(Collectors.toList());

        boolean noErrors = true;
        for (File file : files) {
            try {
                FileUtils.copyFile(file, new File(destinationFolder, file.getName()));
            } catch (IOException e) {
                ExceptionHandler.handle(Level.WARNING, e);
                noErrors = false;
            }
        }
        return noErrors;
    }

    public static boolean moveToSubFolders(List<Game> games, File chosenFolder) {
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

            String subfolderName = setting.getString();
            File subfolder = new File(chosenFolder, subfolderName);

            List<File> files = entry.getValue();
            for (File file : files) {
                try {
                    FileUtils.moveFile(file, new File(subfolder, file.getName()));
                } catch (IOException e) {
                    ExceptionHandler.handle(Level.WARNING, e);
                    noErrors = false;
                }
            }
        }
        return noErrors;
    }

    public static boolean copyToSubFolders(List<Game> games, File chosenFolder) {
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

            String subfolderName = setting.getString();
            File subfolder = new File(chosenFolder, subfolderName);

            List<File> files = entry.getValue();
            for (File file : files) {
                try {
                    FileUtils.copyFile(file, new File(subfolder, file.getName()));
                } catch (IOException e) {
                    ExceptionHandler.handle(Level.WARNING, e);
                    noErrors = false;
                }
            }
        }
        return noErrors;
    }
}