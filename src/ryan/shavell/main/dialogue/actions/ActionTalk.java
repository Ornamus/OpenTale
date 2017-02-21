package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.logic.entity.battle.Arena;

public class ActionTalk extends DialogAction {

    public ActionTalk(String text) {
        super(()-> {
            Arena.getDialogBox().setText(text, true);
        }, ()-> {
            DialogBox box = Arena.getDialogBox();
            return !box.isScrolling() && box.shouldMoveOn();
        });
    }
}
