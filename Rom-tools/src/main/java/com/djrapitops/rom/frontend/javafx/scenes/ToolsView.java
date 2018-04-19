package com.djrapitops.rom.frontend.javafx.scenes;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.Style;
import com.djrapitops.rom.frontend.javafx.components.SelectedTextContainer;
import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.Updatable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

/**
 * Tools view in the UI.
 *
 * @author Rsl1122
 */
public class ToolsView extends BorderPane implements Updatable<State> {

    private final JavaFXFrontend frontend;

    public ToolsView(JavaFXFrontend frontend, BorderPane mainContainer) {
        this.frontend = frontend;
        prefWidthProperty().bind(mainContainer.widthProperty());
        update(frontend.getState());
    }

    /**
     * Updates GamesView to display a list of games.
     *
     * @param with Object used as parameters for the update.
     */
    @Override
    public void update(State with) {

        JFXListView<JFXButton> buttons = new JFXListView<>();
        buttons.prefWidthProperty().bind(widthProperty());

        JFXButton moveSingleFolderButton = new JFXButton("Move selected to a single folder");
        JFXButton copySingleFolderButton = new JFXButton("Copy selected to a single folder");
        JFXButton moveSubFoldersButton = new JFXButton("Move selected under a folder in new subfolders");
        JFXButton copySubFoldersButton = new JFXButton("Copy selected under a folder in new subfolders");

        ObservableList<JFXButton> obsButtons = FXCollections.observableArrayList(
                moveSingleFolderButton,
                moveSubFoldersButton,
                copySingleFolderButton,
                copySubFoldersButton
        );
        obsButtons.forEach(button -> {
            button.setAlignment(Pos.CENTER_LEFT);
            button.setStyle(Style.BUTTON_SQUARE + Style.BG_GREEN);
            button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        });

        buttons.setItems(obsButtons);
        // Removes the blue "on focus" box around the list
        buttons.setFocusTraversable(false);

        setTop(new SelectedTextContainer(frontend));
        setCenter(buttons);
    }

}
