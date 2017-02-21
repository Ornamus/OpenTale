package ryan.shavell.main.dialogue;

import ryan.shavell.main.resources.ImageLoader;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ChatBox extends DialogBox{

    private BufferedImage bubble;

    public ChatBox(int y) {
        super(y);

        bubble = ImageLoader.getImage("chatbubble");
    }

    @Override
    public void drawBackground(Graphics2D g) {
        //TODO: render bubble at x and y
    }
}
