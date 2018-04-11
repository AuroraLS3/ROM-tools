package com.djrapitops.rom.frontend.javafx.scenes;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.updating.Updatable;
import javafx.scene.layout.BorderPane;

/**
 * Games view in the UI.
 *
 * @author Rsl1122
 */
public class SettingsView extends BorderPane implements Updatable<Integer> {

    private final JavaFXFrontend frontend;

    public SettingsView(JavaFXFrontend frontend, BorderPane mainContainer) {
        this.frontend = frontend;
        prefWidthProperty().bind(mainContainer.widthProperty());
    }

    /**
     * Updates GamesView to display a list of games.
     *
     * @param with Object used as parameters for the update.
     */
    @Override
    public void update(Integer with) {
        BorderPane container = new BorderPane();

        setCenter(container);
    }
}
