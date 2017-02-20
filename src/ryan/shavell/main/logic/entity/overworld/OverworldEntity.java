package ryan.shavell.main.logic.entity.overworld;

import ryan.shavell.main.resources.ImageLoader;
import ryan.shavell.main.render.Drawable;
import java.awt.*;

public class OverworldEntity implements Drawable {

    //TODO: animations

    protected int x, y;
    private Image image = null;

    public OverworldEntity(int x, int y, String imageName) {
        this.x = x;
        this.y = y;
        if (imageName != null) image = ImageLoader.getImage(imageName);
    }

    @Override
    public void draw(Graphics2D g) {
        if (image != null) g.drawImage(image, x, y, null);
    }

    @Override
    public void tick() {

    }

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
