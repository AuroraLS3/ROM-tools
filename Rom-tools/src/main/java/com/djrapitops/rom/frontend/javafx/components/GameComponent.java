package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.javafx.Variables;
import com.djrapitops.rom.game.Game;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * JavaFX component for a Game.
 *
 * @author Rsl1122
 */
public class GameComponent extends VBox {

    public GameComponent(Game game) {
        ObservableList<Node> children = getChildren();

        Text title = new Text(game.getName());
        title.setFont(Variables.FONT_TITLE);
        children.add(title);

        Insets leftPadding = new Insets(0, 0, 0, 8);

        Text metadata = new Text(game.getMetadata().getName());
        VBox.setMargin(metadata, leftPadding);
        children.add(metadata);
    }
}
