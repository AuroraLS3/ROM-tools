package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.scenes.Views;
import com.djrapitops.rom.frontend.javafx.updating.Updatable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.util.Arrays;

/**
 * Top bar navigation.
 *
 * @author Rsl1122
 */
public class MainNavigation extends BorderPane implements Updatable<Views> {

    private static final String ACTIVE_STYLE = "-fx-background-color: '#ccc'";
    private static final String INACTIVE_STYLE = "-fx-background-color: '#fff'";
    private final Button gamesButton;
    private final Button toolsButton;
    private final Button settingsButton;

    public MainNavigation(JavaFXFrontend frontend) {
        gamesButton = new Button("Games");
        toolsButton = new Button("Tools");
        settingsButton = new Button("Settings");

        gamesButton.setOnAction(e -> frontend.changeView(Views.GAMES));
        toolsButton.setOnAction(e -> frontend.changeView(Views.TOOLS));
        settingsButton.setOnAction(e -> frontend.changeView(Views.SETTINGS));

        setLeft(gamesButton);
        setCenter(toolsButton);
        setRight(settingsButton);

        update(frontend.getCurrentView());
    }

    @Override
    public void update(Views with) {
        for (Button button : Arrays.asList(gamesButton, toolsButton, settingsButton)) {
            button.setStyle(INACTIVE_STYLE);
        }
        switch (with) {
            case GAMES:
                gamesButton.setStyle(ACTIVE_STYLE);
                break;
            case TOOLS:
                toolsButton.setStyle(ACTIVE_STYLE);
                break;
            case SETTINGS:
                settingsButton.setStyle(ACTIVE_STYLE);
                break;
            default:
                break;
        }
    }
}
