package com.djrapitops.rom.frontend.javafx.views;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;
import javafx.scene.layout.BorderPane;

/**
 * Settings view in the UI.
 *
 * @author Rsl1122
 */
public class SettingsView extends BorderPane implements Updatable<State> {

    public SettingsView(JavaFXFrontend frontend, BorderPane mainContainer) {
        prefWidthProperty().bind(mainContainer.widthProperty());

        frontend.getState().addStateListener(this);
    }

    @Override
    public void update(State with) {
        BorderPane container = new BorderPane();

        setCenter(container);
    }
}
