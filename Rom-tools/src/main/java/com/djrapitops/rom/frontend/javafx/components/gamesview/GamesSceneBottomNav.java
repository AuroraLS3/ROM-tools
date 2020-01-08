package com.djrapitops.rom.frontend.javafx.components.gamesview;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.Style;
import com.djrapitops.rom.frontend.javafx.views.GamesView;
import com.djrapitops.rom.frontend.javafx.views.Views;
import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashSet;
import java.util.function.Consumer;

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

        frontend.getState().addStateListener(this);
    }

    private Pane getTopDrawer() {
        HBox container = new HBox();
        container.prefWidthProperty().bind(this.widthProperty());

        SearchField searchField = new SearchField(frontend.getState());
        searchField.setStyle(Style.BUTTON_SQUARE + Style.BG_LIGHT_GRAY_CYAN + "-fx-prompt-text-fill: #fff;");
        container.getChildren().add(searchField);

        JFXButton filters = new JFXButton("Filters");
        filters.setStyle(Style.BUTTON_SQUARE + Style.BG_LIGHT_GRAY_CYAN);
        filters.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        filters.setOnAction(event -> frontend.changeView(Views.FILTERS));

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
        addGamesButton.setStyle(Style.BUTTON_SQUARE + Style.BG_DARK_CYAN);
        addGamesButton.prefWidthProperty().bind(container.widthProperty());
        children.add(addGamesButton);

        selectAll = new JFXButton("Select All");
        selectAll.setStyle(Style.BUTTON_SQUARE + Style.BG_DARK_CYAN);
        selectAll.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        selectAll.prefWidthProperty().bind(container.widthProperty());
        selectAll.setOnAction(getSelectAllActionHandler());
        children.add(selectAll);

        JFXButton selectWithFilters = new JFXButton("disabled button");
        selectWithFilters.setStyle(Style.BUTTON_SQUARE + Style.BG_DARK_CYAN);
        selectWithFilters.setDisable(true);
        selectWithFilters.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        selectWithFilters.prefWidthProperty().bind(container.widthProperty());
        children.add(selectWithFilters);

        return container;
    }

    private void updateState(Consumer<State> operation) {
        frontend.getState().performStateChange(operation);
    }

    private EventHandler<ActionEvent> getSelectAllActionHandler() {
        return event -> {
            if (!frontend.getState().getSelectedGames().isEmpty()) {
                updateState(state -> state.setSelectedGames(new HashSet<>()));
            } else {
                updateState(state -> state.setSelectedGames(new HashSet<>(state.getVisibleGames())));
            }
        };
    }

    @Override
    public void update(State state) {
        if (!frontend.getState().getSelectedGames().isEmpty()) {
            selectAll.setText("Remove Selection");
        } else {
            if (state.getSearch().isEmpty() && (state.hasFilteredConsoles())) {
                selectAll.setText("Select All");
            } else {
                selectAll.setText("Select Visible");
            }
        }
    }

}
