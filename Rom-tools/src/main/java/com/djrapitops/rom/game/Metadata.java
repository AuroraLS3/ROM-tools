package com.djrapitops.rom.game;

import com.djrapitops.rom.util.Verify;

import java.util.Objects;

/**
 * Container for game related metadata.
 *
 * @author Rsl1122
 */
public class Metadata {

    String name;
    Console console;

    public static Factory create() {
        return new Factory();
    }

    public String getName() {
        return name;
    }

    public Console getConsole() {
        return console;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return Objects.equals(name, metadata.name) &&
                console == metadata.console;
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

    /**
     * Builder for Metadata objects.
     */
    public static class Factory {
        private Metadata metadata;

        Factory() {
            this.metadata = new Metadata();
        }

        public Factory setName(String name) {
            metadata.name = name;
            return this;
        }

        public Factory setConsole(Console console) {
            metadata.console = console;
            return this;
        }

        public Factory setConsole(FileExtension extension) {
            return setConsole(extension.getConsole());
        }

        /**
         * Finish building the MetaData object.
         *
         * @return Finished Metadata object.
         * @throws IllegalStateException If object is incomplete, for example a field is missing.
         */
        public Metadata build() throws IllegalStateException {
            Verify.notNull(metadata.name, () -> new IllegalStateException("Name was not specified"));
            Verify.notNull(metadata.console, () -> new IllegalStateException("Console was not specified"));

            return metadata;
        }
    }
}
