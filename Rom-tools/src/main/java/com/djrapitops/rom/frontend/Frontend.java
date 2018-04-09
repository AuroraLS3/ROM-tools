package com.djrapitops.rom.frontend;

import com.djrapitops.rom.frontend.javafx.updating.UIUpdateProcess;
import com.djrapitops.rom.frontend.javafx.updating.Updatable;
import com.djrapitops.rom.game.Game;

import java.util.List;

/**
 * Represents a UI implementation.
 *
 * @author Rsl1122
 */
public interface Frontend extends Updatable<List<Game>> {
    UIUpdateProcess getUiUpdateProcess();
}
