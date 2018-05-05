package utils;

import com.djrapitops.rom.game.Console;
import com.djrapitops.rom.game.FileExtension;
import com.djrapitops.rom.game.Metadata;

public class MetadataCreationUtility {

    public static Metadata createForTestGame() {
        return Metadata.create()
                .setConsole(FileExtension.GB)
                .setName("TestGame")
                .build();
    }

    public static Metadata createNonExistent() {
        return Metadata.create()
                .setConsole(Console.ATARI_2600)
                .setName("Not to be saved")
                .build();
    }

    public static Metadata createWithName(String name) {
        return Metadata.create()
                .setConsole(Console.ATARI_2600)
                .setName(name)
                .build();
    }
}
