package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.game.FileExtension;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import com.djrapitops.rom.game.Metadata;
import com.djrapitops.rom.util.file.FileTest;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class MainProcessesFileMovingTest extends FileTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private String testfile1;
    private String testfile2;
    private File originalFolder;
    private File targetFolder;

    @BeforeClass
    public static void setUpClass() {
        Main.setExecutorService(Executors.newFixedThreadPool(10));
    }

    @Before
    public void setUp() throws Exception {
        originalFolder = temporaryFolder.newFolder();
        targetFolder = temporaryFolder.newFolder();

        File file1 = new File(originalFolder, "Testfile1.txt");
        File file2 = new File(originalFolder, "Testfile2.txt");

        if (!file1.createNewFile()) {
            throw new FileNotFoundException();
        }
        if (!file2.createNewFile()) {
            throw new FileNotFoundException();
        }

        testfile1 = file1.getAbsolutePath();
        testfile2 = file2.getAbsolutePath();
    }

    @Test
    public void testMoveToFolder() {
        MainProcesses.processFileMoveToGivenFolder(targetFolder, Collections.singletonList(createGame()));
        Awaitility.await()
                .atMost(2, TimeUnit.SECONDS)
                .until(() -> targetFolder.listFiles() != null && targetFolder.listFiles().length > 1);

        List<String> targetFileNames = getFileNamesInFolder(targetFolder);
        List<String> originalFileNames = getFileNamesInFolder(originalFolder);

        assertTrue(targetFileNames.contains("Testfile1.txt"));
        assertTrue(targetFileNames.contains("Testfile2.txt"));
        assertFalse(originalFileNames.contains("Testfile1.txt"));
        assertFalse(originalFileNames.contains("Testfile2.txt"));
    }

    @Test
    public void testCopyToFolder() {
        MainProcesses.processFileCopyToGivenFolder(targetFolder, Collections.singletonList(createGame()));
        Awaitility.await()
                .atMost(2, TimeUnit.SECONDS)
                .until(() -> targetFolder.listFiles() != null && targetFolder.listFiles().length > 1);

        List<String> targetFileNames = getFileNamesInFolder(targetFolder);
        List<String> originalFileNames = getFileNamesInFolder(originalFolder);

        assertTrue(targetFileNames.contains("Testfile1.txt"));
        assertTrue(targetFileNames.contains("Testfile2.txt"));
        assertTrue(originalFileNames.contains("Testfile1.txt"));
        assertTrue(originalFileNames.contains("Testfile2.txt"));
    }

    @Test
    public void testMoveToFolderEmptyListOfGames() {
        MainProcesses.processFileMoveToGivenFolder(targetFolder, Collections.emptyList());

        List<String> targetFileNames = getFileNamesInFolder(targetFolder);
        List<String> originalFileNames = getFileNamesInFolder(originalFolder);

        assertFalse(targetFileNames.contains("Testfile1.txt"));
        assertFalse(targetFileNames.contains("Testfile2.txt"));
        assertTrue(originalFileNames.contains("Testfile1.txt"));
        assertTrue(originalFileNames.contains("Testfile2.txt"));
    }

    @Test
    public void testCopyToFolderEmptyListOfGames() {
        MainProcesses.processFileCopyToGivenFolder(targetFolder, Collections.emptyList());

        List<String> targetFileNames = getFileNamesInFolder(targetFolder);
        List<String> originalFileNames = getFileNamesInFolder(originalFolder);

        assertFalse(targetFileNames.contains("Testfile1.txt"));
        assertFalse(targetFileNames.contains("Testfile2.txt"));
        assertTrue(originalFileNames.contains("Testfile1.txt"));
        assertTrue(originalFileNames.contains("Testfile2.txt"));
    }

    private List<String> getFileNamesInFolder(File folder) {
        File[] fileArray = folder.listFiles();
        assertNotNull(fileArray);
        return Arrays.stream(fileArray)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    private Game createGame() {
        Game game = new Game("Testgame");
        GameFile file = new GameFile(FileExtension.GB, testfile1, "Hash");
        GameFile file2 = new GameFile(FileExtension.GB, testfile2, "Hash2");
        Metadata metadata = Metadata.create().setConsole(FileExtension.GB).setName("Testgame").build();
        game.setGameFiles(Arrays.asList(file, file2));
        game.setMetadata(metadata);
        return game;
    }

}