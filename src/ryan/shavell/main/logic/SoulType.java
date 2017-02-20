package ryan.shavell.main.logic;

import ryan.shavell.main.resources.SpriteSheet;

import java.awt.image.BufferedImage;

/**
 * Created by Ryan Shavell on 2/19/2017.
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

    SoulType(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public BufferedImage getImage() {
        return souls.getImage(x, y);
    }

    public BufferedImage getDamagedImage() {
        return souls.getImage(x + 1, y);
    }
}
