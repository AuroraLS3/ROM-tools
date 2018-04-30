package com.djrapitops.rom.backend.settings;

import com.djrapitops.rom.game.Console;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ConsoleFolderSettingsTest {

    @Test
    public void allConsolesHaveSettingsEnum() {
        for (Console console : Console.values()) {
            assertNotNull(Settings.valueOf("FOLDER_" + console.name()));
        }
    }
}