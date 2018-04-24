package utils.fakeClasses;

import com.djrapitops.rom.game.FileExtension;
import com.djrapitops.rom.game.Metadata;

public class TestMetadata {

    public static Metadata createForTestGame() {
        return Metadata.create().setConsole(FileExtension.GB).setName("Testgame").build();
    }

}
