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
public class FileExtension {
    public static final String A26 = "a26";
    public static final String A52 = "a52";
    public static final String A78 = "a78";
    public static final String NES = "nes";
    public static final String SMC = "smc";
    public static final String SFC = "sfc";
    public static final String MD = "md";
    public static final String SMD = "smd";
    public static final String GEN = "gen";
    public static final String GG = "gg";
    public static final String Z64 = "z64";
    public static final String V64 = "v64";
    public static final String N64 = "n64";
    public static final String GB = "gb";
    public static final String GBC = "gbc";
    public static final String GBA = "gba";
    public static final String GCM = "gcm";
    public static final String GCZ = "gcz";
    public static final String NDS = "nds";
    public static final String WBFS = "wbsf";
    public static final String WAD = "wad";
    public static final String CIA = "cia";
    public static final String DS3 = "3ds";
    public static final String NGP = "ngp";
    public static final String NGC = "ngc";
    public static final String PCE = "pce";
    public static final String VB = "vb";
    public static final String ELF = "elf";
    public static final String PBP = "pbp";
    public static final String XEX = "xex";
    public static final String DOL = "dol";
    public static final String VEC = "vec";
    // SRL: GBA, WII
    public static final String SRL = "srl";
    // CUE: PSX, SegaCD
    public static final String CUE = "cue";
    // BIN: Atari 2600, Atari 7800, Sega Genesis, Nintendo DS, PSX, SegaCD
    public static final String BIN = "bin";
    // ISO: PSX2, PSP
    public static final String ISO = "iso";

    private FileExtension() {
        // Static variable class.
    }

    /**
     * Used to get an enum value for a specific file extension.
     *
     * @param file File to get the FileExtension for.
     * @return FileExtension if found.
     * @throws UnsupportedFileExtensionException if the file does not have a file format.
     */
    public static String getFor(File file) {
        String fileName = file.getName();
        int beginIndex = getExtensionStartIndex(file, fileName);

        return fileName.substring(beginIndex + 1).toLowerCase();
    }

    private static int getExtensionStartIndex(File file, String fileName) {
        int beginIndex = fileName.lastIndexOf('.');
        if (beginIndex == -1) {
            throw new UnsupportedFileExtensionException("File did not have a file format", file.getAbsolutePath(), "");
        }
        return beginIndex;
    }
}
