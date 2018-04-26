package utils;

import com.djrapitops.rom.game.FileExtension;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import com.djrapitops.rom.util.file.MD5CheckSum;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameCreationUtility {

    public static Game createTestGameWithTwoFakeFiles() {
        Game game = new Game("TestGame");
        GameFile file = new GameFile(FileExtension.GB, "Example Path", "Hash");
        GameFile file2 = new GameFile(FileExtension.GB, "Example Path2", "Hash2");
        game.setGameFiles(Arrays.asList(file, file2));
        game.setMetadata(MetadataCreationUtility.createForTestGame());
        return game;
    }

    public static Game createGameWithCorrectFileHash(File file) throws IOException {
        Game game = new Game("TestGame");
        GameFile gameFile = new GameFile(FileExtension.GB, file.getAbsolutePath(), new MD5CheckSum(file).toHash());
        game.setGameFiles(Collections.singletonList(gameFile));
        game.setMetadata(MetadataCreationUtility.createForTestGame());
        return game;
    }

    public static Game createGameWithIncorrectFileHash(File file) {
        Game game = new Game("TestGame");
        GameFile gameFile = new GameFile(FileExtension.GB, file.getAbsolutePath(), "Wrong Hash");
        game.setGameFiles(Collections.singletonList(gameFile));
        game.setMetadata(MetadataCreationUtility.createForTestGame());
        return game;
    }

    public static Game createTestGameWithFiles(String... filePaths) {
        Game game = new Game("TestGame");
        List<GameFile> gameFiles = Arrays.stream(filePaths)
                .map(path -> new GameFile(FileExtension.GB, path, "Hash"))
                .collect(Collectors.toList());
        game.setGameFiles(gameFiles);
        game.setMetadata(MetadataCreationUtility.createForTestGame());
        return game;
    }
}
