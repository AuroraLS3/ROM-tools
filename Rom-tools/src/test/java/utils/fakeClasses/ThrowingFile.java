package utils.fakeClasses;

import java.io.File;

public class ThrowingFile extends File {

    public ThrowingFile(File file, String s) {
        super(file, s);
    }

    @Override
    public String getAbsolutePath() {
        throw new RuntimeException("Fake Error");
    }
}
