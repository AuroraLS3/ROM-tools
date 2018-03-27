package com.djrapitops.rom.frontend.javafx.scenes;

import com.djrapitops.rom.frontend.javafx.Variables;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Games view in the UI.
 *
 * @author Rsl1122
 */
public class GamesScene extends Scene {

    public GamesScene(Stage primaryStage) {
        super(createRoot(primaryStage), Variables.WIDTH, Variables.HEIGHT);
    }

    private static Parent createRoot(Stage primaryStage) {
        return new BorderPane();
    }
}
