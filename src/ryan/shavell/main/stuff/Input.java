package ryan.shavell.main.stuff;

import ryan.shavell.main.logic.InputTaker;
import ryan.shavell.main.render.Board;
import ryan.shavell.main.render.Drawable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Input implements KeyListener, MouseListener {

    private static List<Integer> keysPressed = new ArrayList<>();

    public static boolean isPressed(int keycode) {
        return keysPressed.contains(keycode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean newPress = false;
        if (!keysPressed.contains(e.getKeyCode())) {
            keysPressed.add(e.getKeyCode());
            newPress = true;
        }

        if (newPress) {
            for (Drawable d : new ArrayList<>(Board.getDrawables())) {
                if (d instanceof InputTaker) {
                    InputTaker t = (InputTaker) d;
                    t.onKeyPress(e);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove((Object)e.getKeyCode());

        for (Drawable d : new ArrayList<>(Board.getDrawables())) {
            if (d instanceof InputTaker) {
                InputTaker t = (InputTaker) d;
                t.onKeyRelease(e);
            }
        }
    }

    //Not needed or used
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        //TODO: Implement
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //TODO: Implement
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //TODO: Implement
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //TODO: Implement
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //TODO: Implement
    }
}
