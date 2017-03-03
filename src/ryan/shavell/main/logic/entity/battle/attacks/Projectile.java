package ryan.shavell.main.logic.entity.battle.attacks;

import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.ImageLoader;

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
    protected boolean rotateToAngle = false;

    protected boolean movingAtAngle = false;

    private int damage = 3;

    protected Rectangle hitbox = null;

    public Projectile(int x, int y, String img) {
        this(x, y, ImageLoader.getImage(img));
    }

    public Projectile(int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        oldX = x;
        oldY = y;
        if (img != null) animation = new Animation(0, img);
    }

    public Projectile(int x, int y, Animation a) {
        this.x = x;
        this.y = y;
        if (a != null) animation = a;
    }

    public Projectile setMoveSpeed(double speed) {
        moveSpeed = speed;
        return this;
    }

    public Projectile moveAtAngle(double angle) {
        this.angle = angle;
        movingAtAngle = true;
        rotateToAngle = true;
        return this;
    }

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

    public int getXRound() {
        return (int) Math.round(x);
    }

    public int getYRound() {
        return (int) Math.round(y);
    }

    public Rectangle getHitbox() {
        if (hitbox == null || (oldX != x || oldY != y)) {
            //if (rotateToAngle) {

            //} else {
                if (frame != null) {
                    hitbox = new Rectangle(getXRound() + hitbox_niceness, getYRound() + hitbox_niceness, frame.getWidth() - (hitbox_niceness * 2), frame.getHeight() - (hitbox_niceness * 2));
                }
            //}
        }
        return hitbox;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public void draw(Graphics2D g) {
        frame = animation.getImage();
        //if (rotateToAngle) {
            //g.drawImage(Utils.rotate(frame, angle), getXRound(), getYRound(), null);
        //} else {
            g.drawImage(frame, getXRound(), getYRound(), null);
        //}
    }
}
