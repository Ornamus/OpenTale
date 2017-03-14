package ryan.shavell.main.logic.overworld.specifics;

import ryan.shavell.main.dialogue.actions.*;
import ryan.shavell.main.logic.battle.encounters.Boogie;
import ryan.shavell.main.logic.overworld.OverworldEntity;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.ImageLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OverworldBoogie extends OverworldEntity {

    private final String voice = "boogie_text";
    private final Animation portrait;

    public OverworldBoogie(int x, int y) {
        super(x, y, "boogie/boogie_ow");
        portrait = new Animation(0, ImageLoader.getImage("boogie/boogie_portrait"));
    }

    @Override
    public List<Action> onInteract() {
        List<Action> actions = new ArrayList<>();


        Collections.addAll(actions, new Action[]{
                new ActionSong("Boogie"),
                //new ActionDialog("* Hello. I'm [color(236,230,108)BOOGIE]. I'm a [color(236,230,108)FLOWER]. I guess.", voice, portrait, 0),
                new ActionDialog("* Hello./n* I'm [color(236,230,108)BOOGIE]./n* I'm a [color(236,230,108)FLOWER]. I guess.", voice, portrait, 0),
                new ActionDialog("* So, I've never seen you down here before. You must be new.", voice, portrait, 0),
                new ActionDialog("* New to the UNDERGROUND, that is.", voice, portrait, 0),
                new ActionDialog("* Gee, you must be awfully disoriented from that fall.", voice, portrait, 0),
                new ActionDialog("* I guess I can help you out by showing you how things work around here.", voice, portrait, 0),
                new ActionDialog("* Not like there's anyone else who can right now.", voice, portrait, 0),
                new ActionDialog("* Hope you're ready./n* Because, uh, I'm not.", voice, portrait, 0),
                new ActionStartEncounter(new Boogie()),
                //new ActionStartAttack()
        });

        return actions;
    }
}
