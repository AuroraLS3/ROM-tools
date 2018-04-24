package com.djrapitops.rom.backend;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.backend.database.SQLiteDatabase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import utils.fakeClasses.FakeFrontend;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class BackendIntegrationTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testOpenDoesNotThrowException() {
        Backend backend = new Backend();
        backend.setGameStorage(new SQLiteDatabase(new File(temporaryFolder.getRoot(), "games.db")));

        Main.setBackend(backend);
        Main.setExecutorService(Executors.newFixedThreadPool(10));

        FakeFrontend fakeFrontend = new FakeFrontend();
        backend.open(fakeFrontend);
        await().atMost(1, TimeUnit.SECONDS).until(opened(backend));
        await().atMost(5, TimeUnit.SECONDS)
                .until(() -> {
                    String info = fakeFrontend.getState().getLoadingInformation();
                    System.out.println(info);
                    return "Loaded 0 games.".equals(info);
                });
        backend.close();
    }

    private Callable<Boolean> opened(Backend backend) {
        return backend::isOpen;
    }
}