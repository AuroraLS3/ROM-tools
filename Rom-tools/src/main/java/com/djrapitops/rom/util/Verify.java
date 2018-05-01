package com.djrapitops.rom.util;

/**
 * Utility class for throwing exceptions if conditions are not met.
 *
 * @author Rsl1122
 */
public class Verify {

    /**
     * Check if given parameter is true and throw exception if it is false.
     *
     * @param check     Condition to check.
     * @param throwThis Functional interface that returns a Throwable.
     * @param <T>       Type of the Throwable given by the interface.
     * @throws T If condition is false.
     */
    public static <T extends Throwable> void isTrue(boolean check, Throwing<T> throwThis) throws T {
        if (!check) {
            throw throwThis.get();
        }
    }

    /**
     * Check if given parameter is false and throw exception if it is true.
     *
     * @param check     Condition to check.
     * @param throwThis Functional interface that returns a Throwable.
     * @param <T>       Type of the Throwable given by the interface.
     * @throws T If condition is true.
     */
    public static <T extends Throwable> void isFalse(boolean check, Throwing<T> throwThis) throws T {
        isTrue(!check, throwThis);
    }

    /**
     * Check that given object is not null and throw exception if it is null.
     *
     * @param obj       Object to check nullity of.
     * @param throwThis Functional interface that returns a Throwable.
     * @param <T>       Type of the Throwable given by the interface.
     * @throws T If object is null.
     */
    public static <T extends Throwable> void notNull(Object obj, Throwing<T> throwThis) throws T {
        isTrue(obj != null, throwThis);
    }

    /**
     * Check that given object is not null, throw exception if it is null and return the object if it is not null.
     *
     * @param obj       Object to check nullity of.
     * @param throwThis Functional interface that returns a Throwable.
     * @param <T>       Type of the Throwable given by the interface.
     * @param <K>       Type of the Object given to the method.
     * @return Object given to the method.
     * @throws T If object is null.
     */
    public static <T extends Throwable, K> K notNullPassThrough(K obj, Throwing<T> throwThis) throws T {
        notNull(obj, throwThis);
        return obj;
    }

    /**
     * Check that given array is not null and that it does not contain null values, throw exception if null.
     *
     * @param objects   Array of Objects to check nullity of.
     * @param throwThis Functional interface that returns a Throwable.
     * @param <T>       Type of the Throwable given by the interface.
     * @throws T If object is null.
     */
    public static <T extends Throwable> void notNull(Object[] objects, Throwing<T> throwThis) throws T {
        isTrue(objects != null, throwThis);
        for (Object obj : objects) {
            isTrue(obj != null, throwThis);
        }
    }

    /**
     * Functional interface for defining Throwables for each check.
     *
     * @param <T> Type of the Throwable given by the interface.
     */
    public interface Throwing<T extends Throwable> {
        T get();
    }
}
