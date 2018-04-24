package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.Style;
import com.djrapitops.rom.frontend.javafx.views.Views;
import com.djrapitops.rom.frontend.state.Updatable;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 * Top bar navigation.
 *
 * @author Rsl1122
 */
public class MainNavigation extends HBox implements Updatable<Views> {

    private static final String ACTIVE_STYLE = Style.BG_DARK_GREEN + Style.BUTTON_SQUARE;
    private static final String INACTIVE_STYLE = Style.BG_GREEN + Style.BUTTON_SQUARE;

    private final JFXButton gamesButton;
    private final JFXButton toolsButton;
    private final JFXButton settingsButton;

    public MainNavigation(JavaFXFrontend frontend, Stage primaryStage) {
        gamesButton = new JFXButton("Games");
        toolsButton = new JFXButton("Tools");
        settingsButton = new JFXButton("Settings");

        gamesButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolsButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        settingsButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        prefWidthProperty().bind(primaryStage.widthProperty());
        gamesButton.prefWidthProperty().bind(this.widthProperty());
        toolsButton.prefWidthProperty().bind(this.widthProperty());
        settingsButton.prefWidthProperty().bind(this.widthProperty());

        gamesButton.setOnAction(e -> frontend.changeView(Views.GAMES));
        toolsButton.setOnAction(e -> frontend.changeView(Views.TOOLS));
        settingsButton.setOnAction(e -> frontend.changeView(Views.SETTINGS));

        getChildren().add(gamesButton);
        getChildren().add(toolsButton);
        getChildren().add(settingsButton);

        setAlignment(Pos.TOP_CENTER);

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
