package com.djrapitops.rom.util;

import java.text.SimpleDateFormat;

/**
 * TimeStamp formatting utility.
 *
 * @author Rsl1122
 */
public class TimeStamp {

    private final SimpleDateFormat formatter;
    private final long epochMs;

    public TimeStamp(long epochMs) {
        this.epochMs = epochMs;
        formatter = new SimpleDateFormat("yyyy_MM_dd-HH:mm:ss");
    }

    /**
     * Formats the epoch millisecond to the proper format.
     *
     * @return yyyy_MM_dd-HH:mm:ss format String.
     */
    public String asFormatted() {
        return formatter.format(epochMs);
    }

    @Override
    public String toString() {
        return asFormatted();
    }
}
