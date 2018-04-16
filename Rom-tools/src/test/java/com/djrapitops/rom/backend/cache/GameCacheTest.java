package com.djrapitops.rom.backend.cache;

import com.djrapitops.rom.backend.Operation;
import com.djrapitops.rom.exceptions.BackendException;
import com.djrapitops.rom.util.ThrowingWrapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import utils.fakeClasses.FakeDAO;
import utils.fakeClasses.TestGameBackend;
import utils.fakeClasses.ThrowingGameBackend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class GameCacheTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TestGameBackend testGameBackend;
    private ThrowingGameBackend throwingGameBackend;

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

        String expected = "Test";
        cache.save(new Operation<String>(FakeDAO::new, () -> Collections.EMPTY_MAP, Keys.GAMES), expected);
        assertEquals(expected, testGameBackend.getLastSaved());
    }

    @Test
    public void testBackendUnCached() throws BackendException {
        // See TestGameBackend return value
        thrown.expect(BackendException.class);
        thrown.expectMessage(ThrowingGameBackend.FETCH);

        GameCache cache = new GameCache(throwingGameBackend);

        cache.fetch(new Operation<String>(FakeDAO::new, () -> Collections.EMPTY_MAP, Keys.GAMES));
    }

    @Test
    public void testCaching() throws BackendException {
        List<Boolean> callsToWrapper = new ArrayList<>();
        List<String> expected = Arrays.asList("Test", "Passed");

        ThrowingWrapper<List<String>, BackendException> wrapper = () -> {
            callsToWrapper.add(true);
            return expected;
        };

        GameCache cache = new GameCache(testGameBackend);

        assertEquals(0, callsToWrapper.size());
        assertEquals(expected, cache.getOrFetch(Keys.GAMES, wrapper));
        assertEquals(1, callsToWrapper.size());

        assertEquals(expected, cache.getOrFetch(Keys.GAMES, wrapper));
        // Calls still one since request is fetched from cache.
        assertEquals(1, callsToWrapper.size());
    }

    @Test
    public void testClearsFromCache() throws BackendException {
        List<Boolean> callsToWrapper = new ArrayList<>();
        List<String> expected = Arrays.asList("Test", "Passed");

        ThrowingWrapper<List<String>, BackendException> wrapper = () -> {
            callsToWrapper.add(true);
            return expected;
        };

        GameCache cache = new GameCache(testGameBackend);

        assertEquals(expected, cache.getOrFetch(Keys.GAMES, wrapper));

        cache.clear(Keys.GAMES);

        assertEquals(expected, cache.getOrFetch(Keys.GAMES, wrapper));
        // Calls are now two since request is fetched from backend.
        assertEquals(2, callsToWrapper.size());
    }
}