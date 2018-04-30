package com.djrapitops.rom.backend.settings;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.backend.Backend;
import com.djrapitops.rom.backend.Log;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.exceptions.ExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Class that is in charge of saving settings to a file.
 *
 * @author Rsl1122
 */
public class SettingsManager {

    private final SettingsFile settingsFile;

    private Map<Settings, Serializable> settingValues;
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
            settingValues = settingsFile.load();
        } catch (IOException | UncheckedIOException e) {
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

    public void setValue(Settings settings, Serializable value) {
        settingValues.put(settings, value);
    }

    public void save() {
        CompletableFuture.supplyAsync(() -> settingValues, Main.getExecutorService())
                .thenAccept(values -> {
                    try {
                        Log.log("Saving settings..");
                        settingsFile.save(values);
                        Log.log("Settings saved successfully.");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).handle(ExceptionHandler.handle(Level.SEVERE));
    }
}
