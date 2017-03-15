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

    protected int x, y;
    private Animation anim;
    private boolean drawHitbox = false;

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
        return new Rectangle(x, y + Utils.round(frame.getWidth() * (2.0/3.0)), frame.getWidth(), Utils.round(frame.getHeight() / 3));
    }

    public List<Action> onInteract() {
        List<Action> actions = new ArrayList<>();
        return actions;
    }

    public void setDrawHitbox(boolean b) {
        drawHitbox = b;
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(anim.getImage(), x, y, null);
        if (drawHitbox) {
            g.setColor(Color.BLUE);
            g.draw(getCollisionBox());
        }
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
