package com.djrapitops.rom.frontend.javafx;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.backend.Backend;
import com.djrapitops.rom.exceptions.ExceptionHandler;
import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.javafx.components.MainNavigation;
import com.djrapitops.rom.frontend.javafx.scenes.*;
import com.djrapitops.rom.frontend.updating.UIUpdateProcess;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.util.Verify;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
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

    // Shared components
    private MainNavigation mainNavigation;

    private BorderPane mainContainer;

    // Scenes
    private GamesView gamesView;
    private ToolsView toolsView;
    private SettingsView settingsView;

    public JavaFXFrontend() {
        uiUpdateService = Executors.newSingleThreadScheduledExecutor();
        currentView = Views.GAMES;
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

            mainNavigation = new MainNavigation(this, primaryStage);
            mainContainer = new BorderPane();
            mainContainer.prefWidthProperty().bind(primaryStage.widthProperty());

            gamesView = new GamesView(this, mainContainer);
            toolsView = new ToolsView(this, mainContainer);
            settingsView = new SettingsView(this, mainContainer);

            mainContainer.setTop(mainNavigation);

            Backend backend = Backend.getInstance();
            backend.open(this);

            changeView(currentView);
            Scene scene = new Scene(mainContainer, Variables.WIDTH, Variables.HEIGHT);
            ObservableList<String> stylesheets = scene.getStylesheets();
            stylesheets.addAll(
                    Main.class.getResource("/css/jfoenix-fonts.css").toExternalForm(),
                    Main.class.getResource("/css/jfoenix-design.css").toExternalForm()
            );
            primaryStage.setScene(scene);
        } catch (Exception e) {
            primaryStage.setScene(new FatalErrorScene(e));
        }
    }

    public void changeView(Views view) {
        System.out.println("Change view: " + view);
        Verify.notNull(primaryStage, () -> new IllegalStateException("Application has not been started yet."));

        Platform.runLater(() -> {
            currentView = view;
            mainNavigation.update(view);
            mainContainer.setCenter(getView(view));
        });
    }

    private Node getView(Views view) {
        switch (view) {
            case GAMES:
                return gamesView;
            case TOOLS:
                return toolsView;
            case SETTINGS:
                return settingsView;
            default:
                ExceptionHandler.handle(Level.WARNING, new IllegalArgumentException("View not defined"));
                return mainContainer.getCenter();
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

    public MainNavigation getMainNavigation() {
        return mainNavigation;
    }

    @Override
    public void update(List<Game> with) {
        gamesView.update(with);
    }
}
