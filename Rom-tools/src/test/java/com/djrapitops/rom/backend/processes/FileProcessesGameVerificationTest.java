package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.game.FileExtension;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import com.djrapitops.rom.util.file.FileTest;
import com.djrapitops.rom.util.file.MD5CheckSum;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import utils.fakeClasses.DummyBackend;
import utils.fakeClasses.TestMetadata;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileProcessesGameVerificationTest extends FileTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private File testFile1;
    private File testFile2;

    @BeforeClass
    public static void setUpClass() {
        Main.setBackend(new DummyBackend());
    }

    @Before
    public void setUp() throws Exception {
        DummyBackend.get().clearThrown();
        testFile1 = temporaryFolder.newFile();
        testFile2 = temporaryFolder.newFile();
    }

    @Test
    public void fileProcessMoveNotifiesAboutIOExceptions() throws IOException {
        Game gameWithWrongHash = createGameWithWrongHash();
        List<Game> expected = Collections.singletonList(gameWithWrongHash);

        List<Game> testWith = Arrays.asList(gameWithWrongHash, createGameWithCorrectHash());
        List<Game> result = FileProcesses.verifyFiles(testWith);
        assertEquals(expected, result);
    }

    private Game createGameWithWrongHash() {
        Game game = new Game("TestGame");
        GameFile file = new GameFile(FileExtension.GB, testFile1.getAbsolutePath(), "Hash");
        game.setGameFiles(Collections.singletonList(file));
        game.setMetadata(TestMetadata.createForTestGame());
        return game;
    }

    private Game createGameWithCorrectHash() throws IOException {
        Game game = new Game("TestGame");
        GameFile file = new GameFile(FileExtension.GB, testFile2.getAbsolutePath(), new MD5CheckSum(testFile2).toHash());
        game.setGameFiles(Collections.singletonList(file));
        game.setMetadata(TestMetadata.createForTestGame());
        return game;
    }

}