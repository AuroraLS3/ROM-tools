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

    // Package private in order to allow specific values for testing.
    static Backend backend;
    static ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * Starts the program.
     * <p>
     * If the program is started via command prompt/terminal, debug messages are available on the console.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        backend = new Backend();
        JavaFXFrontend.start(args);
    }

    public static Backend getBackend() {
        return backend;
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }

}
