package com.djrapitops.rom.game;

/**
 * Enum that contains supported game systems.
 *
 * @author Rsl1122
 */
public enum Console {

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
    METADATA("Game Metadata contains console information");

    private final String fullName;

    Console(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
