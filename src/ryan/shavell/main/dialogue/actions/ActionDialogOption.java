package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.core.Game;
import ryan.shavell.main.dialogue.DialogBox;

import java.util.Collections;
import java.util.List;

public class ActionDialogOption extends Action {

    public ActionDialogOption(List<Action>[] paths, String... options) {
        super(()-> {
            Game.getDialogBox().setOptions(options);
        }, ()-> {
            DialogBox box = Game.getDialogBox();
            boolean done = box.shouldMoveOn();
            if (done) {
                List<Action> actions = paths[box.getSelectedOption()];
                actions.add(0, new ActionDialog(""));
                int index = 0;
                for (Action a : Game.getActionBuffer()) {
                    if (a instanceof ActionDialogOption) { //TODO: fix this for multiple of these in a given chain
                        break;
                    }
                    index++;
                }
                Collections.reverse(actions);
                for (Action a : actions) {
                    Game.getActionBuffer().add(index, a);
                }
            }
            return done;
        });
    }
}
