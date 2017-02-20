package ryan.shavell.main.resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {

    private static Map<String, BufferedImage> images = new HashMap<>();

    public static BufferedImage getImage(String name) {
        BufferedImage i = images.get(name);
        if (i == null) {
            try {
                i = ImageIO.read(ImageLoader.class.getResourceAsStream("sprites/" + name + ".png"));
                images.put(name, i);
            } catch (Exception e) {
                e.printStackTrace();
                //TODO: handle properly
            }
        }
        return i;
    }
}
