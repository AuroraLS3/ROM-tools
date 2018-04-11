package com.djrapitops.rom.frontend.javafx.scenes;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;
import com.djrapitops.rom.frontend.javafx.updating.Updatable;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 * Tools view in the UI.
 *
 * @author Rsl1122
 */
public class ToolsView extends BorderPane implements Updatable<Integer> {

    private final JavaFXFrontend frontend;

    public ToolsView(JavaFXFrontend frontend, BorderPane mainContainer) {
        this.frontend = frontend;
        prefWidthProperty().bind(mainContainer.widthProperty());
    }

    /**
     * Updates GamesView to display a list of games.
     *
     * @param with Object used as parameters for the update.
     */
    @Override
    public void update(Integer with) {
        BorderPane container = new BorderPane();

        container.setTop(new Text(with + " games selected"));

        setCenter(container);
    }

}
