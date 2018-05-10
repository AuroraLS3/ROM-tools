package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Log;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.game.*;
import com.djrapitops.rom.util.TimeStamp;
import com.djrapitops.rom.util.Verify;
import com.djrapitops.rom.util.Wrapper;
import com.djrapitops.rom.util.file.ArchiveExtractor;

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

    /**
     * Parse games from a zip archive.
     *
     * @param archive zip file.
     * @return List of parsed games.
     * @throws IOException If file could not be read.
     */
    public static List<Game> parseGamesFromArchive(File archive) throws IOException {
        File extractFolder = new File("extracted");
        File destinationFolder = new File(extractFolder, archive.getName());
        while (destinationFolder.exists()) {
            String newName = destinationFolder.getName() + new TimeStamp(System.currentTimeMillis()).asFormatted();
            destinationFolder = new File(extractFolder, newName);
        }

        Wrapper<String> passwordWrapper = () -> {
            throw new UnsupportedOperationException("Passwords not yet supported");
        };
        List<File> extracted = FileProcesses.extract(archive, destinationFolder, passwordWrapper);
        return parseGamesFromFiles(extracted);
    }

    /**
     * Parse games from a single file, can be a folder or an archive.
     *
     * @param file File to parse.
     * @return List of Games that could be parsed from the file.
     * @throws IOException If file could not be read.
     */
    public static List<Game> parseGamesFromFile(File file) throws IOException {
        return parseGamesFromFiles(Collections.singleton(file));
    }

    /**
     * Parse games from given files.
     *
     * @param files Files to parse.
     * @return List of Games that could be parsed.
     * @throws IOException if file could not be read.
     */
    public static List<Game> parseGamesFromFiles(Collection<File> files) throws IOException {
        List<Game> games = new ArrayList<>();
        int i = 1;
        int size = files.size();
        for (File file : files) {
            String name = file.getName();
            Log.log("Processing files into games.. (" + i + "/" + size + ") " + name);
            if (file.isDirectory()) {
                games.addAll(parseGamesFromDir(file));
            } else if (ArchiveExtractor.isSupportedArchiveFile(name)) {
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
        return combineMultiFileGames(games);
    }

    private static List<Game> combineMultiFileGames(List<Game> parsedGames) {
        Map<Metadata, List<Game>> groupedByMetadata = groupByMetadata(parsedGames);
        List<Game> combined = combineGroupedGames(groupedByMetadata);
        Log.log("Combine results, before: " + parsedGames.size() + ", after: " + combined.size());
        Verify.isFalse(parsedGames.contains(null), () -> new IllegalStateException("Null in parsed games"));
        Verify.isFalse(combined.contains(null), () -> new IllegalStateException("Null in combined games"));
        return combined;
    }

    private static List<Game> combineGroupedGames(Map<Metadata, List<Game>> groupedByMetadata) {
        List<Game> combined = new ArrayList<>();
        int size = groupedByMetadata.size();
        int i = 1;
        for (Map.Entry<Metadata, List<Game>> entry : groupedByMetadata.entrySet()) {
            Log.log("Combining.. (" + i + "/" + size + "): " + entry.getKey().getName());
            List<Game> games = entry.getValue();

            Game first = null;
            for (Game game : games) {
                if (hasDiskFormatFiles(game.getGameFiles())) {
                    if (first == null) {
                        first = game;
                        continue;
                    }
                    first.getGameFiles().addAll(game.getGameFiles());
                } else {
                    combined.add(game);
                }
            }
            if (first != null) {
                combined.add(first);
            }
            i++;
        }
        return combined;
    }

    public static boolean hasDiskFormatFiles(Collection<GameFile> gameFiles) {
        return gameFiles.stream()
                .map(GameFile::getFileName)
                .anyMatch(fileName ->
                        fileName.toLowerCase().matches("(?i).*(track|disk|chip).[1-9][1-9]?.*")
                                || fileName.endsWith(".cue")
                                || fileName.endsWith(".bin")
                );
    }

    private static Map<Metadata, List<Game>> groupByMetadata(List<Game> parsedGames) {
        Map<Metadata, List<Game>> groupedByMetadata = new HashMap<>();

        for (Game game : parsedGames) {
            Metadata metadata = game.getMetadata();
            List<Game> sameName = groupedByMetadata.getOrDefault(metadata, new ArrayList<>());
            sameName.add(game);
            groupedByMetadata.put(metadata, sameName);
        }
        return groupedByMetadata;
    }

    private static List<Game> parseGamesFromDir(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        return parseGamesFromFiles(Arrays.asList(files));
    }

    /**
     * Parse a single game from given file.
     *
     * @param file File to parse into a game.
     * @return Game parsed from the file.
     * @throws IOException If the file could not be read.
     */
    public static Game parseGame(File file) throws IOException {
        return parseGame(Collections.singleton(file));
    }

    /**
     * Parse a single game from multiple files.
     *
     * @param files Files to parse into a game.
     * @return Game parsed from the files.
     * @throws IOException If a file could not be read.
     */
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

        Game game = new Game();
        game.setMetadata(metadata);
        game.setGameFiles(gameFiles);

        return game;
    }
}
