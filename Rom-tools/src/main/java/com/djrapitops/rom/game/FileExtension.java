package com.djrapitops.rom.game;

import com.djrapitops.rom.exceptions.UnsupportedFileExtensionException;

import java.io.File;

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
    A52(".a52", Console.ATARI_5200),
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
    // SRL: GBA, WII
    SRL(".srl", Console.METADATA),
    // CUE: PSX, SegaCD
    CUE(".cue", Console.METADATA),
    // BIN: Atari 2600, Atari 7800, Sega Genesis, Nintendo DS, PSX, SegaCD
    BIN(".bin", Console.METADATA),
    // ISO: PSX2, PSP
    ISO(".iso", Console.METADATA);

    private final String extension;
    private final Console console;

    FileExtension(String extension, Console console) {
        this.extension = extension;
        this.console = console;
    }

    /**
     * Used to get an enum value for a specific file extension.
     *
     * @param file File to get the FileExtension for.
     * @return FileExtension if found.
     * @throws UnsupportedFileExtensionException if the extension is unsupported or file does not have a file format.
     */
    public static FileExtension getExtensionFor(File file) {
        String fileName = file.getName();
        int beginIndex = getExtensionStartIndex(file, fileName);

        String fileExtension = fileName.substring(beginIndex);
        for (FileExtension ext : values()) {
            if (ext.extension.equalsIgnoreCase(fileExtension)) {
                return ext;
            }
        }
        throw new UnsupportedFileExtensionException("Unsupported extension", file.getAbsolutePath(), fileExtension);
    }

    private static int getExtensionStartIndex(File file, String fileName) {
        int beginIndex = fileName.lastIndexOf('.');
        if (beginIndex == -1) {
            throw new UnsupportedFileExtensionException("File did not have a file format", file.getAbsolutePath(), "");
        }
        return beginIndex;
    }

    public String getExtension() {
        return extension;
    }

    public Console getConsole() {
        return console;
    }
}
