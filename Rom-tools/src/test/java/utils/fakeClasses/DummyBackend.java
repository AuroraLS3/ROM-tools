package utils.fakeClasses;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.backend.Backend;
import com.djrapitops.rom.frontend.Frontend;

import java.util.ArrayList;
import java.util.List;

public class DummyBackend extends Backend {

    private final FakeFrontend fakeFrontend;
    private List<Throwable> thrown;

    public DummyBackend() {
        fakeFrontend = new FakeFrontend();
        thrown = new ArrayList<>();
        setExceptionHandler((level, throwable) -> thrown.add(throwable));
    }

    public static DummyBackend get() {
        return (DummyBackend) Main.getBackend();
    }

    @Override
    public Frontend getFrontend() {
        return fakeFrontend;
    }

    @Override
    public void open(Frontend frontend) {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    public List<Throwable> getThrown() {
        return thrown;
    }

    public void clearThrown() {
        thrown.clear();
    }
}
