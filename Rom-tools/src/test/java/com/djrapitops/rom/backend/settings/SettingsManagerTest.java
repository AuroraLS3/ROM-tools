package com.djrapitops.rom.backend.settings;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.util.file.FileTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import utils.fakeClasses.DummyBackend;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;

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
    public void testSettingsGetNumberMethod() {
        File testFile = getFile("test.conf");

        DummyBackend backend = new DummyBackend();
        SettingsManager settingsManager = new SettingsManager(testFile);
        backend.setSettingsManager(settingsManager);
        Main.setBackend(backend);

        settingsManager.open();

        assertEquals(4000, (int) Settings.FOLDER_ATARI_7800.getNumber());
    }

    @Test
    public void testSettingsGetNumberMethodThrowsException() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Value is not a number!");

        File testFile = getFile("test.conf");

        DummyBackend backend = new DummyBackend();
        SettingsManager settingsManager = new SettingsManager(testFile);
        backend.setSettingsManager(settingsManager);
        Main.setBackend(backend);

        settingsManager.open();

        assertEquals(String.class, Settings.FOLDER_ATARI_2600.getSettingClass());

        // Throws
        Settings.FOLDER_ATARI_2600.getNumber();
    }

    @Test
    public void testSettingsSaving() throws IOException {
        File testFile = new File(temporaryFolder.newFolder(), "test.conf");
        DummyBackend backend = new DummyBackend();
        SettingsManager settingsManager = new SettingsManager(testFile);
        backend.setSettingsManager(settingsManager);
        Main.setBackend(backend);

        settingsManager.open();

        String expected = "TESTVALUE";
        Settings.FOLDER_GBA.setValue(expected);
        settingsManager.save();

        settingsManager.open();

        assertEquals(expected, Settings.FOLDER_GBA.getString());
    }

}