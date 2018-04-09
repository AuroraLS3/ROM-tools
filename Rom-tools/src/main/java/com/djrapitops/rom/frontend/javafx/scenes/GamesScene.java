package com.djrapitops.rom.frontend.javafx.scenes;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.Variables;
import com.djrapitops.rom.frontend.javafx.components.GameComponent;
import com.djrapitops.rom.frontend.javafx.components.MainNavigation;
import com.djrapitops.rom.frontend.javafx.updating.Updatable;
import com.djrapitops.rom.game.Game;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Updates GamesScene to display a list of games.
     *
     * @param with Object used as parameters for the update.
     */
    @Override
    public void update(List<Game> with) {
        VBox container = new VBox();
        ListView<GameComponent> list = new ListView<>();
        list.setItems(
                FXCollections.observableArrayList(
                        with.stream()
                                .map(GameComponent::new)
                                .collect(Collectors.toList())
                )
        );
        container.getChildren().add(list);

        BorderPane root = getContainer();
        root.setTop(new MainNavigation(frontend));
        root.setCenter(container);
        setRoot(root);
    }

    private BorderPane getContainer() {
        return (BorderPane) getRoot();
    }
}
