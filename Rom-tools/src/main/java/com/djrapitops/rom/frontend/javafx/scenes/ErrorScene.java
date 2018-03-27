package com.djrapitops.rom.frontend.javafx.scenes;

import javafx.scene.Scene;
import javafx.scene.control.TextField;

/**
 * Error Scene for when an error occurs.
 *
 * @author Rsl1122
 */
public class ErrorScene extends Scene {

    public ErrorScene(Throwable e) {
        super(new TextField("Error occurred: " + e.toString()));
    }
}
