package ryan.shavell.main.logic.overworld.specifics;

import ryan.shavell.main.dialogue.actions.*;
import ryan.shavell.main.logic.battle.encounters.Boogie;
import ryan.shavell.main.logic.overworld.OverworldEntity;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.ImageLoader;
import ryan.shavell.main.scripting.Script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OverworldBoogie extends OverworldEntity {

    private final Script script;

    public OverworldBoogie(int x, int y) {
        super(x, y, "boogie/boogie_ow");

        script = Script.loadScript("talking/boogie_tutorial");
        script.put("mob", this);
    }

    @Override
    public List<Action> onInteract() {
        List<Action> actions = (List<Action>) script.invokeMethod("run");

        return actions;
    }
}
