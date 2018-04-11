package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.javafx.scenes.GamesView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Bottom navigation for GamesView.
 *
 * @author Rsl1122
 * @see GamesView
 */
public class GamesSceneBottomNav extends VBox {

    public GamesSceneBottomNav(Frontend frontend) {
        getChildren().add(getTopDrawer(frontend));
        getChildren().add(getBottomDrawer(frontend));
    }

    public Pane getTopDrawer(Frontend frontend) {
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

    public Pane getBottomDrawer(Frontend frontend) {
        HBox container = new HBox();
        container.prefWidthProperty().bind(this.widthProperty());

        JFXButton addGames = new JFXButton("Add Games");
        JFXButton selectAll = new JFXButton("Select All");
        JFXButton selectWithFilters = new JFXButton("Select with Filters");

        addGames.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        selectAll.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        selectWithFilters.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        addGames.prefWidthProperty().bind(container.widthProperty());
        selectAll.prefWidthProperty().bind(container.widthProperty());
        selectWithFilters.prefWidthProperty().bind(container.widthProperty());

        ObservableList<Node> children = container.getChildren();
        children.add(addGames);
        children.add(selectAll);
        children.add(selectWithFilters);
        return container;
    }
}
