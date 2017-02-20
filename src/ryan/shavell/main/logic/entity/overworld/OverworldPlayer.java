package ryan.shavell.main.logic.entity.overworld;

import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Input;
import java.awt.*;
import java.awt.event.KeyEvent;

public class OverworldPlayer extends OverworldEntity {

    private SpriteSheet sheet;
    private Animation up, left, right, down;

    private Animation currentAnimation;
    private boolean moving = false;

    public OverworldPlayer(int x, int y) {
        super(x, y, null);
        sheet = new SpriteSheet(19, 30, 6, 2, "frisk");

        int ticksPerFrame = 5;

        //TODO: make the first frame of the moving animations be a footstep, not a standing pose
        //TODO: Make animation not freeze when moving sideways
        //TODO: make animation quirks match Undertale's
        down = new Animation(ticksPerFrame, sheet.getImage(0, 0), sheet.getImage(1, 0), sheet.getImage(2, 0), sheet.getImage(3, 0));
        up = new Animation(ticksPerFrame, sheet.getImage(0, 1), sheet.getImage(1, 1), sheet.getImage(2, 1), sheet.getImage(3, 1));
        left = new Animation(ticksPerFrame, sheet.getImage(4, 0), sheet.getImage(5, 0));
        right = new Animation(ticksPerFrame, sheet.getImage(4, 1), sheet.getImage(5, 1));

        currentAnimation = down;
    }

    @Override
    public void draw(Graphics2D g) {
        if (moving) {
            g.drawImage(currentAnimation.getImage(), x, y, null);
        } else {
            currentAnimation.setCurrentFrame(0);
            g.drawImage(currentAnimation.getImageWithoutIncrement(), x, y, null);
        }
    }

    @Override
    public void tick() {
        super.tick();
        int oldX = x;
        int oldY = y;
        Animation oldAnim = currentAnimation;
        if (Input.isPressed(KeyEvent.VK_UP)) {
            y -= 2;
            currentAnimation = up;
        }
        if (Input.isPressed(KeyEvent.VK_LEFT)) {
            x -= 2;
            currentAnimation = left;
        }
        if (Input.isPressed(KeyEvent.VK_DOWN)) {
            y += 2;
            currentAnimation = down;
        }
        if (Input.isPressed(KeyEvent.VK_RIGHT)) {
            x += 2;
            currentAnimation = right;
        }
        if (oldAnim != currentAnimation) currentAnimation.reset();
        moving = (x + y) - (oldX + oldY) != 0;
    }
}
