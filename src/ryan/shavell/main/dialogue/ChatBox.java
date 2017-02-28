package ryan.shavell.main.dialogue;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.resources.ImageLoader;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ChatBox extends DialogBox {

    private BufferedImage bubble;

    public ChatBox(int y) {
        super(370, y, 370 + 34, y + 24);

        bubble = ImageLoader.getImage("chatbubble");

        setTextWidthLimit(200);

        setVisuals(Color.BLACK, Main.BATTLE_DIALOG);
    }

    @Override
    public void draw(Graphics2D g) {
        if (getText().equals("") || getText() == null) {
            return;
        }
        super.draw(g);
    }

    @Override
    public void drawBackground(Graphics2D g) {
        g.drawImage(bubble, getX(), getY(), null);
    }
}
