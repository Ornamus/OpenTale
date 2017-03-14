package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.logic.battle.Arena;
import ryan.shavell.main.resources.Animation;

import java.awt.image.BufferedImage;

public class ActionImageChange extends Action {

    public ActionImageChange(BufferedImage i) {
        super(()-> {
            Arena.getMob().setImage(i);
        }, ()-> true);
    }

    public ActionImageChange(Animation a) {
        super(()-> {
            Arena.getMob().setAnimation(a);
        }, ()-> true);
    }
}