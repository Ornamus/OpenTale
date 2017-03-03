package ryan.shavell.main.dialogue.actions;

public class ActionCrash extends Action {

    public ActionCrash() {
        super(()-> {
            System.exit(-1);
        }, ()-> true);
    }
}
