package com.djrapitops.rom.game;

import java.io.File;

/**
 * Enum that contains supported game systems.
 *
 * @author Rsl1122
 */
public enum Console {

    ATARI_2600("Atari 2600"),
    ATARI_5200("Atari 5200"),
    ATARI_7800("Atari 7800"),
    NES("Nintendo Entertainment System"),
    SNES("Super Nintendo"),
    GENESIS("Sega Genesis/Megadrive"),
    GAME_GEAR("Sega Master System"),
    N64("Nintendo 64"),
    GAMEBOY("GameBoy"),
    GAMEBOY_COLOR("GameBoy Color"),
    GBA("GameBoy Advance"),
    GAMECUBE("GameCube"),
    NINTENDO_DS("Nintendo DS"),
    WII("Nintendo Wii"),
    NINTENDO_3DS("Nintendo 3DS"),
    NEO_GEO("Neo Geo"),
    PC_ENGINE("PC Engine"),
    VIRTUAL_BOY("Virtual Boy"),
    PSX("Playstation 1"),
    PS2("Playstation 2"),
    PSP("Playstation Portable"),
    XBOX("Xbox"),
    METADATA("Unknown"),
    SEGA_CD("Sega CD"),
    VECTREX("Vectrex");

    private final String fullName;

    Console(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    /**
     * Resolves the Console from the file name.
     *
     * @param file File to resolve Console for.
     * @return Console that this file might be for or METADATA if not determined.
     */
    public static Console resolveForFile(File file) {
        FileExtension extension = FileExtension.getExtensionFor(file);
        return extension.getConsole();
    }
}
