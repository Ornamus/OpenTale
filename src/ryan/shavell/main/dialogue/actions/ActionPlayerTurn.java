package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.logic.entity.battle.Arena;

public class ActionPlayerTurn extends DialogAction {

    public ActionPlayerTurn() {
        super(()-> {
            Arena a = Arena.self;
            //a.mobTurn = false;
            a.freezeMenu = false;
            Arena.getDialogBox().setText(Arena.getMob().getNewTurnText());
            a.selected = 0;
        }, () -> true);
    }
}
