package ryan.shavell.main.render;

import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.logic.entity.battle.Arena;
import ryan.shavell.main.logic.entity.battle.StoryshiftAsriel;
import ryan.shavell.main.logic.entity.battle.TestMob;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel implements ActionListener {

    public List<Drawable> drawables = new ArrayList<>();
    private Timer t;

    private double scale;

    public static Board self = null;

    long start;
    public Board() {
        self = this;
        /*
        drawables.add(new BasicRenderedThing(0, 0, "home"));
        drawables.add(new OverworldEntity(100, 100, "sans_temp"));
        drawables.add(new OverworldPlayer(200, 200));
        */
        drawables.add(new Arena(new StoryshiftAsriel()));
        t = new Timer(25, this);
        t.start();
        start = System.currentTimeMillis();
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);

        scale = 1;

        for (Drawable d : drawables) {
            d.tick();
        }

        for (Drawable d : drawables) {
            if (d.shouldDoubleSize()) {
                if (scale != 2) {
                    g.scale(2.0, 2.0);
                    scale = 2;
                }
            } else {
                if (scale != 1) {
                    g.scale(0.5, 0.5); //if scale is not 1, then scale is 2
                    scale = 1;
                }
            }
            d.draw(g);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
