package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.javafx.Variables;
import com.djrapitops.rom.game.Game;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * JavaFX component for a Game.
 *
 * @author Rsl1122
 */
public class GameComponent extends VBox {

    public GameComponent(Game game) {
        BorderPane container = new BorderPane();
        VBox left = getLeftSide(game);
        VBox right = getRightSide(game);
        right.setAlignment(Pos.CENTER_RIGHT);

        container.setLeft(left);
        container.setRight(right);

        getChildren().add(container);
    }

    private VBox getRightSide(Game game) {
        VBox right = new VBox();
        ObservableList<Node> children = right.getChildren();

        Text metadata = new Text(game.getMetadata().getConsole().getFullName());

        children.add(metadata);
        return right;
    }

    private VBox getLeftSide(Game game) {
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
