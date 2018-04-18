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
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
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

        MenuItem addGameFiles = new MenuItem("Add Files");
        addGameFiles.setOnAction(getAddGameFilesActionHandler());
        MenuItem addGameFolders = new MenuItem("Add Folder");
        addGameFolders.setOnAction(getAddGameFoldersActionHandler());

        SplitMenuButton addGamesChoices = new SplitMenuButton();
//        addGamesChoices.setSkin(new JFXSplitMenuButtonSkin(addGamesChoices));
        addGamesChoices.prefWidthProperty().bind(container.widthProperty());
        addGamesChoices.setText("Add Games");
        addGamesChoices.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addGamesChoices.setOnAction(getAddGameFilesActionHandler());
        addGamesChoices.getItems().add(addGameFiles);
        addGamesChoices.getItems().add(addGameFolders);
        // Removes the blue "on focus" box around the list
        addGamesChoices.setFocusTraversable(false);

        children.add(addGamesChoices);

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

    public EventHandler<ActionEvent> getAddGameFilesActionHandler() {
        return event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Game Files");
            List<File> chosen = fileChooser.showOpenMultipleDialog(frontend.getStage());

            if (chosen == null) {
                return;
            }

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
                    .thenAccept(games -> updateState(state -> state.setLoadedGames(games)))
                    .handle(ExceptionHandler.handle());
        };
    }

    private void updateState(StateOperation operation) {
        frontend.getState().performStateChange(operation);
    }

    public EventHandler<ActionEvent> getSelectAllActionHandler(JFXButton selectAll) {
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
            selectAll.setText("Unselect All");
        } else {
            selectAll.setText("Select All");
        }
    }

    public EventHandler<ActionEvent> getAddGameFoldersActionHandler() {
        return event -> {
            DirectoryChooser fileChooser = new DirectoryChooser();
            fileChooser.setTitle("Select Folder to search recursively");
            File chosenFolder = fileChooser.showDialog(frontend.getStage().getOwner());

            if (chosenFolder == null) {
                return;
            }

            CompletableFuture.supplyAsync(() -> chosenFolder)
                    .thenApply(file -> {
                        try {
                            return GameParsing.parseGamesFromFile(file);
                        } catch (IOException e) {
                            ExceptionHandler.handle(Level.WARNING, e);
                            return new ArrayList<Game>();
                        }
                    })
                    .thenAccept(GameProcesses::addGames)
                    .thenApply(nothing -> GameProcesses.loadGames())
                    .thenAccept(games -> updateState(state -> state.setLoadedGames(games)))
                    .handle(ExceptionHandler.handle());
        };
    }
}
