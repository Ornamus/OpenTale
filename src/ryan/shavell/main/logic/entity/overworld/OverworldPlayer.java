package ryan.shavell.main.logic.entity.overworld;

import ryan.shavell.main.logic.InputTaker;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Input;
import java.awt.*;
import java.awt.event.KeyEvent;

public class OverworldPlayer extends OverworldEntity implements InputTaker {

    private SpriteSheet sheet;
    private Animation up, left, right, down;

    private Animation currentAnimation;
    private boolean moving = false;
    private boolean upHeld = false, downHeld = false, leftHeld = false, rightHeld = false;

    private final int ticksPerFrame = 6;
    private final int moveSpeed = 2;

    public OverworldPlayer(int x, int y) {
        super(x, y, null);
        sheet = new SpriteSheet(19, 30, 6, 2, "frisk");

        down = new Animation(ticksPerFrame, sheet.getImage(0, 0), sheet.getImage(1, 0), sheet.getImage(2, 0), sheet.getImage(3, 0));
        up = new Animation(ticksPerFrame, sheet.getImage(0, 1), sheet.getImage(1, 1), sheet.getImage(2, 1), sheet.getImage(3, 1));
        left = new Animation(ticksPerFrame, sheet.getImage(4, 0), sheet.getImage(5, 0));
        right = new Animation(ticksPerFrame, sheet.getImage(4, 1), sheet.getImage(5, 1));

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

    @Override
    public void tick() {
        super.tick();
        int oldX = x;
        int oldY = y;
        Animation oldAnim = currentAnimation;
        //TODO: make which animation is chosen be based off of how the X and Y coordinates are changing
        if (leftHeld) {
            x -= moveSpeed;
            currentAnimation = left;
        }
        if (rightHeld) {
            x += moveSpeed;
            currentAnimation = right;
        }
        if (upHeld) {
            y -= moveSpeed;
            currentAnimation = up;
        }
        if (downHeld) {
            y += moveSpeed;
            currentAnimation = down;
        }

        boolean newMoving = x != oldX || y != oldY;
        if (oldAnim != currentAnimation || newMoving != moving) currentAnimation.reset();
        moving = newMoving;
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
}
