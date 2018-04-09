package com.djrapitops.rom.frontend.javafx;

import com.djrapitops.rom.backend.Backend;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.javafx.scenes.FatalErrorScene;
import com.djrapitops.rom.frontend.javafx.scenes.GamesScene;
import com.djrapitops.rom.frontend.javafx.scenes.LoadingScene;
import com.djrapitops.rom.frontend.javafx.scenes.Views;
import com.djrapitops.rom.frontend.javafx.updating.UIUpdateProcess;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.util.Verify;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * JavaFX Frontend implementation.
 *
 * @author Rsl1122
 */
public class JavaFXFrontend extends Application implements Frontend {

    private final ScheduledExecutorService uiUpdateService;
    private Stage primaryStage;
    private UIUpdateProcess uiUpdateProcess;

    private Views currentView;

    // Scenes
    private GamesScene gamesScene;

    public JavaFXFrontend() {
        uiUpdateService = Executors.newSingleThreadScheduledExecutor();
    }

    public static void start(String[] args) {
        Application.launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        uiUpdateProcess = new UIUpdateProcess();
        uiUpdateService.scheduleWithFixedDelay(uiUpdateProcess, 20, 20, TimeUnit.MILLISECONDS);

        try {
            primaryStage.setTitle("ROM Tools");
            primaryStage.getIcons().add(new Image("http://djrapitops.com/uploads/NMPlayer.png"));

            primaryStage.setScene(new LoadingScene());
            primaryStage.show();

            gamesScene = new GamesScene(this);

            Backend backend = Backend.getInstance();
            backend.open(this);

            changeView(Views.GAMES);
        } catch (Exception e) {
            primaryStage.setScene(new FatalErrorScene(e));
        }
    }

    public void changeView(Views view) {
        System.out.println("Change view: " + view);
        Verify.notNull(primaryStage, () -> new IllegalStateException("Application has not been started yet."));

        Platform.runLater(() -> {
            primaryStage.setScene(getView(view));
            currentView = view;
        });
    }

    private Scene getView(Views view) {
        switch (view) {
            case GAMES:
                return gamesScene;
            default:
                ExceptionHandler.handle(Level.WARNING, new IllegalArgumentException("View not defined"));
                return primaryStage.getScene();
        }
    }

    @Override
    public UIUpdateProcess getUiUpdateProcess() {
        return uiUpdateProcess;
    }

    @Override
    public void stop() {
        uiUpdateService.shutdownNow();
        Backend.getInstance().close();
    }

    public Views getCurrentView() {
        return currentView;
    }

    @Override
    public void update(List<Game> with) {
        gamesScene.update(with);
    }
}
