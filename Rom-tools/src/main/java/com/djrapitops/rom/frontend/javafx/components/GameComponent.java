package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.javafx.Variables;
import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;
import com.djrapitops.rom.game.Game;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * JavaFX component for a Game.
 *
 * @author Rsl1122
 */
public class GameComponent extends VBox implements Updatable<State> {

    private final Game game;
    private final State state;
    private final JFXCheckBox checkBox;

    public GameComponent(Game game, State state) {
        BorderPane container = new BorderPane();

        this.game = game;
        this.state = state;
        VBox nameAndMeta = getGameNameAndMeta();
        VBox console = getConsoleName();
        console.setAlignment(Pos.CENTER_RIGHT);

        HBox leftContainer = new HBox();
        checkBox = new JFXCheckBox();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> setSelected(newValue));

        leftContainer.getChildren().add(checkBox);
        leftContainer.getChildren().add(nameAndMeta);

        container.setLeft(leftContainer);
        container.setRight(console);

        getChildren().add(container);

        state.addStateListener(this);
        update(state);
    }

    @Override
    public void update(State with) {
        checkBox.setSelected(state.isSelected(game));
    }

    public void setSelected(Boolean newValue) {
        state.performStateChange(current -> current.gameSelected(game, newValue));
    }

    private VBox getConsoleName() {
        VBox right = new VBox();
        ObservableList<Node> children = right.getChildren();

        Text metadata = new Text(game.getMetadata().getConsole().getFullName());

        children.add(metadata);
        return right;
    }

    private VBox getGameNameAndMeta() {
        VBox left = new VBox();
        ObservableList<Node> children = left.getChildren();

        Text title = new Text(game.getMetadata().getName());
        title.setFont(Variables.FONT_TITLE);
        children.add(title);

        Insets leftPadding = new Insets(0, 0, 0, 8);

        game.getGameFiles().stream().map(file -> {
            Text metadata = new Text(file.getFileName());
            VBox.setMargin(metadata, leftPadding);
            return metadata;
        }).forEach(children::add);

        return left;
    }
}
