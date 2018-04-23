package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Simple box that displays how many games are selected.
 *
 * @author Rsl1122
 */
public class SelectedTextContainer extends BorderPane implements Updatable<State> {

    public SelectedTextContainer(State state) {
        update(state);
        setPrefHeight(25);

        state.addStateListener(this);
    }

    @Override
    public void update(State state) {
        Text selectedText = new Text(state.getSelectedGames().size() + " games selected");
        selectedText.setTextAlignment(TextAlignment.CENTER);
        setCenter(selectedText);
    }
}