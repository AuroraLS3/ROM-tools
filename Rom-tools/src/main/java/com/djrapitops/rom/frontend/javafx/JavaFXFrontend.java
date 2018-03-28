package com.djrapitops.rom.frontend.javafx;

import com.djrapitops.rom.Backend;
import com.djrapitops.rom.backend.processes.FileVerificationProcess;
import com.djrapitops.rom.backend.processes.GameLoadingProcess;
import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.javafx.scenes.ErrorScene;
import com.djrapitops.rom.frontend.javafx.scenes.GamesScene;
import com.djrapitops.rom.frontend.javafx.scenes.LoadingScene;
import com.djrapitops.rom.game.Game;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.Future;

/**
 * JavaFX Frontend implementation.
 *
 * @author Rsl1122
 */
public class JavaFXFrontend extends Application implements Frontend {

    private GamesScene gamesScene;

    public static void start(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("ROM Tools");
            primaryStage.getIcons().add(new Image("http://djrapitops.com/uploads/NMPlayer.png"));

            primaryStage.setScene(new LoadingScene());
            primaryStage.show();

            Backend backend = Backend.getInstance();
            backend.open();

            Future<List<Game>> gameLoadingTask = backend.submitTask(new GameLoadingProcess(backend.getGameBackend()));
            List<Game> games = gameLoadingTask.get();
            Future<List<Game>> fileVerificationTask = backend.submitTask(new FileVerificationProcess(games));

            gamesScene = new GamesScene(primaryStage);
            primaryStage.setScene(gamesScene);
        } catch (Exception e) {
            primaryStage.setScene(new ErrorScene(e));
        }
    }

    @Override
    public void stop() {
        Backend.getInstance().close();
    }
}
