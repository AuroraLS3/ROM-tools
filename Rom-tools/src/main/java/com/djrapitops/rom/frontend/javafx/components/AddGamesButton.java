package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.backend.processes.GameParsing;
import com.djrapitops.rom.backend.processes.GameProcesses;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.state.StateOperation;
import com.djrapitops.rom.game.Game;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Button component for adding games.
 *
 * @author Rsl1122
 */
public class AddGamesButton extends JFXButton {

    private final JavaFXFrontend frontend;

    public AddGamesButton(JavaFXFrontend frontend) {
        super("Add Games");
        this.frontend = frontend;

        MenuItem addGameFiles = new MenuItem("Add Files");
        addGameFiles.setOnAction(getAddGameFilesActionHandler());
        MenuItem addGameFolders = new MenuItem("Add Folder");
        addGameFolders.setOnAction(getAddGameFoldersActionHandler());

        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ContextMenu contextMenu = new ContextMenu();
        setOnMouseReleased(event -> contextMenu.show(this, event.getScreenX(), event.getScreenY()));
        contextMenu.getItems().add(addGameFiles);
        contextMenu.getItems().add(addGameFolders);
    }

    private EventHandler<ActionEvent> getAddGameFilesActionHandler() {
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

    private EventHandler<ActionEvent> getAddGameFoldersActionHandler() {
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

    private void updateState(StateOperation operation) {
        frontend.getState().performStateChange(operation);
    }

}