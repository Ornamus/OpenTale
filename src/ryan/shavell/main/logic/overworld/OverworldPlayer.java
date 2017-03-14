package ryan.shavell.main.logic.overworld;

import ryan.shavell.main.logic.InputTaker;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Log;
import ryan.shavell.main.stuff.Utils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class OverworldPlayer extends OverworldEntity implements InputTaker {

    private SpriteSheet sheet;
    private Animation up, left, right, down;

    private Animation currentAnimation;
    private boolean moving = false;
    private boolean upHeld = false, downHeld = false, leftHeld = false, rightHeld = false;

    private final int ticksPerFrame = 6;
    private final int moveSpeed = 2;

    public OverworldPlayer(int x, int y) {
        super(x, y, "frisk");
        sheet = new SpriteSheet(19, 30, 6, 2, "frisk");

        up = new Animation(ticksPerFrame, sheet.get(0, 1), sheet.get(1, 1), sheet.get(2, 1), sheet.get(3, 1));
        down = new Animation(ticksPerFrame, sheet.get(0, 0), sheet.get(1, 0), sheet.get(2, 0), sheet.get(3, 0));
        left = new Animation(ticksPerFrame, sheet.get(4, 0), sheet.get(5, 0));
        right = new Animation(ticksPerFrame, sheet.get(4, 1), sheet.get(5, 1));

        currentAnimation = down;
    }

    @Override
    public void draw(Graphics2D g) {
        if (!moving) {
            currentAnimation.reset();
            currentAnimation.setCurrentFrame(1);
            currentAnimation.setPaused(true);
        }
        g.drawImage(currentAnimation.getImage(), x, y, null);
    }

    private int direction = 0;

    @Override
    public void tick() {
        super.tick();
        int oldX = x;
        int oldY = y;
        Animation oldAnim = currentAnimation;
        if (!Overworld.blockPlayerMovement()) {
            //Map m = Overworld.getMap();
            if (leftHeld) {
                x -= moveSpeed;
            }
            if (rightHeld) {
                x += moveSpeed;
            }

            for (Rectangle r : Overworld.getAllCollisions()) {
                Rectangle box = getCollisionBox();
                while (box.intersects(r)) {
                    if (x > oldX) x--;
                    else if (x < oldX) x++;
                    box = getCollisionBox();
                }
            }

            if (upHeld) {
                y -= moveSpeed;
            }
            if (downHeld) {
                y += moveSpeed;
            }

            for (Rectangle r : Overworld.getAllCollisions()) {
                Rectangle box = getCollisionBox();
                while (box.intersects(r)) {
                    if (y > oldY) y--;
                    else if (y < oldY) y++;
                    box = getCollisionBox();
                }
            }
        }

        boolean newMoving = x != oldX || y != oldY;

        int newDirection = 0;
        if (x > oldX) newDirection = 1;
        else if (x < oldX) newDirection = -1;

        boolean redoAnim = newMoving != moving || newDirection != direction;
        if ((currentAnimation == left && !leftHeld) || (currentAnimation == right && !rightHeld) ||
                (currentAnimation == up && !upHeld) || (currentAnimation == down && !downHeld)) {
            redoAnim = true;
        }

        if (redoAnim && !Overworld.blockPlayerMovement()) {
            if (leftHeld) {
                currentAnimation = left;
            }
            if (rightHeld) {
                currentAnimation = right;
            }
            if (upHeld) {
                currentAnimation = up;
            }
            if (downHeld) {
                currentAnimation = down;
            }
        }

        if ((oldAnim != currentAnimation) || newMoving != moving) currentAnimation.reset();
        moving = newMoving;
        direction = newDirection;
    }

    @Override
    public Rectangle getCollisionBox() {
        BufferedImage frame = currentAnimation.getImageWithoutIncrement();
        //return new Rectangle(x, y + (frame.getWidth() / 2), frame.getWidth(), frame.getHeight() / 2);
        return new Rectangle(x, y + (frame.getWidth()), frame.getWidth(), Utils.round(frame.getHeight() / 2.75));
    }

    /**
     * Gets the direction the player is currently facing.
     *
     * @return The direction the plyer is facing 0 for up, 1 for left, 2 for right, 3 for down.
     */
    public int getDirection() {
        if (currentAnimation == up) return 0;
        else if (currentAnimation == left) return 1;
        else if (currentAnimation == right) return 2;
        else if (currentAnimation == down) return 3;
        else {
            Log.w("OverworldPlayer.getDirection() returned -1!");
            return -1;
        }
    }

    @Override
    public void onKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP) {
            upHeld = true;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            downHeld = true;
        } else if (keyCode == KeyEvent.VK_LEFT) {
            leftHeld = true;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            rightHeld = true;
        } else if (keyCode == KeyEvent.VK_Z) { //Interacting

        }
    }

    @Override
    public void onKeyRelease(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP) {
            upHeld = false;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            downHeld = false;
        } else if (keyCode == KeyEvent.VK_LEFT) {
            leftHeld = false;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            rightHeld = false;
        }
    }

    public double getWidth() {
        return currentAnimation.getImageWithoutIncrement().getWidth();
    }

    public double getHeight() {
        return currentAnimation.getImageWithoutIncrement().getHeight();
    }
}
