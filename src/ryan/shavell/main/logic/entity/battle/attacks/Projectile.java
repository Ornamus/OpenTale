package ryan.shavell.main.logic.entity.battle.attacks;

import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.ImageLoader;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile implements Drawable {

    private double x, y;
    private double angle = 0;

    protected int hitbox_niceness = 1;

    protected double drawAngle = 0;
    protected BufferedImage image;

    protected double moveSpeed = 2;
    protected boolean rotateToAngle = false;

    private int damage = 3;

    protected Rectangle hitbox = null;

    public Projectile(int x, int y, double angleOfMovement, String img) {
        this(x, y, angleOfMovement, ImageLoader.getImage(img));
    }

    public Projectile(int x, int y, double angleOfMovement, BufferedImage img) {
        this.x = x;
        this.y = y;
        angle = angleOfMovement;
        if (img != null) image = img;
        if (image == null) {
            System.out.println("OH NO");
        }
    }

    public Projectile setMoveSpeed(double speed) {
        moveSpeed = speed;
        return this;
    }

    @Override
    public void tick() {
        double oldX = x;
        double oldY = y;
        double rad = Math.toRadians(angle);
        x += Math.sin(rad) * moveSpeed;
        y += Math.cos(rad) * moveSpeed;
        if (oldX != x || oldY != y) {
            hitbox = null;
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
        if (hitbox == null) {
            if (rotateToAngle) {
                //TODO
            } else {
                hitbox = new Rectangle(getXRound() + hitbox_niceness, getYRound() + hitbox_niceness, image.getWidth() - (hitbox_niceness * 2), image.getHeight() - (hitbox_niceness * 2));
            }
        }
        return hitbox;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public void draw(Graphics2D g) {
        if (rotateToAngle) {
            //TODO
        } else {
            g.drawImage(image, getXRound(), getYRound(), null);
        }
    }
}
