package com.djrapitops.rom.frontend.updating;

import com.djrapitops.rom.exceptions.ExceptionHandler;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

/**
 * Process that is run periodically to update the UI components after tasks have finished.
 *
 * @author Rsl1122
 */
public class UIUpdateProcess implements Runnable {

    private final List<Update> tasks;

    public UIUpdateProcess() {
        tasks = new ArrayList<>();
    }

    @Override
    public void run() {
        tasks.iterator().forEachRemaining(toUpdate -> {
            if (toUpdate.isDone()) {
                tryToUpdate(toUpdate);
                tasks.remove(toUpdate);
            }
        });
    }

    public synchronized void submitTask(Update update) {
        tasks.add(update);
    }

    private void tryToUpdate(Update toUpdate) {
        Platform.runLater(() -> {
            try {
                toUpdate.update();
            } catch (InterruptedException | ExecutionException e) {
                ExceptionHandler.handle(Level.WARNING, e);
            }
        });
    }

    public int tasksLeft() {
        return tasks.size();
    }
}
