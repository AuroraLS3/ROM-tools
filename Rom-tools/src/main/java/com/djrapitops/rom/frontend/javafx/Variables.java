package com.djrapitops.rom.frontend.javafx;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Class that holds UI variables in a single location.
 *
 * @author Rsl1122
 */
public class Variables {

    public static final int WIDTH = 950;
    public static final int HEIGHT = 800;

    public static final int LOADING_WIDTH = 400;
    public static final int LOADING_HEIGHT = 200;

    public static final Font FONT_TITLE = Font.font("Verdana", FontWeight.BOLD, 14);

    private Variables() {
        /* Not supposed to be created */
    }

}
