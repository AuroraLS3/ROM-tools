package com.djrapitops.rom.backend.database.sql;

/**
 * Class for parsing different SQL strings.
 *
 * @author Rsl1122
 */
public class SQLParser {

    private final StringBuilder builder;

    /**
     * Constructor that initializes the StringBuilder.
     *
     * @param start String to initialize the builder with.
     */
    SQLParser(String start) {
        builder = new StringBuilder(start);
    }

    /**
     * Adds a space character to the builder.
     *
     * @return The object which method was called.
     */
    public SQLParser addSpace() {
        builder.append(" ");
        return this;
    }

    /**
     * Adds a String to the builder.
     *
     * @param string String to append.
     * @return The object which method was called.
     */
    public SQLParser append(String string) {
        builder.append(string);
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}