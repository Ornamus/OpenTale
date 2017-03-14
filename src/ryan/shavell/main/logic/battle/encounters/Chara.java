package ryan.shavell.main.logic.battle.encounters;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.dialogue.actions.*;
import ryan.shavell.main.logic.battle.Mob;
import ryan.shavell.main.logic.battle.attacks.*;
import ryan.shavell.main.logic.battle.attacks.Asriel.*;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Utils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Chara extends Mob {

    private SpriteSheet sheet;
    private Animation current;
    private BufferedImage mostRecentDraw;

    private String lastAction = null;
    //private String[] randomStarts = {"..."};

    public Chara() {
        super(155);
        sheet = new SpriteSheet(34, 74, 2, 1, "frisk_chara_battle");
        mostRecentDraw = sheet.get(0, 0);
        current = new Animation(0, sheet.get(0, 0));
        setName("Frisk");
        setMaxHealth(100);
        setAttack(3);
        setBoss(true);
    }

    boolean hasNotedIsChara = false;
    boolean notedChange = false;

    @Override
    public String getNewTurnText() {
        if (!hasNotedIsChara && permaChara) {
            setName("Chara");
            hasNotedIsChara = true;
            return "* Frisk is no more. CHARA has regained physical form.";
        } else if (charaLimit > .35 && !notedChange) {
            setName("Frisk...?");
            notedChange = true;
            return "* Something seems wrong with Frisk...";
        } else {
            return "* ...";
        }
    }

    @Override
    public Attack getNextAttack() {
        Attack a = null;
        int random = Utils.randomNumber(0, 4);
        /*
        if (random == 0) a = new FireRain();
        else if (random == 1) a = new TestAttack();
        else if (random == 2) a = new TestAttack2();
        else if (random == 3) a = new TestAttack3();
        else if (random == 4) a = new BlueAttack();
        */
        a = new NothingAttack();
        return a;
    }

    @Override
    public List<Action> onPreAttack() {
        List<Action> actions = new ArrayList<>();

        actions.add(new ActionStartAttack());
        return actions;
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

        actions.add(new ActionTriggerPreAttack());
        lastAction = "FIGHT";
        return actions;
    }

    @Override
    public List<Action> onACT(String option) {
        //System.out.println("ASRIEL was acted on with \"" + option + "\"!");
        List<Action> actions = new ArrayList<>();

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

    float chara = 0.0f;
    float charaLimit = -1;
    boolean up = true;
    boolean permaChara = false;

    int vary;
    int varyChangeWait = 0;

    //TODO: the animation has been overdone. Simplify and make it look nice
    @Override
    public void draw(Graphics2D g, int x, int y, boolean hit) {
        mostRecentDraw = current.getImage();

        charaLimit = 1f - ((float) getHealthPercent() * 1f);

        int twitch = 0;

        if (chara > 1) chara = 1f;
        else if (chara < 0) chara = 0f;

        if (chara > .2 && Utils.randomNumber(0, permaChara ? 200 : 16) == 0) {
            twitch = Utils.randomNumber(0, 1) == 0 ? -2 : 2;
            if (chara > charaLimit) twitch *= 2;
        }

        if (charaLimit >= .70 && chara >= .70 && !permaChara) {
            charaLimit = 1f;
            chara = .6f;
            permaChara = true;
        }

        float frisk = 1f - ((float)Math.pow(chara, 2));
        if (chara == 1) frisk = 0;

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, frisk));
        g.drawImage(mostRecentDraw, x, y, mostRecentDraw.getWidth() * 2, mostRecentDraw.getHeight() * 2, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, chara));
        BufferedImage overlay = sheet.get(1,0);
        if (varyChangeWait >= 3) {
            vary = Utils.randomNumber(0, Math.round(5 - (charaLimit * 5)));
            vary = Utils.randomNumber(0, 1) == 0 ? -vary : vary;
            if (Utils.randomNumber(0, 3) == 0) vary = 0;
            varyChangeWait = 0;
        } else {
            varyChangeWait++;
        }
        g.drawImage(overlay, x + (chara == 1 ? 0 : vary), y, overlay.getWidth() * 2, overlay.getHeight() * 2, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        if (hit) {
            if (!permaChara) {
                int adjustedChara = Math.round(charaLimit * 10);
                int min = adjustedChara - 1 > 0 ? adjustedChara - 1 : 0;
                chara = Utils.randomNumber(min, Math.round(adjustedChara)) / 10.0f;
            }
        } else {
            if (up || permaChara) {
                chara += .0025;
            } else {
                if (!permaChara) chara -= .0025 * (chara > charaLimit ? 1.5 : 1);
            }
            float minChara = charaLimit - .3f;
            if (minChara < 0) minChara = 0;
            if (chara > charaLimit) {
                up = false;
            } else if (chara <= minChara) {
                up = true;
            }
        }
    }

    @Override
    public String getCheckInfo() {
        lastAction = "CHECK";
        return "* " + getName() + " - ATK ?? DEF ??";
        //return super.getCheckInfo() + "/n* ????";
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
        if (!notedChange) {
            return "STMPWYFC"; //TODO: re-crop histrousle and fade out?
        } else {
            return "chara";
        }
    }
}