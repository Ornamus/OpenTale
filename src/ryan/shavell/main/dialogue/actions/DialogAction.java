package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.dialogue.ConsumerAndSupplier;

import java.util.function.Consumer;

public class DialogAction {

    private Consumer consumer;
    private ConsumerAndSupplier done;
    private boolean hasRun = false;

    public DialogAction(Consumer c, ConsumerAndSupplier d) {
        consumer = c;
        done = d;
    }

    public boolean hasRun() {
        return hasRun;
    }

    public void run(Object o) {
        consumer.accept(o);
        hasRun = true;
    }

    public boolean isDone(Object o) {
        return done.run(o);
    }
}
