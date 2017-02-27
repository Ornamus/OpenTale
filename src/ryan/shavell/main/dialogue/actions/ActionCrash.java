package ryan.shavell.main.dialogue.actions;

import java.util.function.BooleanSupplier;

public class ActionCrash extends Action {

    public ActionCrash() {
        super(()-> {
            System.exit(-1);
        }, ()-> true);
    }
}
