package com.djrapitops.rom.frontend.javafx.scenes;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.components.GameComponent;
import com.djrapitops.rom.frontend.javafx.components.GamesSceneBottomNav;
import com.djrapitops.rom.frontend.javafx.updating.Updatable;
import com.djrapitops.rom.game.Game;
import javafx.collections.FXCollections;
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
public class GamesView extends BorderPane implements Updatable<List<Game>> {

    private final JavaFXFrontend frontend;

    public GamesView(JavaFXFrontend frontend) {
        this.frontend = frontend;
    }

    /**
     * Updates GamesView to display a list of games.
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

        setCenter(container);
        setBottom(new GamesSceneBottomNav(frontend));
    }
}
