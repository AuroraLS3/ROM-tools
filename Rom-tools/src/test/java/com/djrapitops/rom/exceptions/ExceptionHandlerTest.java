package com.djrapitops.rom.exceptions;

import com.djrapitops.rom.Main;
import com.djrapitops.rom.backend.Backend;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static org.junit.Assert.assertEquals;

public class ExceptionHandlerTest {

    @Test
    public void testDefaultExceptionHandler() {
        Main.setBackend(new Backend());

        List<Boolean> calls = new ArrayList<>();

        ExceptionHandler.handle(Level.WARNING, new Exception("Test") {
            @Override
            public String getMessage() {
                calls.add(true);
                return super.getMessage();
            }
        });

        // getMessage is called once in default ExceptionHandler and once in Exception#toString
        assertEquals(1, calls.size());
    }

    @Test
    public void testCustomExceptionHandler() {
        Main.setBackend(new Backend());

        List<Boolean> calls = new ArrayList<>();

        Main.getBackend().setExceptionHandler((level, throwable) -> calls.add(true));

        ExceptionHandler.handle(Level.WARNING, new Exception("Test"));
        assertEquals(1, calls.size());
    }

}