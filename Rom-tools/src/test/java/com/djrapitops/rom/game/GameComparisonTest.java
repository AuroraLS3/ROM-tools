package com.djrapitops.rom.game;

import org.junit.Test;
import utils.MetadataCreationUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class GameComparisonTest {

    @Test
    public void gamesSortedAlphabetically() {
        List<Game> games = new ArrayList<>();
        List<String> expected = Arrays.asList("a", "ab", "ba", "bb");

        for (String expectedName : expected) {
            Game game = new Game();
            game.setMetadata(MetadataCreationUtility.createWithName(expectedName));
            games.add(game);
        }

        Collections.shuffle(games);
        Collections.sort(games);

        List<String> result = games.stream()
                .map(Game::getMetadata)
                .map(Metadata::getName)
                .collect(Collectors.toList());
        assertEquals(expected, result);
    }

}