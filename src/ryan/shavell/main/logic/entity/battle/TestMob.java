package ryan.shavell.main.logic.entity.battle;

import ryan.shavell.main.dialogue.actions.ActionImageChange;
import ryan.shavell.main.dialogue.actions.ActionTalk;
import ryan.shavell.main.dialogue.actions.DialogAction;
import ryan.shavell.main.resources.SpriteSheet;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TestMob extends Mob {

    private SpriteSheet sheet;
    private BufferedImage current;

    public TestMob() {
        super(100);
        sheet = new SpriteSheet(35, 71, 2, 1, "asriel_battle_temp");
        current = sheet.getImage(0, 0);
        setName("Asriel");
        setMaxHealth(10);
        setAttack(3);
    }

    @Override
    public List<DialogAction> onACT(String option) {
        System.out.println("ASRIEL was acted on with \"" + option + "\"!");
        List<DialogAction> actions = new ArrayList<>();
        if (option.equals("Insult")) {
            actions.add(new ActionImageChange(sheet.getImage(1, 0)));
            actions.add(new ActionTalk("Wha...really?"));
            actions.add(new ActionTalk("Wow, I guess you really ARE a jerk!"));
            actions.add(new ActionImageChange(sheet.getImage(0, 0)));
            actions.add(new ActionTalk("Well, I guess it's time for you to die now."));
        }
        return actions;
    }

    @Override
    public String[] getACT() {
        return new String[]{"Insult", "Flirt"};
    }

    @Override
    public String getCheckInfo() {
        return super.getCheckInfo() + "/n* Puts on a brave face, but gets emotional very easily.";
    }

    @Override
    public void setImage(BufferedImage i) {
        current = i;
    }

    @Override
    public BufferedImage getImage() {
        return current;
    }

    @Override
    public BufferedImage getHitImage() {
        return sheet.getImage(1, 0);
    }

    @Override
    public String getMusic() {
        return "Asriel"; //TODO: re-crop histrousle and fade out?
    }
}
