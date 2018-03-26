package com.djrapitops.rom.backend.database.sql;

/**
 * Class for parsing different SQL strings.
 *
 * @author Rsl1122
 */
public class SQLParser {

    private final StringBuilder s;

    public SQLParser(String start) {
        s = new StringBuilder(start);
    }

    public SQLParser addSpace() {
        s.append(" ");
        return this;
    }

    public SQLParser append(String string) {
        s.append(string);
        return this;
    }

    @Override
    public String toString() {
        return s.toString();
    }
}