package ryan.shavell.main.logic.battle.attacks;

import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.ImageLoader;
import ryan.shavell.main.stuff.Log;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile implements Drawable {

    protected double x, y;
    private double angle = 0;

    protected double oldX, oldY;

    protected int hitbox_niceness = 1;

    //protected double drawAngle = 0;
    protected Animation animation;
    protected BufferedImage frame = null;

    protected double moveSpeed = 2;

    protected boolean movingAtAngle = false;

    private boolean shouldDelete = false;

    private int damage = 3;

    protected Rectangle hitbox = null;

    public Projectile(double x, double y, String img) {
        this(x, y, ImageLoader.getImage(img));
    }

    public Projectile(double x, double y, BufferedImage img) {
        this.x = x;
        this.y = y;
        oldX = x;
        oldY = y;
        if (img != null) {
            animation = new Animation(0, img);
            frame = animation.getImageWithoutIncrement();
        }
    }

    public Projectile(double x, double y, Animation a) {
        this.x = x;
        this.y = y;
        if (a != null) {
            animation = a;
            frame = a.getImageWithoutIncrement();
        }
    }

    public Projectile setMoveSpeed(double speed) {
        moveSpeed = speed;
        return this;
    }

    public Projectile moveAtAngle(double angle) {
        this.angle = angle;
        movingAtAngle = true;
        return this;
    }

    public boolean isMovingAtAngle() {
        return movingAtAngle;
    }

    public double getAngle() {
        return angle;
    }

    public void onCollision() {}

    @Override
    public void tick() {
        oldX = x;
        oldY = y;
        if (movingAtAngle) {
            double rad = Math.toRadians(angle);
            x += Math.sin(rad) * moveSpeed;
            y += Math.cos(rad) * moveSpeed;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double newX) {
        x = newX;
    }

    public void setY(double newY) {
        y = newY;
    }

    public int getXRound() {
        return (int) Math.round(x);
    }

    public int getYRound() {
        return (int) Math.round(y);
    }

    public double getWidth() {
        return frame.getWidth();
    }

    public double getHeight() {
        return frame.getHeight();
    }

    public void setHitboxNiceness(int i) {
        hitbox_niceness = i;
    }

    public Shape getHitbox() {
        //TODO: account for rotating hitbox to match sprite
        if (hitbox == null || (oldX != x || oldY != y)) {
            if (frame != null) {
                hitbox = new Rectangle(getXRound() + hitbox_niceness, getYRound() + hitbox_niceness, frame.getWidth() - (hitbox_niceness * 2), frame.getHeight() - (hitbox_niceness * 2));
            } else {
                Log.d("FRAME IS NULL!");
            }
        }
        if (hitbox == null) Log.d("RETURNING NULL HITBOX!");
        return hitbox;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int d) {
        damage = d;
    }

    public boolean shouldDelete() {
        return shouldDelete;
    }

    public void setShouldDelete(boolean b) {
        shouldDelete = b;
    }

    @Override
    public void draw(Graphics2D g) {
        frame = animation.getImage();
        g.drawImage(frame, getXRound(), getYRound(), null);
    }
}
