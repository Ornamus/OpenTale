package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.logic.entity.battle.Arena;

import java.util.Collections;
import java.util.List;

public class ActionDialogOption extends Action {

    public ActionDialogOption(List<Action>[] paths, String... options) {
        super(()-> {
            Arena.getDialogBox().setOptions(options);
        }, ()-> {
            DialogBox box = Arena.getDialogBox();
            boolean done = box.shouldMoveOn();
            if (done) {
                List<Action> actions = paths[box.getSelectedOption()];
                actions.add(0, new ActionDialog(""));
                int index = 0;
                for (Action a : Arena.self.actions) {
                    if (a instanceof ActionDialogOption) { //TODO: fix this for multiple of these in a given chain
                        break;
                    }
                    index++;
                }
                Collections.reverse(actions);
                for (Action a : actions) {
                    Arena.self.actions.add(index, a);
                }
            }
            return done;
        });
    }
}
