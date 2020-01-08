package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import org.apache.commons.text.TextStringBuilder;

import java.util.Collection;

public class GameTable {

    private final JFXTreeTableView<GameComponent> table;

    public GameTable(Collection<Game> games) {
        JFXTreeTableColumn<GameComponent, String> gameColumn = new JFXTreeTableColumn<>("Name");
        gameColumn.setPrefWidth(150);
        gameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<GameComponent, String> param) -> {
            if (gameColumn.validateValue(param)) {
                return param.getValue().getValue().name;
            } else {
                return gameColumn.getComputedValue(param);
            }
        });

        JFXTreeTableColumn<GameComponent, String> pathsColumn = new JFXTreeTableColumn<>("Files");
        pathsColumn.setPrefWidth(150);
        pathsColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<GameComponent, String> param) -> {
            if (pathsColumn.validateValue(param)) {
                return param.getValue().getValue().files;
            } else {
                return pathsColumn.getComputedValue(param);
            }
        });

        JFXTreeTableColumn<GameComponent, String> consoleColumn = new JFXTreeTableColumn<>("Console");
        consoleColumn.setPrefWidth(150);
        consoleColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<GameComponent, String> param) -> {
            if (consoleColumn.validateValue(param)) {
                return param.getValue().getValue().console;
            } else {
                return consoleColumn.getComputedValue(param);
            }
        });

        // build tree
        final TreeItem<GameComponent> root = new RecursiveTreeItem<GameComponent>(GameComponent.forGames(games), RecursiveTreeObject::getChildren);

        table = new JFXTreeTableView<>(root);
        table.setShowRoot(false);
        table.setEditable(false);
        table.getColumns().setAll(gameColumn, consoleColumn, pathsColumn);
    }

    public JFXTreeTableView<GameComponent> getTable() {
        return table;
    }

    public static class GameComponent extends RecursiveTreeObject<GameComponent> {
        final StringProperty name;
        final StringProperty files;
        final StringProperty console;

        public GameComponent(Game game) {
            this.name = new SimpleStringProperty(game.getMetadata().getName());
            this.files = new SimpleStringProperty(makeString(game.getGameFiles()));
            this.console = new SimpleStringProperty(game.getMetadata().getConsole().map(Console::getName)
                    .orElse(game.getMetadata().getPotentialConsoles().size() + " options"));
        }

        public static ObservableList<GameComponent> forGames(Collection<Game> games) {
            ObservableList<GameComponent> components = FXCollections.observableArrayList();
            for (Game game : games) {
                components.add(new GameComponent(game));
            }
            return components;
        }

        private String makeString(Collection<GameFile> gameFiles) {
            TextStringBuilder builder = new TextStringBuilder();
            Object[] fileNames = gameFiles.stream()
                    .map(GameFile::getFileName)
                    .toArray();
            builder.appendWithSeparators(fileNames, "\n");
            return builder.toString();
        }
    }
}
