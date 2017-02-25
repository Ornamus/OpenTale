package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.logic.entity.battle.Arena;

public class ActionDialog extends Action {

    public ActionDialog(String text) {
        super(()-> {
            Arena.getDialogBox().setText(text, true);
        }, ()-> {
            DialogBox box = Arena.getDialogBox();
            boolean done = /*!box.isScrolling() && */box.shouldMoveOn();
            if (done) {
                box.setText("");
            }
            return done;
        });
    }
}
