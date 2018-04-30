package com.djrapitops.rom.backend.settings;

import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.util.file.FileTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SettingsFileTest extends FileTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private File file;

    @Before
    public void setUp() throws IOException {
        file = new File(temporaryFolder.newFolder(), "settings.conf");
    }

    @Test
    public void testSavingOfTheConfigFile() throws IOException {
        SettingsFile settingsFile = new SettingsFile(file);
        Map<Settings, Serializable> defaultValues = settingsFile.load();
        settingsFile.save(defaultValues);

        assertTrue(file.exists());
    }

    @Test
    public void testLoadingOfTheConfigFileValues() throws IOException {
        File testFile = getFile("test.conf");
        SettingsFile settingsFile = new SettingsFile(testFile);
        Map<Settings, Serializable> result = settingsFile.load();

        Map<Settings, Serializable> expected = new EnumMap<>(Settings.class);
        expected.put(Settings.FOLDER_ATARI_2600, "Testing");
        expected.put(Settings.FOLDER_ATARI_7800, 4000L);

        assertEquals(expected, result);
    }

    @Test
    public void misconfiguredConfigFileThrowsException() throws IOException {
        thrown.expect(BackendException.class);
        thrown.expectMessage("Configuration file has a wrong format. " +
                "Misconfigured line 1: FOLDER_ATARI_2600");

        File testFile = getFile("faulty_test.conf");
        SettingsFile settingsFile = new SettingsFile(testFile);

        // Throws
        settingsFile.load();
    }
}