package ryan.shavell.main.core.player;

import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
public class Knife extends Weapon {

    private SpriteSheet sheet;
    private Animation anim;

    public Knife() {
        super("Knife", 10, "knife");

        sheet = new SpriteSheet(16, 66, 6, 1, "knife");
        List<BufferedImage> images = sheet.getAllImages();
        anim = new Animation(3, false, images.toArray(new BufferedImage[images.size()]));
    }

    @Override
    public void reset() {
        anim.reset();
    }

    @Override
    public void drawEffect(int x, int y, Graphics2D g) {
        g.drawImage(anim.getImage(), x + 25, y, null);
    }

    @Override
    public boolean isAnimationDone() {
        return anim.isDone();
    }
}
