package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.core.Game;
import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.resources.Animation;

public class ActionDialog extends Action {

    public ActionDialog(String text) {
        this(text, null);
    }

    public ActionDialog(String text, String voice) {
        super(()-> {
            if (voice == null) {
                Game.getDialogBox().setText(text, true);
            } else {
                Game.getDialogBox().setText(text, voice);
            }
        }, ()-> {
            DialogBox box = Game.getDialogBox();
            boolean done = box.shouldMoveOn();
            if (done) {
                box.setText("");
            }
            return done;
        });
    }

    public ActionDialog(String text, String voice, Animation portrait, int doneFrame) {
        super(()-> {
            Game.getDialogBox().setPortrait(portrait, doneFrame);
            if (voice == null) {
                Game.getDialogBox().setText(text, true);
            } else {
                Game.getDialogBox().setText(text, voice);
            }
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
