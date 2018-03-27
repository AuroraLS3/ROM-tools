package com.djrapitops.rom;

import com.djrapitops.rom.frontend.javafx.JavaFXFrontend;

/**
 * Main class of ROM-tools, launches backend threads & UI thread.
 *
 * @author Rsl1122
 */
public class Main {

    private static Backend backend;

    public static void main(String[] args) {
        backend = new Backend();

        JavaFXFrontend.start(args);
    }


    public static Backend getBackend() {
        return backend;
    }
}
