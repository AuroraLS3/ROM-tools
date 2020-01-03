package utils;

import com.djrapitops.rom.game.Consoles;
import com.djrapitops.rom.game.FileExtension;
import com.djrapitops.rom.game.Metadata;

public class MetadataCreationUtility {

    public static Metadata createForTestGame() {
        Metadata metadata = new Metadata();
        metadata.setConsole(Consoles.findAllMatchingExtension(FileExtension.GB).get(0));
        metadata.setName("TestGame");
        return metadata;

    }

    public static Metadata createNonExistent() {
        Metadata metadata = new Metadata();
        metadata.setConsole(Consoles.findAllMatchingExtension(FileExtension.A26).get(0));
        metadata.setName("Not to be saved");
        return metadata;
    }

    public static Metadata createWithName(String name) {
        Metadata metadata = new Metadata();
        metadata.setConsole(Consoles.findAllMatchingExtension(FileExtension.A26).get(0));
        metadata.setName(name);
        return metadata;
    }
}
