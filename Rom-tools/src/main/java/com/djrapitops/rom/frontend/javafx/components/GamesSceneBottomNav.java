package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.backend.processes.GameParsing;
import com.djrapitops.rom.backend.processes.GameProcesses;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.scenes.GamesView;
import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.StateOperation;
import com.djrapitops.rom.frontend.state.Updatable;
import com.djrapitops.rom.game.Game;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

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

    public Pane getTopDrawer() {
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

    public Pane getBottomDrawer() {
        HBox container = new HBox();
        container.prefWidthProperty().bind(this.widthProperty());

        ObservableList<Node> children = container.getChildren();

        JFXButton addGames = new JFXButton("Add Games");
        addGames.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addGames.prefWidthProperty().bind(container.widthProperty());
        addGames.setOnAction(getAddGamesActionHandler());
        children.add(addGames);

        selectAll = new JFXButton("Select All");
        selectAll.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        selectAll.prefWidthProperty().bind(container.widthProperty());
        selectAll.setOnAction(getSelectAllActionHandler(selectAll));
        children.add(selectAll);

        JFXButton selectWithFilters = new JFXButton("Select with Filters");
        selectWithFilters.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        selectWithFilters.prefWidthProperty().bind(container.widthProperty());
        children.add(selectWithFilters);

        return container;
    }

    public EventHandler<ActionEvent> getAddGamesActionHandler() {
        return event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Game Files");
            List<File> chosen = fileChooser.showOpenMultipleDialog(frontend.getStage());

            CompletableFuture.supplyAsync(() -> chosen)
                    .thenApply(files -> {
                        try {
                            return GameParsing.parseGamesFromFiles(files);
                        } catch (IOException e) {
                            ExceptionHandler.handle(Level.WARNING, e);
                            return new ArrayList<Game>();
                        }
                    })
                    .thenAccept(GameProcesses::addGames)
                    .thenApply(nothing -> GameProcesses.loadGames())
                    .thenAccept(games -> updateState(state -> state.setLoadedGames(games)));
        };
    }

    private void updateState(StateOperation operation) {
        frontend.getState().performStateChange(operation);
    }

    public EventHandler<ActionEvent> getSelectAllActionHandler(JFXButton selectAll) {
        return event -> {
            if (frontend.getState().getLoadedGames().size() == frontend.getState().getSelectedGames().size()) {
                updateState(state -> state.setSelectedGames(new HashSet<>()));
            } else {
                updateState(state -> state.setSelectedGames(new HashSet<>(state.getLoadedGames())));
            }
        };
    }

    @Override
    public void update(State state) {
        if (frontend.getState().getLoadedGames().size() == frontend.getState().getSelectedGames().size()) {
            selectAll.setText("Unselect All");
        } else {
            selectAll.setText("Select All");
        }
    }
}
