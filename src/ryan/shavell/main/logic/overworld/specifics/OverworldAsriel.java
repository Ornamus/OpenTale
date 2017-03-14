package ryan.shavell.main.logic.overworld.specifics;

import ryan.shavell.main.dialogue.actions.*;
import ryan.shavell.main.logic.battle.encounters.StoryshiftAsriel;
import ryan.shavell.main.logic.overworld.OverworldEntity;
import ryan.shavell.main.resources.SpriteSheet;
import java.util.ArrayList;
import java.util.List;

public class OverworldAsriel extends OverworldEntity {

    public OverworldAsriel(int x, int y) {
        super(x, y, new SpriteSheet(16, 28, 4, 1, "asriel_overworld").get(0,0));
    }

    @Override
    public List<Action> onInteract() {
        String voice = "asriel_text";
        List<Action> actions = new ArrayList<>();
        actions.add(new ActionDialog("* Hey there, newcomer!/n* What's up?", voice));

        List<Action> no = new ArrayList<>();
        List<Action> yes = new ArrayList<>();

        no.add(new ActionDialog("* Alright! Cool!/n* Talk to you later, then.", voice));
        no.add(new ActionCrash());

        yes.add(new ActionDialog("* Alright, yeah, sure!", voice));
        yes.add(new ActionDialog("* Just don't forget this is just a demo, okay?", voice));
        yes.add(new ActionDialog("* Ahem...", voice));

        yes.add(new ActionDialog("* So you finally came.", voice));
        yes.add(new ActionDialog("* At last. Time to show what we are really made of.", voice));
        yes.add(new ActionDialog("* God against mortal.", voice));
        yes.add(new ActionDialog("* Monster against human.", voice));
        yes.add(new ActionDialog("* That look in your eyes...", voice));
        yes.add(new ActionDialog("* That's the same look Chara gives when we play.", voice));
        yes.add(new ActionDialog("* Heh heh heh.", voice));
        yes.add(new ActionDialog("* You just won't give up no matter what, will you?", voice));
        yes.add(new ActionDialog("* Good, good. We'll have a great time.", voice));
        yes.add(new ActionDialog("* But enough stalling.", voice));
        yes.add(new ActionDialog("* Prepare yourself human!", voice));
        yes.add(new ActionDialog("* Let me show you my true power!", voice));
        yes.add(new ActionDialog("* Here's [color(187,102,187)Asriel Dreemurr]!", voice));
        yes.add(new ActionDialog("* The future Captain of the Royal Guard!", voice));
        yes.add(new ActionDialog("* And protector of all monsters!!", voice));

        yes.add(new ActionStartEncounter(new StoryshiftAsriel()));

        actions.add(new ActionDialogOption(new List[]{yes, no}, "Lets fight.", "Nothing."));

        return actions;
    }
}
