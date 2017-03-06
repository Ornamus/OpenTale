package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.logic.entity.battle.Arena;

import java.util.List;

public class ActionTriggerPreAttack extends Action {

    public ActionTriggerPreAttack() {
        super(()-> {
            List<Action> actions = Arena.getMob().onPreAttack();
            actions.add(new ActionStartAttack());
            //Arena.self.actions.addAll(actions);
            Arena.self.actions = actions;
        }, ()-> true);
    }
}
