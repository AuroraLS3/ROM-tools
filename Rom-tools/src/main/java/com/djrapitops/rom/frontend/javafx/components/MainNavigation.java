package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.scenes.Views;
import com.djrapitops.rom.frontend.javafx.updating.Updatable;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.Arrays;

/**
 * Top bar navigation.
 *
 * @author Rsl1122
 */
public class MainNavigation extends HBox implements Updatable<Views> {

    private static final String ACTIVE_STYLE = "-fx-background-color: '#ccc'";
    private static final String INACTIVE_STYLE = "-fx-background-color: '#fff'";
    private final Button gamesButton;
    private final Button toolsButton;
    private final Button settingsButton;

    public MainNavigation(JavaFXFrontend frontend) {
        setAlignment(Pos.CENTER);

        gamesButton = new Button("Games");
        toolsButton = new Button("Tools");
        settingsButton = new Button("Settings");

        gamesButton.setOnAction(e -> frontend.changeView(Views.GAMES));
        toolsButton.setOnAction(e -> frontend.changeView(Views.TOOLS));
        settingsButton.setOnAction(e -> frontend.changeView(Views.SETTINGS));

        ObservableList<Node> children = getChildren();
        children.add(gamesButton);
        children.add(toolsButton);
        children.add(settingsButton);

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
        }
    }
}
