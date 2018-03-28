package com.djrapitops.rom.exceptions;

/**
 * Enum for different levels of Exception handling.
 * <p>
 * CRITICAL: Program has to halt.
 * SEVERE: Operation could not be performed.
 * WARNING: Operation ran into issues, but could be performed.
 * DEBUG: Exception that is harmless to operations.
 *
 * @author Rsl1122
 */
public enum Level {

    CRITICAL,
    SEVERE,
    WARNING,
    DEBUG

}
