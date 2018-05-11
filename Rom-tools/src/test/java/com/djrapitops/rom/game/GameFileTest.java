package com.djrapitops.rom.game;

import com.djrapitops.rom.MainTestingVariables;
import com.djrapitops.rom.exceptions.UnsupportedFileExtensionException;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import utils.fakeClasses.DummyBackend;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class GameFileTest {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void throwsExceptionWhenNoFileFormat() throws IOException {
        File noExtension = new File("noExtension");

        thrown.expect(UnsupportedFileExtensionException.class);
        thrown.expectMessage("File did not have a file format");

        // Throws
        new GameFile(noExtension);
    }

    @Test
    public void throwsExceptionWhenUnsupportedException() throws IOException {
        File wrongExtension = new File("wrongExtension.unsupported");

        thrown.expect(UnsupportedFileExtensionException.class);
        thrown.expectMessage("Unsupported extension");

        // Throws
        new GameFile(wrongExtension);
    }

    @Test
    public void throwsIOExceptionWhenCannotHash() throws IOException {
        // non-existing file will cause exception during MD5CheckSum
        File nonExistent = new File("nonExistent.gb");

        thrown.expect(IOException.class);
        // Can not use message due to FileNotFoundException containing localized text on some OS:es

        // Throws
        new GameFile(nonExistent);
    }

    @Test
    public void nonExistingFileWillNotMatchHashBecauseFileDoesNotExist() {
        GameFile gameFile = new GameFile(FileExtension.GB, new File("nonExistent.gb").getAbsolutePath(), "NoHash");
        assertFalse(gameFile.exists());
        assertFalse(gameFile.matchesHash());
    }

    @Test
    public void wrongHashFileWillNotMatchHash() throws IOException {
        File file = new File(temporaryFolder.getRoot(), "existing.gb");
        assert file.createNewFile();

        GameFile gameFile = new GameFile(FileExtension.GB, file.getAbsolutePath(), "WrongHash");
        assertTrue(gameFile.exists());
        assertFalse(gameFile.matchesHash());
    }

    @Test
    public void throwsIOExceptionWhenCannotMatchHash() {
        DummyBackend backend = new DummyBackend();
        MainTestingVariables.setBackend(backend);

        // folder will cause exception during MD5CheckSum
        File folder = new File(temporaryFolder.getRoot(), "folder");
        assert folder.mkdir();

        GameFile gameFile = new GameFile(FileExtension.GB, folder.getAbsolutePath(), "WrongHash");

        assertFalse(gameFile.matchesHash());
        assertEquals(1, backend.getThrown().size());
        assertTrue(backend.getThrown().get(0) instanceof IOException);
    }

}