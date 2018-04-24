package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.game.FileExtension;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import com.djrapitops.rom.util.file.FileTest;
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
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FileProcessesExceptionTest extends FileTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private File testfile1;

    @BeforeClass
    public static void setUpClass() {
        Main.setBackend(new DummyBackend());
    }

    @Before
    public void setUp() throws Exception {
        DummyBackend.get().clearThrown();
        // Here we create a folder and use it as a file.
        // Using folders like files usually causes IOExceptions.
        testfile1 = temporaryFolder.newFolder();
    }

    @Test
    public void fileProcessMoveNotifiesAboutIOExceptions() {
        assertFalse(FileProcesses.moveToSingleFolder(
                Collections.singletonList(createBrokenGame()),
                temporaryFolder.getRoot()
        ));
        assertEquals(1, DummyBackend.get().getThrown().size());
    }

    @Test
    public void fileProcessCopyNotifiesAboutIOExceptions() {
        assertFalse(FileProcesses.copyToSingleFolder(
                Collections.singletonList(createBrokenGame()),
                temporaryFolder.getRoot()
        ));
        assertEquals(1, DummyBackend.get().getThrown().size());
    }

    @Test
    public void extractionThrowsExceptionOnEmptyZip() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("net.lingala.zip4j.exception.ZipException: java.io.IOException: Negative seek offset");

        File emptyZipFile = getFile("empty_zip.zip");

        FileProcesses.extract(emptyZipFile, temporaryFolder.getRoot(), () -> "No Password");
    }

    @Test
    public void extractionThrowsExceptionOnDestinationBeingFolder() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Destination folder was not a folder");

        File emptyZipFile = getFile("empty_zip.zip");

        FileProcesses.extract(emptyZipFile, temporaryFolder.newFile(), () -> "No Password");
    }

    private Game createBrokenGame() {
        Game game = new Game("Testgame");
        GameFile file = new GameFile(FileExtension.GB, testfile1.getAbsolutePath(), "Hash");
        game.setGameFiles(Collections.singletonList(file));
        game.setMetadata(TestMetadata.createForTestGame());
        return game;
    }

}