package ryan.shavell.main.render;

import ryan.shavell.main.logic.entity.overworld.Overworld;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel implements ActionListener {

    private static List<Drawable> add = new ArrayList<>();
    private static List<Drawable> remove = new ArrayList<>();

    private static List<Drawable> drawables = new ArrayList<>();

    private static int xOffset = 0, yOffset = 0;

    private Timer t;

    private double scale;

    public static Board self = null;

    long start;
    public Board() {
        self = this;

        //drawables.add(new Arena(new StoryshiftAsriel()));
        //drawables.add(new Arena(new Chara()));

        drawables.add(new Overworld());

        t = new Timer(25, this);
        t.start();
        start = System.currentTimeMillis();
    }

    public static void add(Drawable d) {
        add.add(d);
    }

    public static void remove(Drawable d) {
        remove.add(d);
    }

    public static int getXOffset() {
        return xOffset;
    }

    public static int getYOffset() {
        return yOffset;
    }

    public static void setXOffset(int o) {
        xOffset = o;
    }

    public static void setYOffset(int o) {
        yOffset = o;
    }


    public static List<Drawable> getDrawables() {
        return new ArrayList<>(drawables);
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth() + 2, getHeight() + 2);
        g.setColor(Color.BLACK);

        g.translate(xOffset, yOffset);

        scale = 1;

        drawables.addAll(add);
        add.clear();
        drawables.removeAll(remove);
        remove.clear();

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
                    g.scale(0.5, 0.5);
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
