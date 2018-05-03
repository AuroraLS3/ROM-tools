package com.djrapitops.rom.frontend.javafx.views;

import com.djrapitops.rom.backend.settings.Settings;
import com.djrapitops.rom.frontend.javafx.Style;
import com.jfoenix.controls.JFXCheckBox;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
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

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.prefWidthProperty().bind(widthProperty());
        VBox container = new VBox();
        container.prefWidthProperty().bind(scrollPane.widthProperty());
        container.setPadding(new Insets(10));

        for (Settings setting : Settings.values()) {

            BorderPane settingLine = new BorderPane();

            settingLine.setLeft(new Text(setting.getLabel()));

            switch (setting.getSettingClass().getSimpleName()) {
                case "String":
                    TextField field = new TextField(setting.asString());
                    field.setStyle(Style.BUTTON_SQUARE);
                    field.textProperty().addListener((observable, oldValue, newValue) -> setting.setValue(newValue));
                    settingLine.setRight(field);
                    break;
                case "Boolean":
                    JFXCheckBox checkBox = new JFXCheckBox();
                    checkBox.setSelected(setting.asBoolean());
                    checkBox.setStyle(Style.CHECKBOX_PURPLE);
                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> setting.setValue(newValue));
                    settingLine.setRight(checkBox);
                    break;
                default:
                    break;
            }
            container.getChildren().add(settingLine);
        }

        scrollPane.setContent(container);
        setCenter(scrollPane);
    }
}
