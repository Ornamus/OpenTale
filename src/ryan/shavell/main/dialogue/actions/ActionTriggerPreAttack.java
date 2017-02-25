package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.logic.entity.battle.Arena;

import java.util.List;
import java.util.function.BooleanSupplier;

public class ActionTriggerPreAttack extends Action {

    public ActionTriggerPreAttack() {
        super(()-> {
            List<Action> actions = Arena.getMob().onPreAttack();
            Arena.self.actions.addAll(actions);
        }, ()-> true);
    }
}
