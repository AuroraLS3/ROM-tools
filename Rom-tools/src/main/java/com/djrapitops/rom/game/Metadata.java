package com.djrapitops.rom.game;

import com.djrapitops.rom.util.Verify;

/**
 * Container for game related metadata.
 *
 * @author Rsl1122
 */
public class Metadata {

    String name;
    String console;

    public static Factory create() {
        return new Factory();
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

        public Factory setConsole(String console) {
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
