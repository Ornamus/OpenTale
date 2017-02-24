package ryan.shavell.main.logic.entity.battle;

import ryan.shavell.main.dialogue.actions.*;
import ryan.shavell.main.logic.entity.battle.attacks.*;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class StoryshiftAsriel extends Mob {

    private SpriteSheet sheet;
    private BufferedImage current;

    private Animation talk1, talk2, talk3, talk4, talk5;
    private String lastAction = null;
    private String[] randomStarts = {"Asriel is considering his clothes for later.", "Smells like goat.", "Asriel prepares a star attack.",
            "Asriel chuckles confidently.", "Asriel prepares a fire attack, but realizes his mistake and hastily fixes it.", "Asriel whispers \"So cool!\"",
            "Asriel pats his ear fur down to look more intimidating.", "Asriel remembers a joke Chara told and snorts.", "Asriel tries to play it cool."};

    public StoryshiftAsriel() {
        super(100);
        sheet = new SpriteSheet(35, 71, 6, 2, "asriel_battle_temp");
        current = sheet.getImage(0, 0);
        setName("Asriel");
        setMaxHealth(80);
        setAttack(3);
    }

    @Override
    public String getNewTurnText() {
        if (starstruck) {
            return "* You're starstruck now.";
        } else {
            return "* " + randomStarts[Utils.randomNumber(0, randomStarts.length - 1)];
        }
    }

    @Override
    public Attack getNextAttack() {
        Attack a;
        if (lastAction.equals("Flirt")) {
            a = new TestAttack2();
        } else {
            a = new FireRain();
        }
        return a;
    }

    boolean starstruck = false;

    @Override
    public List<DialogAction> onAfterAttack(Attack a) {
        List<DialogAction> actions = new ArrayList<>();
        if (a instanceof TestAttack2) {
            starstruck = true;
            actions.add(new ActionImageChange(sheet.getImage(0, 0)));
            actions.add(new ActionTalk("Did you honestly think that my strongest attack...", "asriel_text"));
            actions.add(new ActionTalk("Would be so predictable?", "asriel_text"));
            actions.add(new ActionTalk("This isn't some lousy little bullet!", "asriel_text"));
            actions.add(new ActionImageChange(sheet.getImage(4, 0)));
            actions.add(new ActionTalk("This is the stuff of legends!!", "asriel_text"));
            actions.add(new ActionTalk("Hah hah hah hah!!", "asriel_text"));
            actions.add(new ActionImageChange(sheet.getImage(0, 0)));
        }
        actions.add(new ActionPlayerTurn());
        return actions;
    }

    @Override
    public List<DialogAction> onAttack() {
        List<DialogAction> actions = new ArrayList<>();
        /*
        actions.add(new ActionImageChange(sheet.getImage(4, 0)));
        actions.add(new ActionTalk("You think THAT can stop me?", "asriel_text"));
        */
        actions.add(new ActionStartAttack());
        lastAction = "FIGHT";
        return actions;
    }

    @Override
    public List<DialogAction> onACT(String option) {
        System.out.println("ASRIEL was acted on with \"" + option + "\"!");
        List<DialogAction> actions = new ArrayList<>();
        if (option.equals("Flirt")) {
            actions.add(new ActionImageChange(sheet.getImage(1, 0)));
            actions.add(new ActionTalk("Wh-whoa, what?", "asriel_text"));
            actions.add(new ActionTalk("Are you...", "asriel_text"));
            actions.add(new ActionImageChange(sheet.getImage(2, 0)));
            actions.add(new ActionTalk("Are you kidding me?! This is a fight!", "asriel_text"));
            actions.add(new ActionImageChange(sheet.getImage(3, 0)));
            actions.add(new ActionTalk("Does this look like some joke to you?", "asriel_text"));
            actions.add(new ActionDialog("(The option dialog about seriousness pops up here)"));
            actions.add(new ActionTalk("Hah hah...", "asriel_text"));
            actions.add(new ActionTalk("Lets talk about this after the fight!", "asriel_text"));
            actions.add(new ActionImageChange(sheet.getImage(0, 0)));
            actions.add(new ActionTalk("So you won't fight me?", "asriel_text"));
            actions.add(new ActionImageChange(sheet.getImage(4, 0)));
            actions.add(new ActionTalk("Heh, I don't blame you.", "asriel_text"));
            actions.add(new ActionTalk("Let's see how you handle my fabled STAR BLAZING.", "asriel_text"));
            actions.add(new ActionTalk("Behold!", "asriel_text"));
        } else if (option.equals("Insult")) {
            actions.add(new ActionImageChange(sheet.getImage(0, 0)));
            actions.add(new ActionTalk("Hey, I know we're battling and all, but...", "asriel_text"));
            actions.add(new ActionTalk("...You don't need to make it personal, you know?", "asriel_text"));
            actions.add(new ActionTalk("......", "asriel_text"));
            actions.add(new ActionTalk("I wasn't going to use this, but since you're being a little nasty...", "asriel_text"));
            actions.add(new ActionTalk("...I'll use this move a skeleton taught me!", "asriel_text"));
        }
        actions.add(new ActionStartAttack());
        lastAction = option;
        return actions;
    }

    @Override
    public String[] getACT() {
        lastAction = "CHECK";
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
        return sheet.getImage(5, 0);
    }

    @Override
    public String getMusic() {
        return "Asriel"; //TODO: re-crop histrousle and fade out?
    }
}
