package ryan.shavell.main.core.player;

import java.awt.*;

public abstract class Weapon {

    private String name;
    private int damage;
    private String sound;

    public Weapon(String name, int damage, String sound) {
        this.name = name;
        this.damage = damage;
        this.sound = sound;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public String getSound() {
        return sound;
    }

    public abstract void reset();

    public abstract void drawEffect(int x, int y, Graphics2D g);

    public abstract boolean isAnimationDone();
}
