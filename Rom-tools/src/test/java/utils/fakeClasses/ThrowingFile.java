package utils.fakeClasses;

import java.io.File;
import java.net.URI;

public class ThrowingFile extends File {

    public ThrowingFile(String s) {
        super(s);
    }

    public ThrowingFile(String s, String s1) {
        super(s, s1);
    }

    public ThrowingFile(File file, String s) {
        super(file, s);
    }

    public ThrowingFile(URI uri) {
        super(uri);
    }

    @Override
    public String getAbsolutePath() {
        throw new RuntimeException("Fake Error");
    }
}
