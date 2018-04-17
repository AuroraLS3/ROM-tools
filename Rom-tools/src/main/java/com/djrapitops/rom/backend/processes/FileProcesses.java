package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Log;
import com.djrapitops.rom.game.*;
import com.djrapitops.rom.util.Wrapper;
import com.djrapitops.rom.util.file.ZipExtractor;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        for (Game game : games) {
            for (GameFile gameFile : game.getGameFiles()) {
                if (!gameFile.matchesHash()) {
                    gamesWithChangedFiles.add(game);
                    break;
                }
            }
        }
        Log.log("Verified files of " + games.size() + " games.");
        return gamesWithChangedFiles;
    }

    public static List<File> extract(File zipFile, File destinationFolder, Wrapper<String> password) {
        ZipExtractor zip = new ZipExtractor(zipFile, destinationFolder, password);
        try {
            zip.unzip();
            File[] files = destinationFolder.listFiles();
            if (files != null) {
                return Arrays.asList(files);
            }
        } catch (ZipException e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException("No files extracted");
    }

    public static Game parseGame(List<File> files) throws IOException {
        List<GameFile> gameFiles = new ArrayList<>();
        for (File givenFile : files) {
            GameFile gameFile = new GameFile(givenFile);
            gameFiles.add(gameFile);
        }

        GameFile firstFile = gameFiles.get(0);
        String cleanName = firstFile.getCleanName();
        FileExtension extension = firstFile.getExtension();
        Console extConsole = extension.getConsole();
        Console console = extConsole.equals(Console.METADATA)
                ? Console.resolveFromFilename(firstFile.getFileName())
                : extConsole;
        Metadata metadata = Metadata.create()
                .setConsole(console)
                .setName(cleanName)
                .build();

        Game game = new Game(cleanName);
        game.setMetadata(metadata);
        game.setGameFiles(gameFiles);

        return game;
    }
}