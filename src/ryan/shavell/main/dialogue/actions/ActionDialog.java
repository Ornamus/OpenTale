package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.core.Game;
import ryan.shavell.main.dialogue.DialogBox;

public class ActionDialog extends Action {

    public ActionDialog(String text) {
        super(()-> {
            Game.getDialogBox().setText(text, true);
        }, ()-> {
            DialogBox box = Game.getDialogBox();
            boolean done = box.shouldMoveOn();
            if (done) {
                box.setText("");
            }
            return done;
        });
    }

    public ActionDialog(String text, String voice) {
        super(()-> {
            Game.getDialogBox().setText(text, voice);
        }, ()-> {
            DialogBox box = Game.getDialogBox();
            boolean done = box.shouldMoveOn();
            if (done) {
                box.setText("");
            }
            return done;
        });
    }
}
