package com.djrapitops.rom.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertNotNull;

public abstract class FileTest {

    protected File getFile(String resourceName) {
        File file = new File("src/test/resources", resourceName);
        assertNotNull(file);
        return file;
    }

    protected List<String> lines(File file) throws IOException {
        try (Stream<String> lines = Files.lines(file.toPath())) {
            return lines.collect(Collectors.toList());
        }
    }

}
