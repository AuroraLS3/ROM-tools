package com.djrapitops.rom.util;

import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;

public class TimeStampTest {

    @Test
    public void testFormatting() {
        SimpleDateFormat expected = new SimpleDateFormat("yyyy_MM_dd-HH:mm:ss");
        assertEquals(expected.format(0), new TimeStamp(0).toString());
    }
}