package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.dialogue.DialogBox;

public class ActionTalk extends DialogAction {

    public ActionTalk(String text) {
        super((Object o)-> {
            if (o instanceof DialogBox) {
                ((DialogBox)o).setText(text, true);
            }
        }, (Object o)-> {
            if (o instanceof DialogBox) {
                DialogBox box = (DialogBox) o;
                return !box.isScrolling() && box.shouldMoveOn();
            } else {
                return false;
            }
        });
    }
}
