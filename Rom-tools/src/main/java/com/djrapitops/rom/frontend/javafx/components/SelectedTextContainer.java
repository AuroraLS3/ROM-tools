package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Simple box that displays how many games are selected.
 *
 * @author Rsl1122
 */
public class SelectedTextContainer extends BorderPane {

    public SelectedTextContainer(JavaFXFrontend frontend) {
        Text selectedText = new Text(frontend.getState().getSelectedGames().size() + " games selected");
        selectedText.setTextAlignment(TextAlignment.CENTER);
        setCenter(selectedText);
        setPrefHeight(25);
    }
}