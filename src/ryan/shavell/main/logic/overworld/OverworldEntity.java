package ryan.shavell.main.logic.overworld;

import ryan.shavell.main.dialogue.actions.Action;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.ImageLoader;
import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.stuff.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class OverworldEntity implements Drawable {

    //TODO: animations

    protected int x, y;
    private Animation anim;

    public OverworldEntity(int x, int y, String imageName) {
        this(x, y, ImageLoader.getImage(imageName));
    }

    public OverworldEntity(int x, int y, BufferedImage i) {
        this(x, y, new Animation(0, i));
    }

    public OverworldEntity(int x, int y, Animation a) {
        this.x = x;
        this.y = y;
        anim = a;
    }

    public Rectangle getCollisionBox() {
        BufferedImage frame = anim.getImageWithoutIncrement();
        return new Rectangle(x, y + (frame.getWidth()), frame.getWidth(), Utils.round(frame.getHeight() / 2));
    }

    public List<Action> onInteract() {
        List<Action> actions = new ArrayList<>();
        return actions;
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(anim.getImage(), x, y, null);
    }

    @Override
    public void tick() {}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
