package utils.fakeClasses;

import com.djrapitops.rom.frontend.Frontend;
import com.djrapitops.rom.frontend.state.State;

public class FakeFrontend implements Frontend {

    @Override
    public State getState() {
        return null;
    }
}
