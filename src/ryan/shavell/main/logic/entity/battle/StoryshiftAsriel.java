package ryan.shavell.main.logic.entity.battle;

import ryan.shavell.main.dialogue.actions.*;
import ryan.shavell.main.logic.entity.battle.attacks.*;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StoryshiftAsriel extends Mob {

    private SpriteSheet sheet;
    private Animation current;

    private boolean starstruck = false;
    private boolean didFlirt = false;

    private Animation talk0, talk1, talk2, talk3, talk4;
    private String lastAction = null;
    private String[] randomStarts = {"Asriel is considering his clothes for later.", "Smells like goat.", "Asriel prepares a star attack.",
            "Asriel chuckles confidently.", "Asriel prepares a fire attack, but realizes his mistake and hastily fixes it.", "Asriel whispers \"So cool!\"",
            "Asriel pats his ear fur down to look more intimidating.", "Asriel remembers a joke Chara told and snorts.", "Asriel tries to play it cool."};

    private List<String> conversation = new ArrayList<>();

    //TODO: SIGH...convert each group of these to a list of actions
    private String[] conversationArray = {"Is my warrior outfit ready to be seen...?", "What? No, the flirting isn't distracting me!",
            "You know, STAR BLAZING isn't my only original attack.", "A good deity keeps a few secrets up his sleeve.", "I'll be a great/nCaptain of the Royal Guard!",
            "Dad will be so proud and Mom will bake a pie for me!", "Chara will be glad but...", "Won't I have to leave them behind to go on patrol a lot?",
            "And the family will be torn apart even more.", "Ugh, that's future me's problem!", "Charging my special attack now!", "Hope your b-butt is ready!",
            "What's that look for? It's a human saying.", "Okay, so I haven't practiced my insults a lot.", "Speeches are harder to remember.",
            "I don't want to be rude to you either...", "Anyway! Charging is all done!"};

    public StoryshiftAsriel() {
        super(100);
        sheet = new SpriteSheet(35, 71, 6, 2, "asriel_battle_temp");
        current = new Animation(0, sheet.getImage(0, 0));
        setName("Asriel");
        setMaxHealth(80);
        setAttack(3);
        setBoss(true);

        for (String s : conversationArray) {
            conversation.add(s);
        }

        int speed = 10;

        talk0 = new Animation(speed, sheet.getImage(0, 0), sheet.getImage(0, 1));
        talk1 = new Animation(speed, sheet.getImage(1, 0), sheet.getImage(1, 1));
        talk2 = new Animation(speed, sheet.getImage(2, 0), sheet.getImage(2, 1));
        talk3 = new Animation(speed, sheet.getImage(3, 0), sheet.getImage(3, 1));
        talk4 = new Animation(speed, sheet.getImage(4, 0), sheet.getImage(4, 1));
    }

    @Override
    public String getNewTurnText() {
        if (starstruck && !didFlirt) {
            starstruck = false;
            didFlirt = true;
            return "* You're starstruck now.";
        } else {
            return "* " + randomStarts[Utils.randomNumber(0, randomStarts.length - 1)];
        }
    }

    @Override
    public Attack getNextAttack() {
        Attack a = null;
        if (!didFlirt) {
            if (lastAction.equals("Flirt")) {
                a = new TestAttack2();
            } else {
                a = new FireRain();
            }
        } else {
            int random = Utils.randomNumber(0, 4);
            if (random == 0) a = new FireRain();
            else if (random == 1) a = new TestAttack();
            else if (random == 2) a = new TestAttack2();
            else if (random == 3) a = new TestAttack3();
            else if (random == 4) a = new BlueAttack();
        }
        return a;
    }

    @Override
    public List<Action> onPreAttack() {
        List<Action> actions = new ArrayList<>();
        if (didFlirt && conversation.size() > 0) {
            String response = conversation.get(0);
            actions.add(new ActionTalk(response, "asriel_text"));
            conversation.remove(response);
        }
        actions.add(new ActionStartAttack());
        return actions;
    }

    @Override
    public List<Action> onAfterAttack(Attack a) {
        List<Action> actions = new ArrayList<>();
        if (a instanceof TestAttack2 && !didFlirt) {
            starstruck = true;
            actions.add(new ActionImageChange(talk0));
            actions.add(new ActionTalk("Did you honestly think that my strongest attack...", "asriel_text"));
            actions.add(new ActionTalk("Would be so predictable?", "asriel_text"));
            actions.add(new ActionTalk("This isn't some lousy little bullet!", "asriel_text"));
            actions.add(new ActionImageChange(talk4));
            actions.add(new ActionTalk("This is the stuff of legends!!", "asriel_text"));
            actions.add(new ActionTalk("Hah hah hah hah!!", "asriel_text"));
            actions.add(new ActionImageChange(sheet.getImage(0, 0)));
        }
        actions.add(new ActionPlayerTurn());
        return actions;
    }

    @Override
    public List<Action> onAttack() {
        List<Action> actions = new ArrayList<>();
        /*
        actions.add(new ActionImageChange(sheet.getImage(4, 0)));
        actions.add(new ActionTalk("You think THAT can stop me?", "asriel_text"));
        */
        actions.add(new ActionTriggerPreAttack());
        lastAction = "FIGHT";
        return actions;
    }

    @Override
    public List<Action> onACT(String option) {
        System.out.println("ASRIEL was acted on with \"" + option + "\"!");
        List<Action> actions = new ArrayList<>();
        if (option.equals("Flirt")) {
            if (!didFlirt) {
                actions.add(new ActionImageChange(talk1));
                actions.add(new ActionTalk("Wh-whoa, what?", "asriel_text"));
                actions.add(new ActionTalk("Are you...", "asriel_text"));
                actions.add(new ActionImageChange(talk2));
                actions.add(new ActionTalk("Are you kidding me?! This is a fight!", "asriel_text"));
                actions.add(new ActionImageChange(talk3));
                actions.add(new ActionTalk("Does this look like some joke to you?", "asriel_text"));
                actions.add(new ActionImageChange(sheet.getImage(3, 0)));

                List<Action> flirtyOption = new ArrayList<>();
                List<Action> notFlirtyOption = new ArrayList<>();

                flirtyOption.add(new ActionImageChange(talk1));
                flirtyOption.add(new ActionTalk("Hah hah...", "asriel_text"));
                flirtyOption.add(new ActionImageChange(talk2));
                flirtyOption.add(new ActionTalk("Lets talk about this after the fight!", "asriel_text"));

                notFlirtyOption.add(new ActionImageChange(talk4));
                notFlirtyOption.add(new ActionTalk("That's what I thought!", "asriel_text"));

                List<Action>[] paths = new List[]{flirtyOption, notFlirtyOption};

                actions.add(new ActionDialogOption(paths, "I'm serious about you!", "I'm serious about the fight!"));

                actions.add(new ActionImageChange(talk0));
                actions.add(new ActionTalk("So you won't fight me?", "asriel_text"));
                actions.add(new ActionImageChange(talk4));
                actions.add(new ActionTalk("Heh, I don't blame you.", "asriel_text"));
                actions.add(new ActionTalk("Let's see how you handle my fabled STAR BLAZING.", "asriel_text"));
                actions.add(new ActionTalk("Behold!", "asriel_text"));
                actions.add(new ActionImageChange(sheet.getImage(0, 0)));
            } else {
                actions.add(new ActionDialog("* Asriel is too distracted to flirt. Seems like ACTing won't escalate this battle."));
            }
        } else if (option.equals("Insult")) {

        }
        actions.add(new ActionTriggerPreAttack());
        lastAction = option;
        return actions;
    }

    @Override
    public List<Action> onSPARE() {
        List<Action> actions = new ArrayList<>();

        //TODO

        actions.add(new ActionTriggerPreAttack());
        return actions;
    }

    @Override
    public String[] getACT() {
        return new String[]{"Insult", "Flirt"};
    }

    @Override
    public String getCheckInfo() {
        lastAction = "CHECK";
        return super.getCheckInfo() + "/n* Puts on a brave face, but gets emotional very easily.";
    }

    @Override
    public void setAnimation(Animation a) {
        current = a;
    }

    @Override
    public void setImage(BufferedImage i) {
        current = new Animation(0, i);
    }

    @Override
    public BufferedImage getImage() {
        return current.getImage();
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
