package com.djrapitops.rom.game;

/**
 * Enum for supported file extensions and consoles they're linked to.
 * <p>
 * Source for file - console relation:
 * http://emulation.gametechwiki.com/index.php/List_of_filetypes
 *
 * @author Rsl1122
 */
public enum FileExtension {
    A26(".a26", Console.ATARI_2600),
    A78(".a78", Console.ATARI_7800),
    NES(".nes", Console.NES),
    SMC(".smc", Console.SNES),
    SFC(".sfc", Console.SNES),
    MD(".md", Console.GENESIS),
    SMD(".smd", Console.GENESIS),
    GEN(".gen", Console.GENESIS),
    GG(".gg", Console.GAME_GEAR),
    Z64(".z64", Console.N64),
    V64(".v64", Console.N64),
    N64(".n64", Console.N64),
    GB(".gb", Console.GAMEBOY),
    GBC(".gbc", Console.GAMEBOY_COLOR),
    GBA(".gba", Console.GBA),
    GCM(".gcm", Console.GAMECUBE),
    GCZ(".gcz", Console.GAMECUBE),
    NDS(".nds", Console.NINTENDO_DS),
    WBFS(".wbsf", Console.WII),
    WAD(".wad", Console.WII),
    CIA(".cia", Console.NINTENDO_3DS),
    DS3(".3ds", Console.NINTENDO_3DS),
    NGP(".ngp", Console.NEO_GEO),
    NGC(".ngc", Console.NEO_GEO),
    PCE(".pce", Console.PC_ENGINE),
    VB(".vb", Console.VIRTUAL_BOY),
    ELF(".elf", Console.PS2),
    PBP(".pbp", Console.PSP),
    XEX(".xex", Console.XBOX),
    DOL(".dol", Console.GAMECUBE),
    VEC(".vec", Console.VECTREX),
    // SRL: GBA or WII
    SRL(".srl", Console.METADATA),
    // CUE, BIN: PSX or SegaCD
    CUE(".cue", Console.METADATA),
    BIN(".bin", Console.METADATA),
    // ISO: PSX2 or PSP
    ISO(".iso", Console.METADATA);

    private final String extension;
    private final Console console;

    FileExtension(String extension, Console console) {
        this.extension = extension;
        this.console = console;
    }

    public static FileExtension getExtensionFor(String extension) {
        for (FileExtension ext : values()) {
            if (ext.extension.equalsIgnoreCase(extension)) {
                return ext;
            }
        }
        throw new IllegalArgumentException("Unsupported extension: " + extension);
    }

    public String getExtension() {
        return extension;
    }

    public Console getConsole() {
        return console;
    }
}
