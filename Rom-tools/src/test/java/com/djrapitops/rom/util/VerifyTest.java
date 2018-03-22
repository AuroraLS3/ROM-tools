package com.djrapitops.rom.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class VerifyTest {
    private final String testSuccessMsg = "Test Success";
    private final IllegalStateException exception = new IllegalStateException(testSuccessMsg);
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void isTrue() {
        Verify.isTrue(true, () -> exception);
    }

    @Test
    public void isTrueException() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(testSuccessMsg);

        Verify.isTrue(false, () -> exception);
    }

    @Test
    public void isFalse() {
        Verify.isFalse(false, () -> exception);
    }

    @Test
    public void isFalseException() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(testSuccessMsg);

        Verify.isFalse(true, () -> exception);
    }

    @Test
    public void notNull() {
        Verify.notNull("", () -> exception);
    }

    @Test
    public void notNullException() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(testSuccessMsg);

        Verify.notNull(null, () -> exception);
    }

    @Test
    public void notNullArray() {
        Verify.notNull(new String[]{"", ""}, () -> exception);
    }

    @Test
    public void notNullArrayHasNullException() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(testSuccessMsg);

        Verify.notNull(new String[]{null, ""}, () -> exception);
    }
}