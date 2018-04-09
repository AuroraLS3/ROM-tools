package com.djrapitops.rom.frontend.javafx.components;

import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.javafx.scenes.GamesView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Bottom navigation for GamesView.
 *
 * @author Rsl1122
 * @see GamesView
 */
public class GamesSceneBottomNav extends BorderPane {

    public GamesSceneBottomNav(Frontend frontend) {
        setTop(getTopDrawer(frontend));
        setBottom(getBottomDrawer(frontend));
    }

    public Node getTopDrawer(Frontend frontend) {
        BorderPane container = new BorderPane();
        container.setLeft(new TextField());
        container.setRight(new Button("Filters"));
        return container;
    }

    public Node getBottomDrawer(Frontend frontend) {
        BorderPane container = new BorderPane();
        container.setLeft(new Button("Add Games"));
        container.setCenter(new Button("Select All"));
        container.setRight(new Button("Select with Filters"));
        return container;
    }
}
