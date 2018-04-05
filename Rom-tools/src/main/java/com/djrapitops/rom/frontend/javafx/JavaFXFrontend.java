package com.djrapitops.rom.frontend.javafx;

import com.djrapitops.rom.Backend;
import com.djrapitops.rom.backend.processes.FileVerificationProcess;
import com.djrapitops.rom.backend.processes.GameLoadingProcess;
import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.javafx.scenes.ErrorScene;
import com.djrapitops.rom.frontend.javafx.scenes.GamesScene;
import com.djrapitops.rom.frontend.javafx.scenes.LoadingScene;
import com.djrapitops.rom.frontend.javafx.scenes.Views;
import com.djrapitops.rom.frontend.javafx.updating.UIUpdateProcess;
import com.djrapitops.rom.frontend.javafx.updating.Update;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.util.Verify;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * JavaFX Frontend implementation.
 *
 * @author Rsl1122
 */
public class JavaFXFrontend extends Application implements Frontend {

    private final ScheduledExecutorService uiUpdateService;
    private Stage primaryStage;
    private UIUpdateProcess uiUpdateProcess;

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
            backend.open();

            Future<List<Game>> gameLoadingTask = backend.submitTask(new GameLoadingProcess(backend.getGameBackend()));
            Future<List<Game>> fileVerificationTask = backend.submitTask(new FileVerificationProcess(gameLoadingTask));

            uiUpdateProcess.submitTask(new Update<>(gamesScene, gameLoadingTask));
            primaryStage.setScene(gamesScene);
        } catch (Exception e) {
            primaryStage.setScene(new ErrorScene(e));
        }
    }

    public void changeView(Views view) {
        Verify.notNull(primaryStage, () -> new IllegalStateException("Application has not been started yet."));

        Platform.runLater(() -> {
            switch (view) {
                case GAMES:
                    primaryStage.setScene(gamesScene);
                default:
                    primaryStage.setScene(new ErrorScene(new IllegalArgumentException("View not defined")));
            }
        });
    }

    @Override
    public void stop() {
        uiUpdateService.shutdownNow();
        Backend.getInstance().close();
    }
}
