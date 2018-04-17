package com.djrapitops.rom.backend;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.frontend.Frontend;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import utils.fakeClasses.FakeFrontend;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class BackendIntegrationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testOpenDoesNotThrowException() {
        Backend backend = new Backend();
        Main.setBackend(backend);

        backend.open(new FakeFrontend());
        await().atMost(1, TimeUnit.SECONDS).until(opened(backend));
    }

    private Callable<Boolean> opened(Backend backend) {
        return backend::isOpen;
    }

    @Test
    public void testOpenDoesThrowsException() {
        Backend backend = null;
        Backend backend2 = null;
        try {
            thrown.expect(BackendException.class);
            thrown.expectMessage("Program is already running! Only one instance can run at a time.");

            backend = new Backend();
            Main.setBackend(backend);

            Frontend frontend = new FakeFrontend();
            backend.open(frontend);
            backend2 = new Backend();
            backend2.open(frontend);

            await().atMost(1, TimeUnit.SECONDS).until(opened(backend));
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