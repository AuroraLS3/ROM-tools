package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.backend.settings.Settings;
import com.djrapitops.rom.backend.settings.SettingsManager;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.util.file.FileTest;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import utils.GameCreationUtility;
import utils.fakeClasses.DummyBackend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class MainProcessesFileMovingTest extends FileTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private String testFile1;
    private String testFile2;
    private File originalFolder;
    private File targetFolder;

    @BeforeClass
    public static void setUpClass() {
        Main.setExecutorService(Executors.newFixedThreadPool(10));
    }

    @Before
    public void setUp() throws Exception {
        DummyBackend backend = new DummyBackend();
        SettingsManager settingsManager = new SettingsManager(temporaryFolder.newFile());
        settingsManager.open();
        backend.setSettingsManager(settingsManager);
        Main.setBackend(backend);

        originalFolder = temporaryFolder.newFolder();
        targetFolder = temporaryFolder.newFolder();

        File file1 = new File(originalFolder, "TestFile1.txt");
        File file2 = new File(originalFolder, "TestFile2.txt");

        if (!file1.createNewFile()) {
            throw new FileNotFoundException();
        }
        if (!file2.createNewFile()) {
            throw new FileNotFoundException();
        }

        testFile1 = file1.getAbsolutePath();
        testFile2 = file2.getAbsolutePath();
    }

    @Test
    public void testMoveToFolder() {
        MainProcesses.processFileMoveToGivenFolder(targetFolder, Collections.singletonList(createGame()));
        Awaitility.await()
                .atMost(2, TimeUnit.SECONDS)
                .until(() -> Objects.requireNonNull(targetFolder.listFiles()).length > 1);

        List<String> targetFileNames = getFileNamesInFolder(targetFolder);
        List<String> originalFileNames = getFileNamesInFolder(originalFolder);

        assertTrue(targetFileNames.contains("TestFile1.txt"));
        assertTrue(targetFileNames.contains("TestFile2.txt"));
        assertTrue(originalFileNames.toString(), originalFileNames.isEmpty());
    }

    @Test
    public void testCopyToFolder() {
        MainProcesses.processFileCopyToGivenFolder(targetFolder, Collections.singletonList(createGame()));
        Awaitility.await()
                .atMost(2, TimeUnit.SECONDS)
                .until(() -> Objects.requireNonNull(targetFolder.listFiles()).length > 1);

        List<String> targetFileNames = getFileNamesInFolder(targetFolder);
        List<String> originalFileNames = getFileNamesInFolder(originalFolder);

        assertTrue(targetFileNames.contains("TestFile1.txt"));
        assertTrue(targetFileNames.contains("TestFile2.txt"));
        assertTrue(originalFileNames.contains("TestFile1.txt"));
        assertTrue(originalFileNames.contains("TestFile2.txt"));
    }

    @Test
    public void testMoveToFolderEmptyListOfGames() {
        MainProcesses.processFileMoveToGivenFolder(targetFolder, Collections.emptyList());

        List<String> targetFileNames = getFileNamesInFolder(targetFolder);
        List<String> originalFileNames = getFileNamesInFolder(originalFolder);

        assertTrue(originalFileNames.toString(), targetFileNames.isEmpty());
        assertTrue(originalFileNames.contains("TestFile1.txt"));
        assertTrue(originalFileNames.contains("TestFile2.txt"));
    }

    @Test
    public void testCopyToFolderEmptyListOfGames() {
        MainProcesses.processFileCopyToGivenFolder(targetFolder, Collections.emptyList());

        List<String> targetFileNames = getFileNamesInFolder(targetFolder);
        List<String> originalFileNames = getFileNamesInFolder(originalFolder);

        assertTrue(originalFileNames.toString(), targetFileNames.isEmpty());
        assertTrue(originalFileNames.contains("TestFile1.txt"));
        assertTrue(originalFileNames.contains("TestFile2.txt"));
    }

    @Test
    public void testMoveToSubFolders() {
        assertTrue(SettingsManager.getInstance().isOpen());

        MainProcesses.processFileMoveToSubFolders(targetFolder, Collections.singletonList(createGame()));
        Awaitility.await()
                .atMost(2, TimeUnit.SECONDS)
                .until(() -> Objects.requireNonNull(targetFolder.listFiles()).length > 0);

        List<String> targetFileNames = getFileNamesInFolder(new File(targetFolder, Settings.FOLDER_GAMEBOY.asString()));
        List<String> originalFileNames = getFileNamesInFolder(originalFolder);

        assertTrue(targetFileNames.contains("TestFile1.txt"));
        assertTrue(targetFileNames.contains("TestFile2.txt"));
        assertTrue(originalFileNames.toString(), originalFileNames.isEmpty());
    }

    @Test
    public void testCopyToSubFolders() {
        assertTrue(SettingsManager.getInstance().isOpen());

        MainProcesses.processFileCopyToSubFolders(targetFolder, Collections.singletonList(createGame()));
        Awaitility.await()
                .atMost(2, TimeUnit.SECONDS)
                .until(() -> Objects.requireNonNull(targetFolder.listFiles()).length > 0);

        List<String> targetFileNames = getFileNamesInFolder(new File(targetFolder, Settings.FOLDER_GAMEBOY.asString()));
        List<String> originalFileNames = getFileNamesInFolder(originalFolder);

        assertTrue(targetFileNames.contains("TestFile1.txt"));
        assertTrue(targetFileNames.contains("TestFile2.txt"));
        assertTrue(originalFileNames.contains("TestFile1.txt"));
        assertTrue(originalFileNames.contains("TestFile2.txt"));
    }

    @Test
    public void testMoveToSubfoldersEmptyListOfGames() {
        MainProcesses.processFileMoveToSubFolders(targetFolder, Collections.emptyList());

        File subFolder = new File(targetFolder, Settings.FOLDER_GAMEBOY.asString());
        assertFalse(subFolder.exists());

        List<String> originalFileNames = getFileNamesInFolder(originalFolder);
        assertTrue(originalFileNames.contains("TestFile1.txt"));
        assertTrue(originalFileNames.contains("TestFile2.txt"));
    }

    @Test
    public void testCopyToSubfoldersEmptyListOfGames() {
        MainProcesses.processFileCopyToSubFolders(targetFolder, Collections.emptyList());

        File subFolder = new File(targetFolder, Settings.FOLDER_GAMEBOY.asString());
        assertFalse(subFolder.exists());

        List<String> originalFileNames = getFileNamesInFolder(originalFolder);
        assertTrue(originalFileNames.contains("TestFile1.txt"));
        assertTrue(originalFileNames.contains("TestFile2.txt"));
    }

    private List<String> getFileNamesInFolder(File folder) {
        File[] fileArray = folder.listFiles();
        assertNotNull(fileArray);
        return Arrays.stream(fileArray)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    private Game createGame() {
        return GameCreationUtility.createTestGameWithFiles(testFile1, testFile2);
    }

}