package com.djrapitops.rom.game;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a Console supported by ROM Tools.
 *
 * @author Rsl1122
 */
public class Console {

    private final String name;
    private final Collection<String> fileExtensions;

    public Console(String name, String... fileExtensions) {
        this.name = name;
        this.fileExtensions = Arrays.asList(fileExtensions);
    }

    Collection<String> getFileExtensions() {
        return fileExtensions;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Console console = (Console) o;
        return name.equals(console.name) &&
                fileExtensions.equals(console.fileExtensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fileExtensions);
    }

    @Override
    public String toString() {
        return "Console{" +
                "name='" + name + '\'' +
                ", fileExtensions=" + fileExtensions +
                '}';
    }
}
