package ryan.shavell.main.logic.battle.encounters;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.dialogue.actions.*;
import ryan.shavell.main.logic.battle.Mob;
import ryan.shavell.main.logic.battle.attacks.*;
import ryan.shavell.main.logic.battle.attacks.Asriel.*;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.AudioHandler;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoryshiftAsriel extends Mob {

    private SpriteSheet sheet;
    private Animation current;
    private BufferedImage mostRecentDraw;

    private String voice = "asriel_text";

    private boolean starstruck = false;
    private boolean didFlirt = false;
    private boolean usedSpecialAttack = false;

    private Animation neutral, blush_shock, blush_mad, blush_neutral, eyebrow_raise, happy_eyebrow_hard, happy_eyebrow_soft, confused, shocked, sad, sad_happy, mad;
    private BufferedImage damaged;
    //private String lastAction = null;
    private String[] randomStarts = {"Asriel is considering his clothes for later.", "Smells like goat.", "Asriel prepares a star attack.",
            "Asriel chuckles confidently.", "Asriel prepares a fire attack, but realizes his mistake and hastily fixes it.", "Asriel whispers \"So cool!\"",
            "Asriel pats his ear fur down to look more intimidating.", "Asriel remembers a joke Chara told and snorts.", "Asriel tries to play it cool."};

    private List<List<Action>> conversation = new ArrayList<>();

    public StoryshiftAsriel() {
        super(150);
        sheet = new SpriteSheet(35, 71, 7, 4, "asriel_battle_temp");
        mostRecentDraw = sheet.get(0, 0);
        current = new Animation(0, sheet.get(0, 0));
        setName("Asriel");
        setMaxHealth(130);
        setAttack(3);
        setBoss(true);
        setFleeable(false);

        int speed = 10;

        neutral = new Animation(speed, sheet.get(0, 1), sheet.get(0, 0));
        blush_shock = new Animation(speed, sheet.get(1, 1), sheet.get(1, 0));
        blush_mad = new Animation(speed, sheet.get(2, 1), sheet.get(2, 0));
        blush_neutral = new Animation(speed, sheet.get(3, 1), sheet.get(3, 0));
        eyebrow_raise = new Animation(speed, sheet.get(4, 1), sheet.get(4, 0));
        happy_eyebrow_hard = new Animation(speed, sheet.get(5, 1), sheet.get(5, 0));

        happy_eyebrow_soft = new Animation(speed, sheet.get(0, 3), sheet.get(0, 2));
        confused = new Animation(speed, sheet.get(1, 3), sheet.get(1, 2));
        shocked = new Animation(speed, sheet.get(2, 3), sheet.get(2, 2));
        sad = new Animation(speed, sheet.get(3, 3), sheet.get(3, 2));
        sad_happy = new Animation(speed, sheet.get(4, 3), sheet.get(4, 2));
        mad = new Animation(speed, sheet.get(5, 3), sheet.get(5, 2));

        damaged = sheet.get(6, 0);

        generateConversation();
    }

    @Override
    public String getNewTurnText() {
        if (starstruck && !didFlirt) {
            starstruck = false;
            didFlirt = true;
            return "* You're starstruck now.";
        } else if (isSpareable()) {
            return "* Asriel is sparing you.";
        } else {
            return "* " + randomStarts[Utils.randomNumber(0, randomStarts.length - 1)];
        }
    }

    @Override
    public Attack getNextAttack() {
        Attack a = null;
        if (!didFlirt) {
            if (getLastAction().equals("Flirt")) {
                a = new StarBlazing(true);
            } else {
                a = new JSAttack("attacks/asriel_fire_rain");
                //a = new JSAttack("var start = function(){}; var tick = function(){print(\"TICK?\");};");
            }
        } else {
            if (isSpareable()) {
                if (!usedSpecialAttack) {
                    a = new AsrielSpecial();
                } else {
                    a = new NothingAttack();
                }
            } else {
                int random = Utils.randomNumber(0, 5);
                if (random == 0) a = new JSAttack("attacks/asriel_fire_rain");
                else if (random == 1) a = new StarRain();
                else if (random == 2) a = new TestAttack2();
                else if (random == 3) a = new TestAttack3();
                else if (random == 4) a = new BlueAttack();
                else if (random == 5) a = new StarBlazing(false);
            }
        };
        return a;
    }

    @Override
    public List<Action> onPreAttack() {
        List<Action> actions = new ArrayList<>();
        if (didFlirt && conversation.size() > 0) {
            List<Action> convoPart = conversation.get(0);
            actions.addAll(convoPart);
            conversation.remove(convoPart);
            if (conversation.isEmpty()) { //Special attack
                setSpareable(true);
            }
        }
        return actions;
    }

    @Override
    public List<Action> onAfterAttack(Attack a) {
        List<Action> actions = new ArrayList<>();
        if (a instanceof StarBlazing && !didFlirt) {
            starstruck = true;
            actions.add(new ActionTalk("Did you honestly think that my strongest attack...", "asriel_text", neutral, 1));
            actions.add(new ActionTalk("Would be so predictable?", "asriel_text", neutral, 1));
            actions.add(new ActionTalk("This isn't some lousy little bullet!", "asriel_text", neutral, 1));
            actions.add(new ActionTalk("This is the stuff of legends!!", "asriel_text", eyebrow_raise, 1));
            actions.add(new ActionTalk("Hah hah hah hah!!", "asriel_text", happy_eyebrow_hard, 1));
            actions.add(new ActionImageChange(sheet.get(0, 0)));
        } else if (isSpareable() && !usedSpecialAttack) {
            AudioHandler.stopSong(getMusic());
            usedSpecialAttack = true;
            Collections.addAll(actions,
                    new ActionTalk("I... um. That's all I got.", "asriel_text", confused, 1),
                    new ActionTalk("And you clearly won't give up even after...", "asriel_text", confused, 1),
                    new ActionTalk("That.", "asriel_text", confused, 1),
                    new ActionTalk("Very well. I conceed.", "asriel_text", sad, 1),
                    new ActionTalk("I will spare you, worthy challenger.", "asriel_text", sad_happy, 1),
                    new ActionTalk("Please, accept my mercy.", "asriel_text", sad_happy, 1));
        }
        //actions.add(new ActionPlayerTurn());
        return actions;
    }

    @Override
    public List<Action> onAttack() {
        List<Action> actions = new ArrayList<>();
        return actions;
    }

    @Override
    public List<Action> onACT(String option) {
        //Log.d("ASRIEL was acted on with \"" + option + "\"!");
        List<Action> actions = new ArrayList<>();
        if (option.equals("Flirt")) {
            if (!didFlirt) {

                actions.add(new ActionTalk("Wh-whoa, what?", "asriel_text", blush_shock, 1));
                actions.add(new ActionTalk("Are you...", "asriel_text", blush_shock, 1));;
                actions.add(new ActionTalk("Are you kidding me?! This is a fight!", "asriel_text", blush_mad, 1));
                actions.add(new ActionTalk("Does this look like some joke to you?", "asriel_text", blush_neutral, 1));
                actions.add(new ActionImageChange(sheet.get(3, 0)));

                List<Action> flirtyOption = new ArrayList<>();
                List<Action> notFlirtyOption = new ArrayList<>();

                flirtyOption.add(new ActionTalk("Hah hah...", "asriel_text", blush_shock, 1));
                flirtyOption.add(new ActionTalk("Lets talk about this after the fight!", "asriel_text", blush_mad, 1));

                notFlirtyOption.add(new ActionTalk("That's what I thought!", "asriel_text", eyebrow_raise, 1));

                List<Action>[] paths = new List[]{flirtyOption, notFlirtyOption};

                actions.add(new ActionDialogOption(paths, "I'm serious about you!", "I'm serious about the fight!"));

                actions.add(new ActionTalk("So you won't fight me?", "asriel_text", neutral, 1));
                actions.add(new ActionTalk("Heh, I don't blame you.", "asriel_text", eyebrow_raise, 1));
                actions.add(new ActionTalk("Let's see how you handle my fabled STAR BLAZING.", "asriel_text", eyebrow_raise, 1));
                actions.add(new ActionTalk("Behold!", "asriel_text", eyebrow_raise, 1));
                actions.add(new ActionImageChange(sheet.get(0, 0)));
            } else {
                actions.add(new ActionDialog("* Asriel is too distracted to flirt. Seems like ACTing won't escalate this battle."));
            }
        } else if (option.equals("Insult")) {
            if (didFlirt)
            actions.add(new ActionDialog("* Asriel is too busy with his speech to pay attention to what you're saying."));
        }
        //actions.add(new ActionTriggerPreAttack());
        //setLastAction(option);
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
    public void draw(Graphics2D g, int x, int y, boolean hit) {
        mostRecentDraw = hit ? damaged : current.getImage();
        g.drawImage(mostRecentDraw, x, y, mostRecentDraw.getWidth()  * 2, mostRecentDraw.getHeight() * 2, null);
    }

    @Override
    public String getCheckInfo() {
        //setLastAction("CHECK");
        return super.getCheckInfo() + "/n* Puts on a brave face, but gets emotional very easily.";
    }

    @Override
    public int getX() {
        return (Main.WIDTH / 2) - (mostRecentDraw.getWidth());
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
    public String getMusic() {
        return "StarScuffle"; //Asriel
    }

    public void generateConversation() {
        List<List<Action>> convo = new ArrayList<>();

        convo.add(simplePart("Is my warrior outfit ready to be seen...?", neutral));
        convo.add(simplePart("What? No, the flirting isn't distracting me!", blush_shock));
        convo.add(simplePart("You know, STAR BLAZING isn't my only original attack.", neutral));
        convo.add(simplePart("A good deity keeps a few secrets up his sleeve.", eyebrow_raise));
        convo.add(simplePart("I'll be a great/nCaptain of the Royal Guard!", neutral));
        convo.add(simplePart("Dad will be so proud and Mom will bake a pie for me!", happy_eyebrow_soft));
        convo.add(simplePart("Chara will be glad but...", neutral));
        convo.add(simplePart("Won't I have to leave them behind to go on patrol a lot?", sad));
        convo.add(simplePart("And the family will be torn apart even more.", sad));
        convo.add(simplePart("Ugh, that's future me's problem!", mad));
        convo.add(simplePart("Charging my [color(255,0,0)special attack] now!", neutral));
        convo.add(simplePart("Hope your b-butt is ready!", eyebrow_raise));
        convo.add(simplePart("What's that look for? It's a human saying.", neutral));
        convo.add(simplePart("Okay, so I haven't practiced my insults a lot.", neutral));
        convo.add(simplePart("Speeches are harder to remember.", blush_neutral));
        convo.add(simplePart("I don't want to be rude to you either...", sad));

        Collections.addAll(convo, toList(new ActionTalk("Anyway! Charging is all done!", "asriel_text", neutral, 1),
                new ActionTalk("Here comes my [color(255,0,0)special secret attack]!", "asriel_text", eyebrow_raise, 1),
                new ActionImageChange(shocked.getFrame(1)),
                new ActionDialog("(the strange dog appears)"),
                new ActionTalk("...", "asriel_text", shocked, 1),
                new ActionTalk("Uh.", "asriel_text", shocked, 1),
                new ActionDialog("(the dog leaves with the special attack)"),
                new ActionDialog("* What."), new ActionTalk("O...kay then...", "asriel_text", shocked, 1),
                new ActionTalk("I guess, um, have a normal special attack?", "asriel_text", confused, 1)));

        conversation.clear();
        conversation = convo;
    }

    public List<Action> toList(Action... actions) {
        List<Action> list = new ArrayList<>();
        Collections.addAll(list, actions);
        return list;
    }

    public List<Action> simplePart(String text, Animation anim) {
        return Collections.singletonList(new ActionTalk(text, "asriel_text", anim, 1));
    }
}
