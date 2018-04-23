package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Log;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.game.*;
import com.djrapitops.rom.util.Wrapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

/**
 * Class responsible for parsing files into Game objects.
 *
 * @author Rsl1122
 */
public class GameParsing {

    private GameParsing() {
        /* Hides Constructor */
    }

    public static List<Game> parseGamesFromArchive(File archive) throws IOException {
        File destinationFolder = new File("extracted");
        Wrapper<String> passwordWrapper = () -> {
            throw new UnsupportedOperationException("Passwords not yet supported");
        };
        List<File> extracted = FileProcesses.extract(archive, destinationFolder, passwordWrapper);
        return parseGamesFromFiles(extracted);
    }

    public static List<Game> parseGamesFromFile(File file) throws IOException {
        return parseGamesFromFiles(Collections.singleton(file));
    }

    public static List<Game> parseGamesFromFiles(Collection<File> files) throws IOException {
        // TODO Handle cases where games require multiple files to work.
        List<Game> games = new ArrayList<>();
        int i = 1;
        int size = files.size();
        for (File file : files) {
            Log.log("Processing files into games.. (" + i + "/" + size + ")");
            String name = file.getName();
            if (file.isDirectory()) {
                games.addAll(parseGamesFromDir(file));
            } else if (name.endsWith(".zip")) {
                games.addAll(parseGamesFromArchive(file));
            } else {
                try {
                    games.add(parseGame(file));
                } catch (IllegalArgumentException e) {
                    // Unsupported file format
                    ExceptionHandler.handle(Level.WARNING, e);
                }
            }
            i++;
        }
        Log.log("Processed " + size + " files.");
        return games;
    }

    private static List<Game> parseGamesFromDir(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            return new ArrayList<>();
        }
        return parseGamesFromFiles(Arrays.asList(files));
    }

    public static Game parseGame(File file) throws IOException {
        return parseGame(Collections.singleton(file));
    }

    public static Game parseGame(Collection<File> files) throws IOException {
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
