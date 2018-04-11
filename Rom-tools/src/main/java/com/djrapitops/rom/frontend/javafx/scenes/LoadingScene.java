package com.djrapitops.rom.frontend.javafx.scenes;

import com.djrapitops.rom.frontend.javafx.Variables;
import com.jfoenix.controls.JFXSpinner;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 * Loading window to display while the program starts.
 *
 * @author Rsl1122
 */
public class LoadingScene extends Scene {

    public LoadingScene() {
        super(createRoot(), Variables.WIDTH, Variables.LOADING_HEIGHT);
    }

    private static Parent createRoot() {
        BorderPane borderPane = new BorderPane();

        JFXSpinner spinner = new JFXSpinner();
        spinner.setRadius(50);
        borderPane.setCenter(spinner);

        Text text = new Text("Loading..");
        borderPane.setBottom(text);

        return borderPane;
    }
}
