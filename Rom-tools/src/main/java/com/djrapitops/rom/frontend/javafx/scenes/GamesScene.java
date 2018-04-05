package com.djrapitops.rom.frontend.javafx.scenes;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.Variables;
import com.djrapitops.rom.frontend.javafx.components.GameComponent;
import com.djrapitops.rom.frontend.javafx.updating.Updatable;
import com.djrapitops.rom.game.Game;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Games view in the UI.
 *
 * @author Rsl1122
 */
public class GamesScene extends Scene implements Updatable<List<Game>> {

    private final JavaFXFrontend frontend;

    public GamesScene(JavaFXFrontend frontend) {
        super(new BorderPane(), Variables.WIDTH, Variables.HEIGHT);

        this.frontend = frontend;
    }

    @Override
    public void update(List<Game> with) {
        VBox games = new VBox();
        ObservableList<Node> children = games.getChildren();
        with.stream()
                .map(GameComponent::new)
                .forEach(children::add);
        BorderPane root = getContainer();
        root.setCenter(games);
        setRoot(root);
    }

    private BorderPane getContainer() {
        return (BorderPane) getRoot();
    }
}
