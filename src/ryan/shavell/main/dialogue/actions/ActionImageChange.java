package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.logic.entity.battle.Arena;
import java.awt.image.BufferedImage;

public class ActionImageChange extends DialogAction {

    public ActionImageChange(BufferedImage i) {
        super(()-> {
            Arena.getMob().setImage(i);
        }, ()-> {
            return true;
        });
    }
}
