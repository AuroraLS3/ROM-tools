package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.scenes.GamesView;
import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.StateOperation;
import com.djrapitops.rom.frontend.state.Updatable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashSet;

/**
 * Bottom navigation for GamesView.
 *
 * @author Rsl1122
 * @see GamesView
 */
public class GamesSceneBottomNav extends VBox implements Updatable<State> {

    private final JavaFXFrontend frontend;
    private JFXButton selectAll;

    public GamesSceneBottomNav(JavaFXFrontend frontend) {
        this.frontend = frontend;
        getChildren().add(getTopDrawer());
        getChildren().add(getBottomDrawer());
    }

    private Pane getTopDrawer() {
        HBox container = new HBox();
        container.prefWidthProperty().bind(this.widthProperty());

        JFXTextField searchField = new JFXTextField();
        searchField.setLabelFloat(true);
        searchField.setPromptText("Search");
        container.getChildren().add(searchField);

        JFXButton filters = new JFXButton("Filters");

        searchField.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        filters.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        searchField.prefWidthProperty().bind(container.widthProperty());
        filters.prefWidthProperty().bind(container.widthProperty());

        container.getChildren().add(filters);
        return container;
    }

    private Pane getBottomDrawer() {
        HBox container = new HBox();
        container.prefWidthProperty().bind(this.widthProperty());

        ObservableList<Node> children = container.getChildren();

        AddGamesButton addGamesButton = new AddGamesButton(frontend);
        addGamesButton.prefWidthProperty().bind(container.widthProperty());
        children.add(addGamesButton);

        selectAll = new JFXButton("Select All");
        selectAll.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        selectAll.prefWidthProperty().bind(container.widthProperty());
        selectAll.setOnAction(getSelectAllActionHandler());
        children.add(selectAll);

        JFXButton selectWithFilters = new JFXButton("Select with Filters");
        selectWithFilters.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        selectWithFilters.prefWidthProperty().bind(container.widthProperty());
        children.add(selectWithFilters);

        return container;
    }

    private void updateState(StateOperation operation) {
        frontend.getState().performStateChange(operation);
    }

    private EventHandler<ActionEvent> getSelectAllActionHandler() {
        return event -> {
            if (!frontend.getState().getSelectedGames().isEmpty()) {
                updateState(state -> state.setSelectedGames(new HashSet<>()));
            } else {
                updateState(state -> state.setSelectedGames(new HashSet<>(state.getLoadedGames())));
            }
        };
    }

    @Override
    public void update(State state) {
        if (!frontend.getState().getSelectedGames().isEmpty()) {
            selectAll.setText("Remove Selection");
        } else {
            selectAll.setText("Select All");
        }
    }

}
