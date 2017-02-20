package ryan.shavell.main.render;

import ryan.shavell.main.resources.ImageLoader;
import java.awt.*;

public class BasicRenderedThing implements Drawable {

    protected int x, y;
    private Image image = null;

    public BasicRenderedThing(int x, int y, String imageName) {
        this.x = x;
        this.y = y;
        image = ImageLoader.getImage(imageName);
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, x, y, null);
    }

    @Override
    public void tick() {}
}
