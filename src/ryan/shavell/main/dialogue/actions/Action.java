package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.dialogue.ConsumerAndSupplier;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class Action {

    private Runnable runnable;
    private BooleanSupplier done;
    private boolean hasRun = false;

    public Action(Runnable r, BooleanSupplier b) {
        runnable = r;
        done = b;
    }

    public boolean hasRun() {
        return hasRun;
    }

    public void run() {
        runnable.run();
        hasRun = true;
    }

    public boolean isDone() {
        return done.getAsBoolean();
    }
}
