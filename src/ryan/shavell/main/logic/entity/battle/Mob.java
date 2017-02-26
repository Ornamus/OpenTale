package ryan.shavell.main.logic.entity.battle;

import ryan.shavell.main.dialogue.actions.Action;
import ryan.shavell.main.logic.entity.battle.attacks.Attack;
import ryan.shavell.main.resources.Animation;

import java.awt.image.BufferedImage;
import java.util.List;

//TODO: implement lastAction here

public abstract class Mob {

    private int y;

    private int maxHealth = 10, currentHealth = 10, attack = 0, defense = 0;
    private String name = null;

    private boolean boss = false;

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

    public int getY() {
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

    public abstract String getNewTurnText();
    public abstract Attack getNextAttack();


    public abstract List<Action> onPreAttack();
    public abstract List<Action> onAttack();
    public abstract List<Action> onACT(String option);
    public abstract List<Action> onSPARE();
    public abstract List<Action> onAfterAttack(Attack a);

    public abstract String[] getACT();

    public abstract BufferedImage getImage();

    public abstract BufferedImage getHitImage();

    public abstract void setAnimation(Animation animation);
    public abstract void setImage(BufferedImage image);

    public abstract String getMusic();
}
