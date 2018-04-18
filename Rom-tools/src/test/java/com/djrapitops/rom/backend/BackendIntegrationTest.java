package com.djrapitops.rom.backend;

import com.djrapitops.rom.Main;
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
}