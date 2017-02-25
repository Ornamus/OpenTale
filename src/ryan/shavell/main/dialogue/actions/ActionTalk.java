package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.dialogue.ChatBox;
import ryan.shavell.main.logic.entity.battle.Arena;

public class ActionTalk extends Action {

    public ActionTalk(String text, String sound) {
        super(()-> {
            Arena.getChatBubble().setText(text, sound);
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
