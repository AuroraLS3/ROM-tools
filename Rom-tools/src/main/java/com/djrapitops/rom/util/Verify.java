package com.djrapitops.rom.util;

/**
 * Utility class for throwing exceptions if boolean conditions are not met.
 *
 * @author Rsl1122
 */
public class Verify {

    public static <T extends Throwable> void isTrue(boolean check, Throwing<T> throwThis) throws T {
        if (!check) {
            throw throwThis.get();
        }
    }

    public static <T extends Throwable> void isFalse(boolean check, Throwing<T> throwThis) throws T {
        isTrue(!check, throwThis);
    }

    public static <T extends Throwable> void notNull(Object obj, Throwing<T> throwThis) throws T {
        isTrue(obj != null, throwThis);
    }

    public static <T extends Throwable> void notNull(Object[] objects, Throwing<T> throwThis) throws T {
        isTrue(objects != null, throwThis);
        for (Object obj : objects) {
            isTrue(obj != null, throwThis);
        }
    }

    public interface Throwing<T extends Throwable> {
        T get();
    }
}
