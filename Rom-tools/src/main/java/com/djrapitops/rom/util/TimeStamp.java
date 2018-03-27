package com.djrapitops.rom.util;

import java.text.SimpleDateFormat;

/**
 * TimeStamp formatting utility.
 *
 * @author Rsl1122
 */
public class TimeStamp {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
    private final long epochMs;

    public TimeStamp(long epochMs) {
        this.epochMs = epochMs;
    }

    public String toFormatted() {
        return FORMATTER.format(epochMs);
    }

    @Override
    public String toString() {
        return toFormatted();
    }
}
