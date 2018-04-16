package com.djrapitops.rom.backend;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.updating.UIUpdateProcess;
import com.djrapitops.rom.game.Game;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

public class BackendIntegrationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testOpenDoesNotThrowException() throws BackendException {
        Backend backend = new Backend();
        Main.setBackend(backend);
        UIUpdateProcess updateProcess = new UIUpdateProcess();

        backend.open(new Frontend() {
            @Override
            public UIUpdateProcess getUiUpdateProcess() {
                return updateProcess;
            }

            @Override
            public void update(List<Game> with) {

            }
        });
        await().atMost(1, TimeUnit.SECONDS).until(opened(backend));
        assertEquals(2, updateProcess.tasksLeft());
    }

    private Callable<Boolean> opened(Backend backend) {
        return backend::isOpen;
    }

    @Test
    public void testOpenDoesThrowsException() throws BackendException {
        Backend backend = null;
        Backend backend2 = null;
        try {
            thrown.expect(BackendException.class);
            thrown.expectMessage("Program is already running! Only one instance can run at a time.");

            backend = new Backend();
            Main.setBackend(backend);
            UIUpdateProcess updateProcess = new UIUpdateProcess();

            Frontend frontend = new Frontend() {
                @Override
                public UIUpdateProcess getUiUpdateProcess() {
                    return updateProcess;
                }

                @Override
                public void update(List<Game> with) {

                }
            };
            backend.open(frontend);
            backend2 = new Backend();
            backend2.open(frontend);

            await().atMost(1, TimeUnit.SECONDS).until(opened(backend));
            assertEquals(1, updateProcess.tasksLeft());
        } finally {
            if (backend != null) {
                backend.close();
            }
            if (backend2 != null) {
                backend2.close();
            }
        }
    }
}