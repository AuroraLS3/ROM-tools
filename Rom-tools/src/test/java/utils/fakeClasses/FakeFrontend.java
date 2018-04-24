package utils.fakeClasses;

import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.state.State;
import com.djrapitops.rom.frontend.state.StateOperation;

public class FakeFrontend implements Frontend {

    private State state;

    public FakeFrontend() {
        state = new State() {
            @Override
            public void performStateChange(StateOperation operation) {
                operation.operateOnState(state);
            }
        };
    }

    @Override
    public State getState() {
        return state;
    }
}
