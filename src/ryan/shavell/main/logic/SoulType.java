package ryan.shavell.main.logic;

import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;

import java.awt.image.BufferedImage;

/**
 * Contains data for SoulTypes.
 *
 * @author Ornamus
 * @version 2017.2.26
 */
public enum SoulType {
    NORMAL(0, 0),
    GREEN(2, 0),
    YELLOW(4, 0),
    ORANGE(0, 1),
    PURPLE(2, 1),
    BLUE(4, 1);

    private static SpriteSheet souls = new SpriteSheet(16, 16, 6, 2, "souls");

    private int x, y;
    private Animation damaged = null;

    SoulType(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Animation getDamagedAnimation() {
        if (damaged == null) {
            damaged = new Animation(3, getDamagedImage(), getImage());
        }
        return damaged;
    }

    public BufferedImage getImage() {
        return souls.get(x, y);
    }

    public BufferedImage getDamagedImage() {
        return souls.get(x + 1, y);
    }
}
