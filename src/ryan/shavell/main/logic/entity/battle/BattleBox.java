package ryan.shavell.main.logic.entity.battle;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.core.player.PlayerInfo;
import ryan.shavell.main.logic.InputTaker;
import ryan.shavell.main.logic.SoulType;
import ryan.shavell.main.logic.entity.battle.attacks.Attack;
import ryan.shavell.main.logic.entity.battle.attacks.Projectile;
import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.AudioHandler;
import ryan.shavell.main.stuff.Utils;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

//TODO: figure out if the new y positioning is correct or not (is it always growing from the bottom, or centered until it's too big THEN growing from bottom?
//TODO: is pre-programmed/constructor-passed x and y redundant now, since resizing and recalculatingBounds throws those values that out the window?
//TODO: resize box and show heart WHILE pre-attack mob dialog is happening instead of waiting until the dialog is complete
//TODO: Figure out why holding Z makes blue hearts have input lag (might not actually be a thing?)

/**
 * Contains all the logic and variables for handling monster attacks and SOUL stuff.
 *
 * @author Ornamus
 * @version 2017.3.2
 */
public class BattleBox implements Drawable, InputTaker {

    private static final int SOUL_SPEED = 3;

    private boolean keyUp, keyLeft, keyRight, keyDown;

    private int x = 50, y = 251, width = 165, height = 140; //defaults
    private int soulX, soulY;
    private Rectangle hitbox = null;
    private Attack attack = null;

    private boolean init = false;
    private boolean doingResize = false;
    private boolean done = false;

    private Long startTime = null;

    private boolean immune = false;
    private Long timeSinceHit = null;
    private double immuneTime = 1;

    private Line2D left, right, top, bottom;

    public BattleBox(int x) {
        this.x = x - (width / 2);
        reset();
    }

    public void recalculateBounds() {
        if (attack != null && !attack.isDone()) {
            width = attack.getWidth();
            height = attack.getHeight();
        }
        soulX = x + (width / 2) - 8;
        soulY = y + (height / 2) - 8;
        hitbox = null;

        left = new Line2D.Double(x + 4, y, x + 4, y + height);
        right = new Line2D.Double(x + width - 3, y, x + width - 3, y + height);
        top = new Line2D.Double(x, y + 4, x + width, y + 4);
        bottom = new Line2D.Double(x, y + height - 3, x + width, y + height - 3);
    }

    public void reset() {
        keyUp = false;
        keyLeft = false;
        keyRight = false;
        keyDown = false;

        recalculateBounds();

        startTime = System.currentTimeMillis();

        init = false;
        doingResize = false;
        done = false;
    }

    private boolean falling = false;
    private int jumpHeight = 0;
    private int momentum = 0;

    int ticksSinceMoment = 0;

    @Override
    public void tick() {
        boolean wasResizing = doingResize;
        if (!doingResize) {
            if (!init) {
                attack.start();
                init = true;
            }
            SoulType t = Arena.getSoulType();
            int oldX = soulX;
            int oldY = soulY;
            if (t == SoulType.NORMAL) {
                if (keyUp) soulY -= SOUL_SPEED;
                if (keyLeft) soulX -= SOUL_SPEED;
                if (keyRight) soulX += SOUL_SPEED;
                if (keyDown) soulY += SOUL_SPEED;
            } else if (t == SoulType.BLUE) {
                if (keyLeft) soulX -= SOUL_SPEED;
                if (keyRight) soulX += SOUL_SPEED;
                if (keyUp) {
                    if (!falling && jumpHeight <= 60) { //TODO: tune
                        soulY -= 4;
                        momentum = 2;
                        jumpHeight += 4;
                    } else {
                        falling = true;
                    }
                }

                hitbox = null;
                if (bottom.getY1() != soulY + 16) {
                    if (!keyUp) falling = true;
                } else {
                    falling = false;
                    jumpHeight = 0;
                }
                //System.out.println("Bottom y: " + bottom.getY1() + ", checking y: " + (soulY + 16) + ", falling; " + falling);
                if (falling) {
                    if (momentum > -5) {
                        if (ticksSinceMoment >= 1) {
                            momentum--;
                            ticksSinceMoment = 0;
                        } else {
                            ticksSinceMoment++;
                        }
                    }
                    soulY -= momentum;
                }
                //System.out.println("Falling: " + falling);
            }
            if (oldX != soulX || oldY != soulY) {
                hitbox = null;
            }

            Rectangle box = getSoulHitbox();
            while (box.intersectsLine(left) || box.intersectsLine(right) || box.intersectsLine(top) || box.intersectsLine(bottom)) {
                int diffOldX = soulX;
                int diffOldY = soulY;
                if (box.intersectsLine(left)) soulX++;
                else if (box.intersectsLine(right)) soulX--;
                else if (box.intersectsLine(top)) soulY++;
                else if (box.intersectsLine(bottom)) soulY--;
                if (diffOldX != soulX || diffOldY != soulY) {
                    hitbox = null;
                    box = getSoulHitbox();
                }
            }
            if (timeSinceHit != null) {
                double seconds = ((System.currentTimeMillis() - timeSinceHit) * 1.0) / 1000.0;
                if (seconds > immuneTime && immune) immune = false;
            }
            if (attack != null) {
                attack.tick();
                for (Projectile p : attack.getProjectiles()) {
                    if (p.getHitbox() != null) {
                        if (box.intersects(p.getHitbox()) && !immune) {
                            AudioHandler.playEffect("soul_hit");
                            immune = true;
                            timeSinceHit = System.currentTimeMillis();
                            PlayerInfo.currentHealth -= p.getDamage();
                            if (PlayerInfo.currentHealth <= 0) PlayerInfo.currentHealth = 0;
                            Arena.getSoulType().getDamagedAnimation().reset(); //TODO; put this somewhere better?
                        }
                    }
                }
            }

        } else {
            int rateOfChange = 24;
            if (currentWidth == endWidth && currentHeight == endHeight) {
                doingResize = false;
            } else {
                int oldWidth = currentWidth;
                int oldHeight = currentHeight;

                if (currentWidth < endWidth) currentWidth += rateOfChange;
                else if (currentWidth > endWidth) currentWidth -= rateOfChange;
                if (currentHeight < endHeight) currentHeight += rateOfChange;
                else if (currentHeight > endHeight) currentHeight -= rateOfChange;

                if (Utils.getSign(currentWidth - endWidth) != Utils.getSign(oldWidth - endWidth)) {
                    currentWidth = endWidth;
                }
                if (Utils.getSign(currentHeight - endHeight) != Utils.getSign(oldHeight - endHeight)) {
                    currentHeight = endHeight;
                }

                Point renderCoords = calculateXY(currentWidth, currentHeight);

                renderX = renderCoords.x;
                renderY = renderCoords.y;
                /*
                renderX = (Main.WIDTH / 2) - (currentWidth / 2);

                renderY = 391 - currentHeight;
                */
            }
        }
        boolean notAttacking = (attack == null && System.currentTimeMillis() - startTime >= 500) || (attack != null && attack.isDone());
        boolean justFinishedResize = wasResizing && !isResizing();
        if (notAttacking && !isResizing() && !justFinishedResize && !done) {
            doResizeAnimation(width, height, Arena.getDialogBox().getWidth(), Arena.getDialogBox().getHeight());
        } else if (notAttacking && justFinishedResize && !done) {
            done = true;
        }
    }

    private int currentWidth, currentHeight, endWidth, endHeight, renderX, renderY;

    public void doResizeAnimation(int startWidth, int startHeight, int endWidth, int endHeight) {
        width = endWidth;
        height = endHeight;
        Point newCoords = calculateXY(width, height);
        x = newCoords.x;
        y = newCoords.y;
        //TODO: y
        recalculateBounds();

        this.currentWidth = startWidth;
        this.currentHeight = startHeight;
        this.endWidth = endWidth;
        this.endHeight = endHeight;
        doingResize = true;
    }

    public Point calculateXY(int cWidth, int cHeight) {
        int cX = (Main.WIDTH / 2) - (cWidth / 2);
        int cY = 391 - cHeight;
        return new Point(cX, cY);
    }

    public boolean isResizing() {
        return doingResize;
    }

    public boolean isDone() {
        return done;
    }

    public Point getSOUL() {
        return new Point(soulX, soulY);
    }

    @Override
    public void draw(Graphics2D g) {
        drawBox(g);
        if (!doingResize && (attack == null || !attack.isDone())) {
            BufferedImage i = immune ? Arena.getSoulType().getDamagedAnimation().getImage() : Arena.getSoulType().getImage();
            g.drawImage(i, soulX, soulY, null);
            if (attack != null) attack.draw(g);
        }
    }

    public void drawBox(Graphics2D g) {
        int displayX = doingResize ? renderX : x;
        int displayY = doingResize ? renderY : y;
        int displayWidth = doingResize ? currentWidth : width;
        int displayHeight = doingResize ? currentHeight : height;

        g.setColor(Color.BLACK);
        g.fillRect(displayX, displayY, displayWidth, displayHeight);

        for (int i = 0; i < 5; i++) {
            g.setColor(Color.WHITE);
            g.drawRect(displayX + i, displayY + i, displayWidth - (i * 2), displayHeight - (i * 2));
        }
    }

    public Rectangle getSoulHitbox() {
        if (hitbox == null) {
            hitbox = new Rectangle(soulX + 1, soulY + 1, 14, 14);
        }
        return hitbox;
    }

    public Attack getAttack() {
        return attack;
    }

    public void setAttack(Attack a) {
        attack = a;
        reset();
    }

    @Override
    public void onKeyPress(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) keyUp = true;
        else if (key == KeyEvent.VK_LEFT) keyLeft = true;
        else if (key == KeyEvent.VK_RIGHT) keyRight = true;
        else if (key == KeyEvent.VK_DOWN) keyDown = true;

    }

    @Override
    public void onKeyRelease(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) keyUp = false;
        else if (key == KeyEvent.VK_LEFT) keyLeft = false;
        else if (key == KeyEvent.VK_RIGHT) keyRight = false;
        else if (key == KeyEvent.VK_DOWN) keyDown = false;
    }
}
