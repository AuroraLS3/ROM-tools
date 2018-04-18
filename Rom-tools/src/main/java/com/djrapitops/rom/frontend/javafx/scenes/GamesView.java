package com.djrapitops.rom.frontend.javafx.scenes;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.components.GameComponent;
import com.djrapitops.rom.frontend.javafx.components.GamesSceneBottomNav;
import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Games view in the UI.
 *
 * @author Rsl1122
 */
public class GamesView extends BorderPane implements Updatable<State> {

    private final JavaFXFrontend frontend;
    private final GamesSceneBottomNav bottomNav;

    private int loadedGames = 0;

    public GamesView(JavaFXFrontend frontend, BorderPane mainContainer) {
        this.frontend = frontend;
        prefWidthProperty().bind(mainContainer.widthProperty());
        prefHeightProperty().bind(mainContainer.heightProperty());
        bottomNav = new GamesSceneBottomNav(frontend);
        bottomNav.prefWidthProperty().bind(this.widthProperty());
    }

    /**
     * Updates GamesView to display a list of games.
     *
     * @param state Object used as parameters for the update.
     */
    @Override
    public void update(State state) {
        bottomNav.update(state);

        int stateSize = state.getLoadedGames().size();
        if (loadedGames == stateSize && loadedGames != 0) {
            return;
        }
        loadedGames = stateSize;

        VBox container = new VBox();
        container.prefWidthProperty().bind(this.widthProperty());

        JFXListView<GameComponent> list = new JFXListView<>();
        List<GameComponent> gameComponents = state.getLoadedGames().stream()
                .map(game -> {
                    GameComponent gameComponent = new GameComponent(game, frontend.getState());
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

        BorderPane selectedContainer = new BorderPane();
        Text selectedText = new Text(frontend.getState().getSelectedGames().size() + " games selected");
        selectedText.setTextAlignment(TextAlignment.CENTER);
        selectedContainer.setCenter(selectedText);
        selectedContainer.setPrefHeight(25);

        setTop(selectedContainer);
        setCenter(container);
        setBottom(bottomNav);
    }
}
