package ryan.shavell.main.dialogue.actions;;

public class ActionWait extends Action {

    public ActionWait(final double seconds) {
        super(()->{
        }, ()->false);
        final Action action = this;
        setRunnable(()-> {
            action.set("start", System.currentTimeMillis());
        });
        setDone(()-> {
           return System.currentTimeMillis() - ((Long) action.get("start")) > (seconds * 1000);
        });
    }
}
