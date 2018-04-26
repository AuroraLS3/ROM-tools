package com.djrapitops.rom.backend.settings;

import com.djrapitops.rom.backend.Backend;
import com.djrapitops.rom.exceptions.BackendException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * Class that is in charge of saving settings to a file.
 *
 * @author Rsl1122
 */
public class SettingsManager {

    private final Map<Settings, Serializable> settingValues;
    private final SettingsFile settingsFile;
    private boolean open = false;

    public SettingsManager(File file) {
        settingValues = new EnumMap<>(Settings.class);
        settingsFile = new SettingsFile(file);
    }

    public static SettingsManager getInstance() {
        return Backend.getInstance().getSettingsManager();
    }

    public void open() {
        try {
            settingsFile.load();
        } catch (IOException e) {
            throw new BackendException("Could not load settings from the settings file", e);
        }
        open = true;
    }

    Serializable getValue(Settings setting) {
        if (!open) {
            throw new IllegalStateException("Settings Manager has not been opened yet.");
        }
        return settingValues.get(setting);
    }
}
