package com.djrapitops.rom.backend.processes;

import com.djrapitops.rom.backend.Log;
import com.djrapitops.rom.game.Game;
import com.djrapitops.rom.game.GameFile;
import com.djrapitops.rom.game.Metadata;
import com.djrapitops.rom.util.Verify;

import java.util.*;

/**
 * Class that holds methods for combining multiple Game objects into a single object.
 *
 * @author Rsl1122
 */
public class GameCombining {

    /**
     * Combines multi-file games into single game objects.
     * <p>
     * For example three Game objects with following GameFiles:
     * Example Game (Disk 1).cue
     * Example Game (Disk 1).bin
     * Example Game (Disk 2).bin
     * will be combined into a single Game object with all of the files.
     *
     * @param games List of games to combine.
     * @return List of combined games, should replace the list given as parameter.
     */
    public static List<Game> combineMultiFileGames(List<Game> games) {
        Map<Metadata, List<Game>> groupedByMetadata = groupByMetadata(games);
        List<Game> combined = combineGroupedGames(groupedByMetadata);
        Log.log("Combine results, before: " + games.size() + ", after: " + combined.size());
        Verify.isFalse(games.contains(null), () -> new IllegalStateException("Null in parsed games"));
        Verify.isFalse(combined.contains(null), () -> new IllegalStateException("Null in combined games"));
        return combined;
    }

    private static List<Game> combineGroupedGames(Map<Metadata, List<Game>> groupedByMetadata) {
        List<Game> combined = new ArrayList<>();
        int size = groupedByMetadata.size();
        int i = 1;
        for (Map.Entry<Metadata, List<Game>> entry : groupedByMetadata.entrySet()) {
            Log.log("Combining.. (" + i + "/" + size + "): " + entry.getKey().getName());
            List<Game> games = entry.getValue();

            Game first = null;
            for (Game game : games) {
                if (hasDiskFormatFiles(game.getGameFiles())) {
                    if (first == null) {
                        first = game;
                        continue;
                    }
                    first.getGameFiles().addAll(game.getGameFiles());
                } else {
                    combined.add(game);
                }
            }
            if (first != null) {
                combined.add(first);
            }
            i++;
        }
        return combined;
    }

    public static boolean hasDiskFormatFiles(Collection<GameFile> gameFiles) {
        return gameFiles.stream()
                .map(GameFile::getFileName)
                .anyMatch(fileName ->
                        fileName.toLowerCase().matches("(?i).*(track|disk|chip).[1-9][1-9]?.*")
                                || fileName.endsWith(".cue")
                                || fileName.endsWith(".bin")
                );
    }

    private static Map<Metadata, List<Game>> groupByMetadata(List<Game> games) {
        Map<Metadata, List<Game>> groupedByMetadata = new HashMap<>();

        for (Game game : games) {
            Metadata metadata = game.getMetadata();
            List<Game> sameName = groupedByMetadata.getOrDefault(metadata, new ArrayList<>());
            sameName.add(game);
            groupedByMetadata.put(metadata, sameName);
        }
        return groupedByMetadata;
    }

}
