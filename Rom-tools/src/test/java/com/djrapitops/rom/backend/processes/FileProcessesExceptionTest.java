package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.MainTestingVariables;
import com.djrapitops.rom.backend.settings.SettingsManager;
import com.djrapitops.rom.exceptions.ExtractionException;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.util.file.FileTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import utils.GameCreationUtility;
import utils.fakeClasses.DummyBackend;

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

    private File testFile;

    @Before
    public void setUp() throws Exception {
        DummyBackend backend = new DummyBackend();
        SettingsManager settingsManager = new SettingsManager(temporaryFolder.newFile());
        settingsManager.open();
        backend.setSettingsManager(settingsManager);
        MainTestingVariables.setBackend(backend);

        // Here we create a folder and use it as a file.
        // Using folders like files usually causes IOExceptions.
        testFile = temporaryFolder.newFolder();
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
    public void fileProcessMoveSubfoldersNotifiesAboutIOExceptions() {
        assertFalse(FileProcesses.moveToSubFolders(
                Collections.singletonList(createBrokenGame()),
                temporaryFolder.getRoot()
        ));
        assertEquals(1, DummyBackend.get().getThrown().size());
    }

    @Test
    public void fileProcessCopySubfoldersNotifiesAboutIOExceptions() {
        assertFalse(FileProcesses.copyToSubFolders(
                Collections.singletonList(createBrokenGame()),
                temporaryFolder.getRoot()
        ));
        assertEquals(1, DummyBackend.get().getThrown().size());
    }

    @Test
    public void extractionThrowsExceptionOnEmptyZip() {
        File emptyZipFile = getFile("archives/empty_zip.zip");

        thrown.expect(ExtractionException.class);
        thrown.expectMessage("Failed to extract: " +
                "net.lingala.zip4j.exception.ZipException: java.io.IOException: Negative seek offset");

        FileProcesses.extract(emptyZipFile, temporaryFolder.getRoot(), () -> "No Password");
    }

    @Test
    public void extractionThrowsExceptionOnDestinationBeingFolder() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Destination folder was not a folder");

        File emptyZipFile = getFile("archives/empty_zip.zip");

        FileProcesses.extract(emptyZipFile, temporaryFolder.newFile(), () -> "No Password");
    }

    @Test
    public void cleanThrowsExceptionOnParameterBeingAFile() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Not a folder.");

        FileProcesses.cleanFolder(temporaryFolder.newFile());
    }

    private Game createBrokenGame() {
        return GameCreationUtility.createGameWithIncorrectFileHash(testFile);
    }

}