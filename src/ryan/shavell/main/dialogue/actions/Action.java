package ryan.shavell.main.dialogue.actions;

import java.util.function.BooleanSupplier;

public class Action {

    private Runnable runnable;
    private BooleanSupplier done;
    private boolean hasRun = false;

    public Action(Runnable r) {
        runnable = r;
        done = ()-> true;
    }

    public Action(Runnable r, BooleanSupplier b) {
        runnable = r;
        done = b;
    }

    public Action setRunnable(Runnable r) {
        runnable = r;
        return this;
    }

    public Action setDone(BooleanSupplier b) {
        done = b;
        return this;
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
