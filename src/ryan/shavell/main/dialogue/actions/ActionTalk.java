package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.dialogue.ChatBox;
import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.logic.entity.battle.Arena;

public class ActionTalk extends DialogAction {

    public ActionTalk(String text) {
        super(()-> {
            Arena.getChatBubble().setText(text, true);
        }, ()-> {
            ChatBox box = Arena.getChatBubble();
            boolean done = !box.isScrolling() && box.shouldMoveOn();
            if (done) {
                box.setText("");
            }
            return done;
        });
    }
}
