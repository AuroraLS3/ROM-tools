package utils.fakeClasses;

import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.state.State;

import java.util.function.Consumer;

public class FakeFrontend implements Frontend {

    private State state;

    public FakeFrontend() {
        state = new State() {
            @Override
            public void performStateChange(Consumer<State> operation) {
                operation.accept(state);
            }
        };
    }

    @Override
    public State getState() {
        return state;
    }
}
