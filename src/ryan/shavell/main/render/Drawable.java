package ryan.shavell.main.render;

import java.awt.*;

public interface Drawable {

    void tick();

    void draw(Graphics2D g);

    default boolean shouldDoubleSize() {
        return true;
    }
}
