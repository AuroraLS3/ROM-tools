package com.djrapitops.rom.frontend.javafx.components.gamesview;

import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.paint.Paint;

/**
 * GamesView component for filtering displayed games.
 *
 * @author Rsl1122
 */
public class SearchField extends JFXTextField implements Updatable<State> {

    private String text = "";

    public SearchField(State state) {
        setLabelFloat(true);
        setPromptText("Search");
        setUnFocusColor(Paint.valueOf("#fff"));
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        textProperty().addListener((observable, oldValue, newValue) -> {
            if (text.equals(newValue)) {
                return;
            }
            text = newValue;
            state.performStateChange(currentState -> currentState.setSearch(newValue));
        });

        state.addStateListener(this);
    }

    @Override
    public void update(State with) {
        String newValue = with.getSearch();
        if (text.equals(newValue)) {
            return;
        }
        text = newValue;
        setText(newValue);
    }
}
