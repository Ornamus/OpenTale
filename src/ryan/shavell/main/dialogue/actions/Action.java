package ryan.shavell.main.dialogue.actions;

import java.util.HashMap;
import java.util.function.BooleanSupplier;


//TODO: ActionBlockPlayerInput and ActionEnablePlayerInput (or one action that can do both)
//TODO: Some over-arching action handler that can be used across places like arena, overworld, and more with little re-implementation

public class Action {

    private static HashMap<String, Object> variables = new HashMap<>();

    private final static Class[] coreActions = {Action.class, ActionCrash.class, ActionDialog.class, ActionDialogOption.class, ActionTalk.class,
    ActionCrash.class, ActionWait.class, ActionSong.class, ActionStartEncounter.class};

    private Runnable runnable;
    private BooleanSupplier done;
    private boolean hasRun = false;

    public Action(Runnable r) {
        this(r, ()-> true);
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

    public Object get(String key) {
        return Action.variables.get(toString() + "_" + key);
    }

    public void set(String key, Object o) {
        Action.variables.put(toString() + "_" + key, o);
    }

    public boolean isDone() {
        return done.getAsBoolean();
    }

    public static Class[] getCoreActions() {
        return coreActions;
    }
}
