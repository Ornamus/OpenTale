package ryan.shavell.main.logic.battle;

import ryan.shavell.main.dialogue.actions.Action;
import ryan.shavell.main.logic.battle.attacks.Attack;
import ryan.shavell.main.resources.Animation;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class Mob {

    private int y;

    private int maxHealth = 10, currentHealth = 10, attack = 0, defense = 0;
    private String name = null;

    private String lastAction = "none";

    private boolean boss = false;
    private boolean spareable = false;
    private boolean fleeable = true;

    public Mob(int y) {
        this.y = y;
    }

    public Mob setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        currentHealth = maxHealth;
        return this;
    }

    public Mob setName(String name) {
        this.name = name;
        return this;
    }

    public Mob setAttack(int atk) {
        attack = atk;
        return this;
    }

    public Mob setDefense(int def) {
        defense = def;
        return this;
    }

    public Mob setBoss(boolean boss) {
        this.boss = boss;
        return this;
    }

    public String getCheckInfo() {
        return "* " + name.toUpperCase() +  " - ATK " + getAttack() + " DEF " + getDefense();
    }

    public void setCurrentHealth(int h) {
        this.currentHealth = h;
    }

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getYOffset() {
        return y;
    }

    public boolean shouldDoubleSize() {
        return true;
    }

    public boolean isBoss() {
        return boss;
    }

    public double getHealthPercent() {
        return (getCurrentHealth() * 1.0) / (getMaxHealth() * 1.0);
    }

    public void setLastAction(String s) {
        lastAction = s;
    }

    public String getLastAction() {
        return lastAction;
    }

    public boolean isSpareable() {
        return spareable;
    }

    public void setSpareable(boolean s) {
        spareable = s;
    }

    public boolean isFleeable() {
        return fleeable;
    }

    public void setFleeable(boolean b) {
        fleeable = b;
    }

    public abstract int getX();

    public abstract String getNewTurnText();
    public abstract Attack getNextAttack();

    public abstract List<Action> onPreAttack();
    public abstract List<Action> onAfterAttack(Attack a);

    public abstract List<Action> onAttack();
    public abstract List<Action> onACT(String option);
    public abstract List<Action> onSPARE();

    public abstract String[] getACT();

    public abstract void draw(Graphics2D g, int x, int y, boolean hit);

    //public abstract BufferedImage getImage();

    //public abstract BufferedImage getHitImage();

    public abstract void setAnimation(Animation animation);
    public abstract void setImage(BufferedImage image);

    public abstract String getMusic();
}
