package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.logic.battle.Arena;

public class ActionStartAttack extends Action {
    public ActionStartAttack() {
        super(()-> {
            Arena.self.startMobTurn();
        }, ()-> true);
    }
}
