package com.djrapitops.rom.backend.settings;

import com.djrapitops.rom.MainTestingVariables;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.util.file.FileTest;
import org.awaitility.Awaitility;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import utils.fakeClasses.DummyBackend;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SettingsManagerTest extends FileTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testOpenLoadsConfigValues() {
        File testFile = getFile("test.conf");
        SettingsManager settingsManager = new SettingsManager(testFile);

        settingsManager.open();

        Serializable expected = "Testing";
        Serializable result = settingsManager.getValue(Settings.FOLDER_ATARI_2600);
        assertEquals(expected, result);
    }

    @Test
    public void testOpenThrowsBackendExceptionOnIOException() throws IOException {
        thrown.expect(BackendException.class);
        thrown.expectMessage("Could not load settings from the settings file");

        File testFile = temporaryFolder.newFolder();
        SettingsManager settingsManager = new SettingsManager(testFile);

        // Throws
        settingsManager.open();
    }

    @Test
    public void prematureSettingCallThrowsIllegalStateException() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Settings Manager has not been opened yet.");

        SettingsManager settingsManager = new SettingsManager(null);

        // Throws
        settingsManager.getValue(Settings.FOLDER_ATARI_2600);
    }

    @Test
    public void testSettingAsNumberMethod() {
        openSettingsManager();

        assertEquals(4000, (int) Settings.FOLDER_ATARI_7800.asNumber());
    }

    private void openSettingsManager() {
        File testFile = getFile("test.conf");

        DummyBackend backend = new DummyBackend();
        SettingsManager settingsManager = new SettingsManager(testFile);
        backend.setSettingsManager(settingsManager);
        MainTestingVariables.setBackend(backend);

        settingsManager.open();
    }

    @Test
    public void testSettingAsBooleanMethod() {
        openSettingsManager();

        assertFalse(Settings.FOLDER_GAMEBOY_COLOR.asBoolean());
        assertFalse(Settings.FOLDER_ATARI_2600.asBoolean());
        assertTrue(Settings.FOLDER_GAMEBOY.asBoolean());
    }

    @Test
    public void testSettingAsNumberMethodThrowsException() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Value is not a number!");

        openSettingsManager();

        assertEquals(String.class, Settings.FOLDER_ATARI_2600.getSettingClass());

        // Throws
        Settings.FOLDER_ATARI_2600.asNumber();
    }

    @Test
    public void testSettingsSaving() throws IOException {
        File testFile = new File(temporaryFolder.newFolder(), "test.conf");
        DummyBackend backend = new DummyBackend();
        SettingsManager settingsManager = new SettingsManager(testFile);
        backend.setSettingsManager(settingsManager);
        MainTestingVariables.setBackend(backend);
        MainTestingVariables.setExecutorService(Executors.newFixedThreadPool(10));

        settingsManager.open();

        String expected = "TESTVALUE";
        Settings.FOLDER_GBA.setValue(expected);
        settingsManager.save();
        Awaitility.await()
                .atMost(2, TimeUnit.SECONDS)
                .until(() -> expected.equals(Settings.FOLDER_GBA.asString()));

        settingsManager.open();

        assertEquals(expected, Settings.FOLDER_GBA.asString());
    }

}