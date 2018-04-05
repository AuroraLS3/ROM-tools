package com.djrapitops.rom.frontend.javafx.scenes;

import com.djrapitops.rom.frontend.javafx.Variables;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Arrays;

/**
 * Error Scene for when an error occurs.
 *
 * @author Rsl1122
 */
public class ErrorScene extends Scene {

    public ErrorScene(Throwable e) {
        super(getText(e));
    }

    private static VBox getText(Throwable e) {
        VBox stackTrace = new VBox();
        ObservableList<Node> children = stackTrace.getChildren();

        Text title = new Text("Non-recoverable error occurred and program had to be stopped");
        title.setFont(Variables.FONT_TITLE);
        children.add(title);

        Insets leftPadding = new Insets(0, 0, 0, 8);
        Text info = new Text("\nWhen reporting the issue, please attach the following:\n");
        VBox.setMargin(info, leftPadding);
        children.add(info);

        StringBuilder stackBuilder = new StringBuilder(e.toString());
        addStackTrace(stackBuilder, e.getStackTrace());

        Throwable cause = e.getCause();
        while (cause != null) {
            stackBuilder.append("caused by: ").append(cause.toString());
            addStackTrace(stackBuilder, cause.getStackTrace());
            cause = cause.getCause();
        }

        children.add(new TextArea(stackBuilder.toString()));

        return stackTrace;
    }

    private static void addStackTrace(StringBuilder stackBuilder, StackTraceElement[] trace) {
        Arrays.stream(trace)
                .map(element -> "\n  " + element.toString())
                .forEach(stackBuilder::append);
    }
}
