package com.djrapitops.rom.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Consoles {

    private static List<Console> CONSOLES = new ArrayList<>();

    static {
        // Add some well known consoles
        add(new Console("Atari 2600", FileExtension.A26, FileExtension.BIN));
        add(new Console("Atari 5200", FileExtension.A52, FileExtension.BIN));
        add(new Console("Atari 7800", FileExtension.A78));
        add(new Console("Nintendo Entertainment System", FileExtension.NES));
        add(new Console("Super Nintendo", FileExtension.SMC, FileExtension.SFC));
        add(new Console("Sega Genesis/Megadrive", FileExtension.MD, FileExtension.SMD, FileExtension.GEN, FileExtension.BIN));
        add(new Console("Sega Master System", FileExtension.GG));
        add(new Console("Nintendo 64", FileExtension.Z64, FileExtension.V64, FileExtension.N64));
        add(new Console("GameBoy", FileExtension.GB));
        add(new Console("GameBoy Color", FileExtension.GBC));
        add(new Console("GameBoy Advance", FileExtension.GBA, FileExtension.SRL));
        add(new Console("GameCube", FileExtension.GCM, FileExtension.GCZ, FileExtension.DOL));
        add(new Console("Nintendo DS", FileExtension.NDS, FileExtension.BIN));
        add(new Console("Nintendo Wii", FileExtension.WBFS, FileExtension.WAD, FileExtension.SRL, FileExtension.DOL));
        add(new Console("Nintendo 3DS", FileExtension.CIA, FileExtension.DS3));
        add(new Console("Neo Geo", FileExtension.NGP, FileExtension.NGC));
        add(new Console("PC Engine", FileExtension.PCE));
        add(new Console("Virtual Boy", FileExtension.VB));
        add(new Console("Playstation 1", FileExtension.CUE, FileExtension.BIN));
        add(new Console("Playstation 2", FileExtension.ELF, FileExtension.ISO));
        add(new Console("Playstation Portable", FileExtension.PBP, FileExtension.ISO));
        add(new Console("Xbox", FileExtension.XEX));
        add(new Console("Sega CD", FileExtension.CUE, FileExtension.BIN));
        add(new Console("Vectrex", FileExtension.VEC));
    }

    private static void add(Console console) {
        CONSOLES.add(console);
    }

    public static List<Console> findAllMatchingExtension(String extension) {
        List<Console> matches = new ArrayList<>();
        for (Console console : CONSOLES) {
            if (console.getFileExtensions().contains(extension)) matches.add(console);
        }
        return matches;
    }

    public static Collection<Console> getAll() {
        return CONSOLES;
    }

    public static Optional<Console> findByName(String name) {
        if (name == null) return Optional.empty();
        for (Console console : CONSOLES) {
            if (name.equalsIgnoreCase(console.getName())) return Optional.of(console);
        }
        return Optional.empty();
    }
}
