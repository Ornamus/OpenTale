package ryan.shavell.main.logic.entity.battle;

import ryan.shavell.main.dialogue.actions.*;
import ryan.shavell.main.logic.entity.battle.attacks.*;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Utils;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestMob extends Mob {

    private SpriteSheet sheet;
    private BufferedImage current;

    private Animation talk1, talk2, talk3, talk4, talk5;
    private String lastAction = null;

    /*
    private String[] randomStarts = {"Asriel pats his fur down.", "Asriel thinks about what to wear for his date.", "Asriel whispers \"So cool!\"",
    "Asriel wonders where Chara hid his God of Hyperdeath outfit.", "Asriel prepares a star attack.", "Asriel is excited to be the test mob for the engine.",
    "Asriel imagines you as a tiny, yellow flower. He chuckles.", "Asriel seems to be concentrating on something.",
    "Asriel has sent you a file:/nNot_a_Virus.exe"};
    */

    private String[] randomStarts = {"Asriel is considering his clothes for later.", "Smells like goat.", "Asriel prepares a star attack.",
    "Asriel chuckles confidently.", "Asriel prepares a fire attack, but realizes his mistake and hastily fixes it.", "Asriel whispers \"So cool!\"",
    "Asriel pats his ear fur down to look more intimidating.", "Asriel remembers a joke Chara told and snorts.", "Asriel tries to play it cool."};

    public TestMob() {
        super(100);
        sheet = new SpriteSheet(35, 71, 6, 2, "asriel_battle_temp");
        current = sheet.getImage(0, 0);
        setName("Asriel");
        setMaxHealth(80);
        setAttack(3);
    }

    @Override
    public String getNewTurnText() {
        if (lastAction.equals("Flirt")) {
            return "* Asriel seems flustered.";
        } else if (lastAction.equals("Insult")) {
            return "* Asriel resists the urge to say \"NYEH HEH HEH!\"";
        } else {
            return "* " + randomStarts[Utils.randomNumber(0, randomStarts.length - 1)];
        }
    }

    @Override
    public Attack getNextAttack() {
        Attack a = null;
        if (lastAction.equals("Insult")) {
            a = new BlueAttack();
        } else {
            int num = Utils.randomNumber(0, 3);
            if (num == 0) a = new TestAttack();
            else if (num == 1) a = new TestAttack2();
            else if (num == 2) a = new TestAttack3();
            else if (num == 3) a = new FireRain();
        }
        //a = new TestAttack3();
        return a;
    }

    @Override
    public List<Action> onPreAttack() {
        return Collections.singletonList(new ActionStartAttack());
    }

    @Override
    public List<Action> onAfterAttack(Attack a) {
        List<Action> actions = new ArrayList<>();
        actions.add(new ActionPlayerTurn());
        return actions;
    }

    @Override
    public List<Action> onAttack() {
        List<Action> actions = new ArrayList<>();
        actions.add(new ActionImageChange(sheet.getImage(4, 0)));
        actions.add(new ActionTalk("You think THAT can stop me?", "asriel_text"));
        actions.add(new ActionTriggerPreAttack());
        lastAction = "FIGHT";
        return actions;
    }

    @Override
    public List<Action> onACT(String option) {
        System.out.println("ASRIEL was acted on with \"" + option + "\"!");
        List<Action> actions = new ArrayList<>();
        if (option.equals("Flirt")) {
            actions.add(new ActionDialog("* You flirt with Asriel."));
            actions.add(new ActionImageChange(sheet.getImage(1, 0)));
            actions.add(new ActionTalk("Wha-what?", "asriel_text"));
            actions.add(new ActionTalk("What are you doing?", "asriel_text"));
            actions.add(new ActionImageChange(sheet.getImage(2, 0)));
            actions.add(new ActionTalk("Take this seriously! Focus on the fight!", "asriel_text"));
        } else if (option.equals("Insult")) {
            actions.add(new ActionImageChange(sheet.getImage(0, 0)));
            actions.add(new ActionTalk("Hey, I know we're battling and all, but...", "asriel_text"));
            actions.add(new ActionTalk("...You don't need to make it personal, you know?", "asriel_text"));
            actions.add(new ActionTalk("......", "asriel_text"));
            actions.add(new ActionTalk("I wasn't going to use this, but since you're being a little nasty...", "asriel_text"));
            actions.add(new ActionTalk("...I'll use this move a skeleton taught me!", "asriel_text"));
        }
        actions.add(new ActionTriggerPreAttack());
        lastAction = option;
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
    public void setAnimation(Animation animation) {
        //derp
    }

    @Override
    public String getMusic() {
        return "Asriel"; //TODO: re-crop histrousle and fade out?
    }
}
