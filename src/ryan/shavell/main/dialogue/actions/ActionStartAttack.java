package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.logic.entity.battle.Arena;

/**
 * Created by Ryan Shavell on 2/23/2017.
 */
public class ActionStartAttack extends DialogAction {
    public ActionStartAttack() {
        super(()-> {
            Arena.self.startMobTurn();
        }, ()-> {
            return true;
        });
    }
}
