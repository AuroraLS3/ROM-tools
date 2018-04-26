package utils;

import com.djrapitops.rom.game.FileExtension;
import com.djrapitops.rom.game.Metadata;

public class MetadataCreationUtility {

    public static Metadata createForTestGame() {
        return Metadata.create().setConsole(FileExtension.GB).setName("TestGame").build();
    }

}
