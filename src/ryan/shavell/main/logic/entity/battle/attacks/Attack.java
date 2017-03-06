package ryan.shavell.main.logic.entity.battle.attacks;

import ryan.shavell.main.logic.SoulType;
import ryan.shavell.main.logic.entity.battle.Arena;
import ryan.shavell.main.render.Drawable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO: add timer and delay functionality (NOT THREADED)
//TODO: either use x and y or remove them

public abstract class Attack implements Drawable {

    private int x, y, width, height;
    private List<Projectile> currentProjectiles = new ArrayList<>();
    private Long startTime = null;
    private double length = 15; //TODO: what default? should there be a default?
    private SoulType type = SoulType.NORMAL;

    public Attack() {
        this(165, 140);
    }

    public Attack(int width, int height) {
        this(50, 251, width, height);
    }

    public Attack(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        Arena.setSoulType(type);
    }

    public boolean isDone() {
        if (startTime != null) {
            return getSecondsSinceStart() > length;
        } else {
            return false;
        }
        //return false;
    }

    public Attack setTimeLength(double seconds) {
        length = seconds;
        return this;
    }

    public Attack setNeededSoulType(SoulType t) {
        type = t;
        return this;
    }

    public void spawnProjectile(Projectile p) {
        currentProjectiles.add(p);
    }

    public void spawnProjectiles(Projectile... ps) {
        Collections.addAll(currentProjectiles, ps);
    }

    public void spawnProjectiles(List<Projectile> ps) {
        currentProjectiles.addAll(ps);
    }

    @Override
    public void tick() {
        for (Projectile p : new ArrayList<>(currentProjectiles)) {
            if (p.shouldDelete()) {
                currentProjectiles.remove(p);
            } else {
                p.tick();
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        for (Projectile p : currentProjectiles) {
            p.draw(g);
        }
    }

    public List<Projectile> getProjectiles() {
        return new ArrayList<>(currentProjectiles);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getStartTime() {
        return startTime;
    }

    public double getSecondsSinceStart() {
        double secondsSinceStart = ((System.currentTimeMillis() - startTime) * 1.0) / 1000;
        return secondsSinceStart;
    }
}
