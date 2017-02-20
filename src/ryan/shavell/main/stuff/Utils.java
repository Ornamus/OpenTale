package ryan.shavell.main.stuff;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Utils {

    private BufferedImage cropImage(BufferedImage src, Rectangle rect) {
        BufferedImage dest = src.getSubimage(0, 0, rect.width, rect.height);
        return dest;
    }
}
