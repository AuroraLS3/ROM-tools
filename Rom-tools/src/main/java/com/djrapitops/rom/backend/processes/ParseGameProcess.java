package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.game.*;
import com.djrapitops.rom.util.Verify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Process for adding a single game
 *
 * @author Rsl1122
 */
public class ParseGameProcess implements Callable<Game> {

    private final List<File> givenFiles;

    /**
     * Constructor for the process.
     *
     * @param givenFiles Files to add as a single game.
     * @throws IllegalArgumentException If given file list is empty.
     */
    public ParseGameProcess(List<File> givenFiles) {
        Verify.isFalse(givenFiles.isEmpty(), () -> new IllegalArgumentException("No Files given"));
        this.givenFiles = givenFiles;
    }

    /**
     * Attempt to get information about a game.
     *
     * @return Created game.
     * @throws Exception                If an exception occurs.
     * @throws IOException              If File can not be read for MD5 Checksum
     * @throws IllegalArgumentException If File extension is not supported
     */
    @Override
    public Game call() throws Exception {
        List<GameFile> gameFiles = new ArrayList<>();
        for (File givenFile : givenFiles) {
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
