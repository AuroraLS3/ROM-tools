package com.djrapitops.rom.frontend.javafx.views;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.components.GameComponent;
import com.djrapitops.rom.frontend.javafx.components.SelectedTextContainer;
import com.djrapitops.rom.frontend.javafx.components.gamesview.GamesSceneBottomNav;
import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;
import com.djrapitops.rom.game.Game;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Games view in the UI.
 *
 * @author Rsl1122
 */
public class GamesView extends BorderPane implements Updatable<State> {

    private int lastseenVisible = -1;

    public GamesView(JavaFXFrontend frontend, BorderPane mainContainer) {
        prefWidthProperty().bind(mainContainer.widthProperty());
        prefHeightProperty().bind(mainContainer.heightProperty());
        GamesSceneBottomNav bottomNav = new GamesSceneBottomNav(frontend);
        bottomNav.prefWidthProperty().bind(this.widthProperty());

        State state = frontend.getState();
        setTop(new SelectedTextContainer(state));
        setBottom(bottomNav);

        state.addStateListener(this);
        update(state);
    }

    @Override
    public void update(State state) {
        VBox container = new VBox();
        container.prefWidthProperty().bind(this.widthProperty());

        JFXListView<GameComponent> list = new JFXListView<>();
        List<Game> visibleGames = state.getVisibleGames();
        int size = visibleGames.size();

        if (lastseenVisible != size) {
            state.clearStateListenerInstances(GameComponent.class);

            List<GameComponent> gameComponents = visibleGames.stream()
                    .map(game -> {
                        GameComponent gameComponent = new GameComponent(game, state);
                        gameComponent.prefWidthProperty().bind(list.prefWidthProperty());
                        return gameComponent;
                    })
                    .collect(Collectors.toList());
            list.setItems(FXCollections.observableArrayList(gameComponents));

            // Removes the blue "on focus" box around the list
            list.setFocusTraversable(false);
            list.prefHeightProperty().bind(heightProperty());

            list.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            container.getChildren().add(list);

            setCenter(container);
        }
        lastseenVisible = size;
    }
}
