package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Progress information in the bottom of the program.
 *
 * @author Rsl1122
 */
public class ProcessBar extends HBox implements Updatable<State> {

    public ProcessBar(State state) {
        update(state);
        setPrefHeight(25);

        state.addStateListener(this);
    }

    @Override
    public void update(State state) {
        Text processInfo = new Text(state.getLoadingInformation());
        processInfo.setTextAlignment(TextAlignment.LEFT);
        getChildren().clear();
        getChildren().add(processInfo);
    }
}