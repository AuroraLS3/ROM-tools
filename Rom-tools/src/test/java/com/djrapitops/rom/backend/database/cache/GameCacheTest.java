package com.djrapitops.rom.backend.database.cache;

import com.djrapitops.rom.backend.GameBackend;
import com.djrapitops.rom.backend.operations.FetchOperations;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.util.ThrowingWrapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import utils.fakeClasses.TestGameBackend;
import utils.fakeClasses.ThrowingGameBackend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GameCacheTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private GameBackend testGameBackend;
    private GameBackend throwingGameBackend;

    @Before
    public void setUp() {
        testGameBackend = new TestGameBackend();
        throwingGameBackend = new ThrowingGameBackend();
    }

    @Test
    public void mainBackendIntegration() throws BackendException {
        GameCache cache = new GameCache(testGameBackend);
        assertFalse(cache.isOpen());
        cache.open();
        assertTrue(cache.isOpen());
        cache.close();
        assertFalse(cache.isOpen());
    }

    @Test
    public void mainBackendFailsToOpen() throws BackendException {
        thrown.expect(BackendException.class);
        thrown.expectMessage(ThrowingGameBackend.OPEN);

        GameCache cache = new GameCache(throwingGameBackend);
        cache.open();
    }

    @Test
    public void savingToMainBackend() {
        GameCache cache = new GameCache(testGameBackend);

        // See TestGameBackend return value
        assertNull(cache.save());
    }

    @Test
    public void testBackendUnCached() throws BackendException {
        // See TestGameBackend return value
        thrown.expect(NullPointerException.class);

        GameCache cache = new GameCache(testGameBackend);
        FetchOperations fetch = cache.fetch();
        assertTrue(fetch instanceof CacheFetchOperations);

        fetch.getGames();
    }

    @Test
    public void testCaching() throws BackendException {
        List<Boolean> callsToWrapper = new ArrayList<>();
        List<String> expected = Arrays.asList("Test", "Passed");

        ThrowingWrapper<List<String>, BackendException> wrapper = new ThrowingWrapper<List<String>, BackendException>() {
            @Override
            public List<String> get() {
                callsToWrapper.add(true);
                return expected;
            }
        };

        GameCache cache = new GameCache(testGameBackend);

        assertEquals(0, callsToWrapper.size());
        assertEquals(expected, cache.getOrFetch(GameCache.Request.GET_GAMES, wrapper));
        assertEquals(1, callsToWrapper.size());

        assertEquals(expected, cache.getOrFetch(GameCache.Request.GET_GAMES, wrapper));
        // Calls still one since request is fetched from cache.
        assertEquals(1, callsToWrapper.size());

    }
}