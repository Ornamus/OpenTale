package ryan.shavell.main.logic.battle.encounters;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.dialogue.actions.Action;
import ryan.shavell.main.logic.battle.Mob;
import ryan.shavell.main.logic.battle.attacks.Attack;
import ryan.shavell.main.logic.battle.attacks.BoogieAttack;
import ryan.shavell.main.logic.battle.attacks.JSAttack;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Boogie extends Mob {

    public SpriteSheet sheet;
    private Animation current;

    public Boogie() {
        super(120);
        setName("Boogie");
        sheet = new SpriteSheet(37, 42, 4, 1, "boogie/boogie_battle");
        setImage(sheet.get(0, 0));
    }

    @Override
    public int getX() {
        return (Main.WIDTH / 2) - (current.getImageWithoutIncrement().getWidth());
    }

    @Override
    public String getNewTurnText() {
        return "* How are you reading this? :(";
    }

    @Override
    public Attack getNextAttack() {
        return new BoogieAttack();
    }

    @Override
    public List<Action> onPreAttack() {
        return new ArrayList<>();
    }

    @Override
    public List<Action> onAfterAttack(Attack a) {
        return new ArrayList<>();
    }

    @Override
    public List<Action> onAttack() {
        return new ArrayList<>();
    }

    @Override
    public List<Action> onACT(String option) {
        return new ArrayList<>();
    }

    @Override
    public List<Action> onSPARE() {
        return new ArrayList<>();
    }

    @Override
    public String[] getACT() {
        return new String[0];
    }

    @Override
    public void draw(Graphics2D g, int x, int y, boolean hit) {
        BufferedImage i = current.getImage();
        g.drawImage(i, x, y, i.getWidth() * 2, i.getHeight() * 2, null);
    }

    @Override
    public void setAnimation(Animation animation) {
        current = animation;
    }

    @Override
    public void setImage(BufferedImage image) {
        current = new Animation(0, image);
    }

    @Override
    public String getMusic() {
        return null;
    }
}
