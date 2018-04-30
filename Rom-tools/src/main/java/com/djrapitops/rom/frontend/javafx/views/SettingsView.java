package com.djrapitops.rom.frontend.javafx.views;

import com.djrapitops.rom.backend.settings.Settings;
import com.djrapitops.rom.frontend.javafx.Style;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Settings view in the UI.
 *
 * @author Rsl1122
 */
public class SettingsView extends BorderPane {

    public SettingsView(BorderPane mainContainer) {
        prefWidthProperty().bind(mainContainer.widthProperty());

        VBox container = new VBox();
        container.setPadding(new Insets(10));

        for (Settings setting : Settings.values()) {

            BorderPane settingLine = new BorderPane();

            settingLine.setLeft(new Text(setting.getLabel()));

            switch (setting.getSettingClass().getSimpleName()) {
                case "String":
                    TextField textField = new TextField(setting.getString());
                    textField.setStyle(Style.BUTTON_SQUARE);
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        setting.setValue(newValue);
                    });
                    settingLine.setRight(textField);
                    break;
                default:
                    break;
            }
            container.getChildren().add(settingLine);
        }
        setCenter(container);
    }
}
