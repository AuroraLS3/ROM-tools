package com.djrapitops.rom.exceptions;

import com.djrapitops.rom.MainTestingVariables;
import com.djrapitops.rom.backend.Backend;
import org.junit.Test;
import utils.fakeClasses.DummyBackend;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static org.junit.Assert.assertEquals;

public class ExceptionHandlerTest {

    @Test
    public void testDefaultExceptionHandler() {
        MainTestingVariables.setBackend(new Backend());

        List<Boolean> calls = new ArrayList<>();

        ExceptionHandler.handle(Level.WARNING, new Exception("Test") {
            @Override
            public String getMessage() {
                calls.add(true);
                return super.getMessage();
            }
        });

        // getMessage is called once in default ExceptionHandler and once in Exception#toString
        assertEquals(2, calls.size());
    }

    @Test
    public void testCustomExceptionHandler() {
        MainTestingVariables.setBackend(new DummyBackend());

        ExceptionHandler.handle(Level.WARNING, new Exception("Test"));
        assertEquals(1, DummyBackend.get().getThrown().size());
    }

}