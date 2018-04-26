package com.djrapitops.rom.frontend.javafx;

import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.frontend.javafx.views.FatalErrorScene;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ExceptionHandler implementation for JavaFX version of the UI.
 *
 * @author Rsl1122
 */
public class JavaFXExceptionHandler implements ExceptionHandler {

    private final JavaFXFrontend frontend;

    public JavaFXExceptionHandler(JavaFXFrontend frontend) {
        this.frontend = frontend;
    }

    @Override
    public void handleThrowable(Level level, Throwable throwable) {
        if (level == Level.SEVERE) {
            Platform.runLater(() -> {
                Stage primaryStage = frontend.getStage();
                primaryStage.setScene(new FatalErrorScene(throwable));
                primaryStage.show();
                frontend.stop();
            });
        }
        // TODO Warnings
        Logger.getGlobal().log(level, throwable.getMessage(), throwable);
    }
}
