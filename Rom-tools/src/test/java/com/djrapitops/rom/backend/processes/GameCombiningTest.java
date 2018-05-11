package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.game.FileExtension;
import com.djrapitops.rom.game.GameFile;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameCombiningTest {
    @Test
    public void multiFileWorksForDisks1() {
        Collection<GameFile> files = Collections.singletonList(
                new GameFile(FileExtension.CUE, "any.cue", "")
        );
        assertTrue(GameCombining.hasDiskFormatFiles(files));
    }

    @Test
    public void multiFileWorksForDisks2() {
        Collection<GameFile> files = Collections.singletonList(
                new GameFile(FileExtension.BIN, "any.bin", "")
        );
        assertTrue(GameCombining.hasDiskFormatFiles(files));
    }

    @Test
    public void multiFileRegexWorks1() {
        Collection<GameFile> files = Arrays.asList(
                new GameFile(FileExtension.A26, "game (Chip 1).a26", ""),
                new GameFile(FileExtension.A26, "game (Chip 2).a26", "")
        );
        assertTrue(GameCombining.hasDiskFormatFiles(files));
    }

    @Test
    public void multiFileRegexWorks2() {
        // Normally these would be bin or cue files, but those are combined separately as well.
        Collection<GameFile> files = Arrays.asList(
                new GameFile(FileExtension.NDS, "game (Disk 1).nds", ""),
                new GameFile(FileExtension.NDS, "game (Disk 2).nds", "")
        );
        assertTrue(GameCombining.hasDiskFormatFiles(files));
    }

    @Test
    public void multiFileRegexWorks3() {
        // Normally these would be bin or cue files, but those are combined separately as well.
        Collection<GameFile> files = Arrays.asList(
                new GameFile(FileExtension.NDS, "game (Track 1).nds", ""),
                new GameFile(FileExtension.NDS, "game (Track 2).nds", "")
        );
        assertTrue(GameCombining.hasDiskFormatFiles(files));
    }

    @Test
    public void multiFileFalseWorks() {
        Collection<GameFile> files = Collections.singletonList(
                new GameFile(FileExtension.NDS, "game.nds", "")
        );
        assertFalse(GameCombining.hasDiskFormatFiles(files));
    }
}