package ryan.shavell.main.core;

import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.dialogue.actions.Action;
import ryan.shavell.main.logic.battle.Arena;
import ryan.shavell.main.logic.overworld.Overworld;
import ryan.shavell.main.stuff.Log;

import java.util.List;

public class Game {

    private static State state = State.MENU;

    public static DialogBox getDialogBox() {
        if (isOverworld()) {
            return Overworld.getDialogBox();
        } else if (isEncounter()) {
            return Arena.getDialogBox();
        } else {
            Log.e("Called Game.getDialogBox() while in the Menu State!");
            return null;
        }
    }

    public static List<Action> getActionBuffer() {
        if (isOverworld()) {
            return Overworld.self.actions;
        } else if (isEncounter()) {
            return Arena.self.actions;
        } else  {
            Log.e("Called Game.getActionBuffer() while in the Menu State!");
            return null;
        }
    }

    public static boolean isMenu() {
        return isState(State.MENU);
    }

    public static boolean isOverworld() {
        return isState(State.OVERWORLD);
    }

    public static boolean isEncounter() {
        return isState(State.ENCOUNTER);
    }

    public static State getState() {
        return state;
    }

    public static void setState(State s) {
        state = s;
    }

    private static boolean isState(State s) {
        return state == s;
    }

    public enum State {
        MENU,
        OVERWORLD,
        ENCOUNTER
    }
}
