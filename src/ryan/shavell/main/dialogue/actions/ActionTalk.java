package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.dialogue.ChatBox;
import ryan.shavell.main.logic.entity.battle.Arena;
import ryan.shavell.main.resources.Animation;

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

    public ActionTalk(String text, String sound, Animation talkAnim, int stopFrameOfAnim) {
        super(()-> {
            Arena.getChatBubble().setText(text, sound);
            talkAnim.reset();
            Arena.getMob().setAnimation(talkAnim);
        }, ()-> {
            ChatBox chat = Arena.getChatBubble();
            if (!chat.isScrolling()) {
                talkAnim.setCurrentFrame(stopFrameOfAnim);
                talkAnim.setPaused(true);
            }
            boolean done = !chat.isScrolling() && chat.shouldMoveOn();
            if (done) {
                chat.setText("");
            }
            return done;
        });
    }
}
