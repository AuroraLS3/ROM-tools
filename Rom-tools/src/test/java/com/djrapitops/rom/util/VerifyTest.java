package com.djrapitops.rom.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

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

        // Throws
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

        // Throws
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

        // Throws
        Verify.notNull(null, () -> exception);
    }

    @Test
    public void notNullPassThrough() {
        String expected = "Test";

        assertEquals(expected, Verify.notNullPassThrough(expected, () -> exception));
    }

    @Test
    public void notNullPassThroughException() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(testSuccessMsg);

        // Throws
        Verify.notNullPassThrough(null, () -> exception);
    }

    @Test
    public void notNullArray() {
        Verify.notNull(new String[]{"", ""}, () -> exception);
    }

    @Test
    public void notNullArrayHasNullException() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(testSuccessMsg);

        // Throws
        Verify.notNull(new String[]{null, ""}, () -> exception);
    }
}