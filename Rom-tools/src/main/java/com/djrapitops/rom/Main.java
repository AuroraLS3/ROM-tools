package com.djrapitops.rom;

import com.djrapitops.rom.backend.Backend;
import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class of ROM-tools, launches backend threads & UI thread.
 *
 * @author Rsl1122
 */
public class Main {

    private static Backend backend;
    private static ExecutorService executorService;

    public static void main(String[] args) {
        backend = new Backend();
        executorService = Executors.newFixedThreadPool(10);
        JavaFXFrontend.start(args);
    }

    public static Backend getBackend() {
        return backend;
    }

    /**
     * Method for testing.
     *
     * @param backend Backend.
     */
    public static void setBackend(Backend backend) {
        Main.backend = backend;
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static void setExecutorService(ExecutorService executorService) {
        Main.executorService = executorService;
    }
}
