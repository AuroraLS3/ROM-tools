package com.djrapitops.rom;

import com.djrapitops.rom.backend.Backend;

import java.util.concurrent.ExecutorService;

/**
 * Class in charge of setting test variables to Main class since they are package private.
 *
 * @author Rsl1122
 */
public class MainTestingVariables {

    public static void setExecutorService(ExecutorService executorService) {
        Main.executorService = executorService;
    }

    public static void setBackend(Backend backend) {
        Main.backend = backend;
    }

}