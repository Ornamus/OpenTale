package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.logic.battle.Mob;
import ryan.shavell.main.logic.overworld.Overworld;

public class ActionStartEncounter extends Action {
    public ActionStartEncounter(Mob m) {
        super(()-> {
            Overworld.self.startEncounter(m);
        }, ()-> true);
    }
}
