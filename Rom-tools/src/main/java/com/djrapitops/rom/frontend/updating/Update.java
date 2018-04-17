package com.djrapitops.rom.frontend.updating;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Class for calling Updatable components after the Future has been completed.
 *
 * @author Rsl1122
 */
public class Update<K> {

    private final Updatable<K> updatable;
    private Future<K> future;
    private K result;

    public Update(Updatable<K> updatable, Future<K> withResult) {
        this.updatable = updatable;
        this.future = withResult;
    }

    public Update(Updatable<K> updatable, K result) {
        this.updatable = updatable;
        this.result = result;
    }

    boolean isDone() {
        return future.isDone();
    }

    /**
     * Updates the UI Node with the result of the Future.
     *
     * @throws ExecutionException   If program has been shut down before calling this.
     * @throws InterruptedException If program has been shut down before calling this.
     */
    void update() throws ExecutionException, InterruptedException {
        updatable.update(getResult());
    }

    private K getResult() throws ExecutionException, InterruptedException {
        return result != null ? result : future.get();
    }
}
