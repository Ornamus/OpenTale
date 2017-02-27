package ryan.shavell.main.logic.entity.battle;

import ryan.shavell.main.dialogue.actions.*;
import ryan.shavell.main.logic.entity.battle.attacks.Attack;
import ryan.shavell.main.logic.entity.battle.attacks.Volty.LightningLaunchAttack;
import ryan.shavell.main.logic.entity.battle.attacks.Volty.NutSpinAttack;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.ImageLoader;
import ryan.shavell.main.stuff.Utils;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Volty extends Mob {

    private BufferedImage i;
    private String voice = "volty_voice";

    private String[] randomTalk = {". . ./n     /n    : )", ". . .", "You /nare /nM I N E", "I  O W N /nY O U .",
    "I AM/nC O M I N G  F O R/nY  O  U", "BACON DOES NOT TASTE /nGOOD./nD O  Y O U ?", "K i l l  m e . . .", "*hissing noises*",
    "I AM MAKING THE EXECUTIVE DECISION TO KILL YOU.", "SAFETY FIRST!", "WHO'S N.U.T.S.?/n:)"};

    private String[] randomFlavor = {"You are not even REMOTELY comfortable with this.", "Flowey just lost his \"Most Creepy Character\" award.",
    "Who would create such a thing?", "He'll kill you over 1,557 times.", "You're pretty sure Satan has possessed this robot.",
            "You suddenly regret that your DETERMINATION will keep you alive to remember this.", "H E L P", "Nothing could have prepared you for this.",
    "It doesn't seem to die.../nOr is it already dead?", "This is 100% NOT FINE.", "\"No, just no.\"", "Volty is making a deep gurgling noise.", "You feel very unsafe.",
    "The temperature of the room drops...", "He sees you when you're sleeping.", "\"Camp in a box.\"/nWait... wrong team.", "Literally unplayable."};

    public Volty(int y) {
        super(y);
        setName("Volty");
        setMaxHealth(130);
        setBoss(true);
        i = ImageLoader.getImage("volty");
    }

    @Override
    public String getNewTurnText() {
        if (lastTalk.contains("N.U.T.S.")) {
            return "* He's nuts.";
        } else {
            return "* " + randomFlavor[Utils.randomNumber(0, randomFlavor.length - 1)];
        }
    }

    @Override
    public Attack getNextAttack() {
        int random = Utils.randomNumber(0, 1);
        if (random == 0) {
            return new NutSpinAttack();
        } else if (random == 1) {
            return new LightningLaunchAttack();
        } else {
            return null;
        }
    }

    String lastTalk = null;

    @Override
    public List<Action> onPreAttack() {
        List<Action> actions = new ArrayList<>();
        lastTalk = randomTalk[Utils.randomNumber(0, randomTalk.length - 1)];
        actions.add(new ActionTalk(lastTalk, voice));
        actions.add(new ActionStartAttack());
        return actions;
    }

    @Override
    public List<Action> onAttack() {
        List<Action> actions = new ArrayList<>();

        actions.add(new ActionTriggerPreAttack());
        return actions;
    }

    boolean hugged = false;

    @Override
    public List<Action> onACT(String option) {
        List<Action> actions = new ArrayList<>();
        if (option.equals("Scream")) {
            actions.add(new ActionDialog("* You scream in terror."));
            actions.add(new ActionDialog("* Volty seems to like that..."));
        } else if (option.equals("Hug")) {
            if (!hugged) {
                actions.add(new ActionDialog("* You carefully hug Volty."));
                actions.add(new ActionDialog("* ..."));
                actions.add(new ActionDialog("* ..."));
                actions.add(new ActionDialog("* You feel like you may never be the same if you do that again."));
                hugged = true;
            } else {
                actions.add(new ActionDialog("* For reasons unknown, you hug Volty again."));
                actions.add(new ActionDialog("* You hear a rumbling noise within him."));
                actions.add(new ActionDialog("* ............."));
                actions.add(new ActionDialog("* ............."));
                actions.add(new ActionDialog("* . . . . . . . . . ."));
                actions.add(new ActionDialog("* .     .     .     .     ."));
                actions.add(new ActionDialog("* O  H    N  O"));
                actions.add(new ActionCrash());
            }
        } else if (option.equals("Surrender SOUL")) {
            actions.add(new ActionDialog("* You surrender your SOUL to Volty."));
            actions.add(new ActionDialog("* ..."));
            actions.add(new ActionDialog("* ...except your SOUL is already his."));
            actions.add(new ActionTalk("Y o u  h a v e/n n o t h i n g  t o/n b a r g a i n /n w i t h.", voice));
            actions.add(new ActionTalk("I  O W N  T H I S /nW O R L D .", voice));
        }

        actions.add(new ActionTriggerPreAttack());
        return actions;
    }

    @Override
    public List<Action> onSPARE() {
        List<Action> actions = new ArrayList<>();

        actions.add(new ActionTriggerPreAttack());
        return actions;
    }

    @Override
    public List<Action> onAfterAttack(Attack a) {
        List<Action> actions = new ArrayList<>();

        actions.add(new ActionPlayerTurn());
        return actions;
    }

    @Override
    public String[] getACT() {
        return new String[]{"Scream", "Hug", "Surrender SOUL"};
    }

    @Override
    public String getCheckInfo() {
        //return super.getCheckInfo() + "/n* Mettaton's demonic cousin...?";
        return "* VOLTY - ATK 1557 DEF 1902/n* Satan incarnate...?";
    }

    @Override
    public BufferedImage getImage() {
        return i;
    }

    @Override
    public BufferedImage getHitImage() {
        return i;
    }

    @Override
    public void setAnimation(Animation animation) {
        i = animation.getImage();
    }

    @Override
    public void setImage(BufferedImage image) {
        i = image;
    }

    @Override
    public String getMusic() {
        return "volty";
    }
}
