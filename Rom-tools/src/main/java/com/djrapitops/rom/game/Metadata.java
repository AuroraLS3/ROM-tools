package com.djrapitops.rom.game;

import java.util.*;

/**
 * Container for game related metadata.
 *
 * @author Rsl1122
 */
public class Metadata {

    String name;
    Console console;
    Collection<Console> potentialConsoles = Collections.emptySet();

    public Metadata() {
    }

    public String getName() {
        return name;
    }

    public Optional<Console> getConsole() {
        return Optional.ofNullable(console);
    }

    public void setConsole(Console console) {
        this.console = console;
        this.potentialConsoles = Collections.singleton(console);
    }

    public Collection<Console> getPotentialConsoles() {
        return potentialConsoles;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConsoles(List<Console> potentialConsoles) {
        if (potentialConsoles.size() == 1) console = potentialConsoles.get(0);
        this.potentialConsoles = potentialConsoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Metadata metadata = (Metadata) o;
        return Objects.equals(name, metadata.name) &&
                console.equals(metadata.console);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, console);
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "name='" + name + '\'' +
                ", console=" + console +
                '}';
    }
}
